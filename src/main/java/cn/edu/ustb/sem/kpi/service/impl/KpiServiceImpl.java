package cn.edu.ustb.sem.kpi.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.core.cons.OrderStatus;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.kpi.service.KpiService;
import cn.edu.ustb.sem.kpi.web.model.OrderProduceInfo;
import cn.edu.ustb.sem.order.dao.OrderDao;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.produce.dao.ProduceAssemblingDao;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;
import cn.edu.ustb.sem.produce.service.ReportService;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;
import cn.edu.ustb.sem.schedule.service.ScheduleService;
@Service("kpiService")
public class KpiServiceImpl implements KpiService {
	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private ScheduleResultService srService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private ProduceAssemblingDao prDao;
	@Autowired
	private ReportService reportService;
	@Override
	public List<OrderProduceInfo> listCurPlan() throws ServiceException {
		//1.列出所有处于排产期的订单
		String hql = "from " + Order.class.getSimpleName() + " o where o.isReported = ?";
		List<Order> orders = this.orderDao.list(hql, -1, -1, ProduceAssembling.REPORTING);
		List<OrderProduceInfo> opis = new ArrayList<OrderProduceInfo>();
		for (Order o : orders) {
			OrderProduceInfo opi = getOpi(o); 
			opis.add(opi);
		}
		return opis;
	}
	@Override
	public Collection<OrderProduceInfo> listAYearPlan() throws ServiceException {
		Calendar now = Calendar.getInstance();
		String nowStr = DateUtil.getDate(now);
		Calendar begin = Calendar.getInstance();
		begin.set(Calendar.MONTH, Calendar.JANUARY);
		begin.set(Calendar.DAY_OF_MONTH, 1);
		String beginStr = DateUtil.getDate(begin);
		String hql = "from " + Order.class.getSimpleName() + " o where o.finishDate >= '" + beginStr + "' and o.finishDate <= '" + nowStr + "'";
		List<Order> os = this.orderDao.list(hql, -1, -1);
		List<OrderProduceInfo> opis = new ArrayList<OrderProduceInfo>();
		for (Order o : os) {
			OrderProduceInfo opi = getOpi(o); 
			opis.add(opi);
		}
		//合并同产品代号的数据
		Map<String, OrderProduceInfo> map = new HashMap<String, OrderProduceInfo>();
		for (OrderProduceInfo opi : opis) {
			String code = opi.getCode();
			if (map.containsKey(code)) {
				OrderProduceInfo ex = map.get(code);
				int planNum = ex.getProduceNum() + opi.getProduceNum();
				int actualNum = ex.getProcessNum() + opi.getProcessNum();
				ex.setProduceNum(planNum);
				ex.setProcessNum(actualNum);
				ex.setFinishedRate((actualNum * 1.0 / planNum) + "");
				ex.setDiff(planNum - actualNum);
			} else {
				map.put(code, opi);
			}
		}
		return map.values();
	}
	
	private OrderProduceInfo getOpi(Order o) throws ServiceException {
		//2.补上每个订单的排产开始时间和排产结束时间
		Set<ScheduleResult> srs = o.getSrs();
		Calendar begin = null;
		Calendar end = null;
		for (ScheduleResult sr : srs) {
			int status = sr.getStatus();
			if (status == ScheduleResult.COMMIT || status == ScheduleResult.TINGGONG || status == ScheduleResult.PRINTED) {
				Calendar start = sr.getStart();
				if (begin == null) {
					begin = start;
				}
				if (end == null) {
					end = start;
				}
				if (start.before(begin)) {
					begin = start;
				}
				if (end.before(start)) {
					end = start;
				}
			}
		}
		OrderProduceInfo opi = new OrderProduceInfo();
		opi.setId(o.getId());
		opi.setBegin(begin);
		opi.setEnd(end);
		opi.setOrderNo(o.getNo());
		//计划排产数量
		opi.setProduceNum(o.getProduceNum());
		OrderModel om = this.reportService.getFinishedNum(o.getId());
		opi.setProcessNum(om.getFinishedNum());
		opi.setFinishedRate((om.getFinishedNum() * 1.0 / o.getProduceNum()) + "");
		opi.setUnit(om.getUnits());
		opi.setCode(o.getProductCode());
		//设置状态
		Calendar now = Calendar.getInstance();
		if (begin != null && end != null) {
			if (now.before(end) && begin.before(now)) {
				opi.setStatus("正常");
			} else if (end.before(now)) {
				opi.setStatus("延期");
			}
		} else {
			opi.setStatus("生产中");
		}
		//设置停工、外协状态
		if (o.getStatus() == OrderStatus.WAIXIE.getIndex()) {
			opi.setStatus("外协");
		}
		Set<ProduceTinggong> pts = o.getPts();
		if (pts != null) {
			for (ProduceTinggong pt : pts) {
				int status = pt.getTinggongStatus();
				if (status == ProduceTinggong.TINGGONG_COMMIT || status == ProduceTinggong.TINGGONG_REPORT) {
					opi.setStatus("停工中");
					break;
				}
			}
		}
		return opi;
	}
}
