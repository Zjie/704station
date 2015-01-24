package cn.edu.ustb.sem.produce.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.dao.UserDao;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.kpi.web.model.WorkerKpiSearchForm;
import cn.edu.ustb.sem.order.dao.OrderDao;
import cn.edu.ustb.sem.order.dao.OrderProcessDao;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderProcess;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderProcessModel;
import cn.edu.ustb.sem.order.web.model.ProduceOtherSearchForm;
import cn.edu.ustb.sem.process.service.PtProductCodeService;
import cn.edu.ustb.sem.produce.dao.PrintHistoryDao;
import cn.edu.ustb.sem.produce.dao.ProduceAssembledDao;
import cn.edu.ustb.sem.produce.dao.ProduceAssemblingDao;
import cn.edu.ustb.sem.produce.dao.ProduceDianshiDao;
import cn.edu.ustb.sem.produce.dao.ProduceOtherDao;
import cn.edu.ustb.sem.produce.dao.ProduceTestDao;
import cn.edu.ustb.sem.produce.dao.ProduceTinggongDao;
import cn.edu.ustb.sem.produce.entity.ProduceAssembled;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;
import cn.edu.ustb.sem.produce.entity.ProduceDianshi;
import cn.edu.ustb.sem.produce.entity.ProduceOther;
import cn.edu.ustb.sem.produce.entity.ProduceTest;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;
import cn.edu.ustb.sem.produce.service.ProduceAssemblingService;
import cn.edu.ustb.sem.produce.service.ReportService;
import cn.edu.ustb.sem.produce.service.TinggongHistoryService;
import cn.edu.ustb.sem.produce.web.model.ProduceAssembingModel;
import cn.edu.ustb.sem.produce.web.model.ProduceDianshiModel;
import cn.edu.ustb.sem.produce.web.model.ProduceOtherModel;
import cn.edu.ustb.sem.produce.web.model.ProduceReportForSaveModel;
import cn.edu.ustb.sem.produce.web.model.ProduceTestModel;
import cn.edu.ustb.sem.produce.web.model.ScheduledResultModel;
import cn.edu.ustb.sem.produce.web.model.TgSearchForm;
import cn.edu.ustb.sem.produce.web.model.TinggongModel;
import cn.edu.ustb.sem.schedule.dao.DispatchUnitDao;
import cn.edu.ustb.sem.schedule.dao.ScheduleResultDao;
import cn.edu.ustb.sem.schedule.dao.UnscheduleProcessGroupDao;
import cn.edu.ustb.sem.schedule.dao.WorkerDao;
import cn.edu.ustb.sem.schedule.entity.DispatchUnit;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
import cn.edu.ustb.sem.schedule.entity.Worker;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;

import com.alibaba.fastjson.JSON;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	@Autowired
	private ProduceOtherDao poDao;
	@Autowired
	private ProduceDianshiDao pdDao;
	@Autowired
	private TinggongHistoryService tghService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ScheduleResultDao srDao;
	@Autowired
	private UnscheduleProcessGroupDao unsrDao;
	@Autowired
	private ProduceAssemblingDao reportDao;
	@Autowired
	private OrderProcessDao orderProcessDao;
	@Autowired
	private PtProductCodeService ptProductCodeService;
	@Autowired
	private ScheduleResultService srService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private PrintHistoryDao phDao;
	@Autowired
	private WorkerDao workerDao;
	@Autowired
	private ProduceTinggongDao tgDao;
	@Autowired
	private ProduceAssemblingService prService;
	@Autowired
	private ProduceAssemblingDao assemblingDao;
	@Autowired
	private ProduceAssembledDao assembledDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProduceTestDao ptDao;
	@Autowired
	private DispatchUnitDao duDao;

	@Override
	public List<OrderModel> getCanReportOrder(Integer workerId)
			throws ServiceException {
		// 改为能报工该生产单元下所有的订单
		/**
		 * Worker model = new Worker(); User u = new User();
		 * u.setId(v.getUid()); model.setUser(u); Worker w =
		 * workerDao.find(model); PrintHistory phModel = new PrintHistory();
		 * phModel.setPrinter(new Worker(w.getId()));
		 * //找出该员工打印过的订单，员工只能针对这些订单进行报工 List<PrintHistory> phl =
		 * phDao.listAll(phModel, -1, -1); Set<Integer> existOrder = new
		 * HashSet<Integer>(); List<OrderModel> orders = new
		 * ArrayList<OrderModel>(); for (PrintHistory ph : phl) { Order o =
		 * ph.getOrder(); if (existOrder.contains(o.getId())) continue;
		 * orders.add(new OrderModel(o)); } return orders;
		 **/
		List<OrderModel> orders = new ArrayList<OrderModel>();
		// Worker model = new Worker();
		// User u = new User();
		// u.setId(v.getUid());
		// model.setUser(u);
		Worker w = workerDao.get(workerId);
		// 查找属于这个单元下的排产计划，并且排产结果是调度员确认过的
		ScheduleResult sr = new ScheduleResult();
		// 只选出用户确认的排产结果
		sr.setStatus(ScheduleResult.COMMIT);
		Worker wo = new Worker();
		// 只选出该工人所属的生产单元下的排产结果
		wo.setUnit(w.getUnit());
		sr.setWorker(wo);
		List<ScheduleResult> results = srDao.listAll(sr, -1, -1);
		// 保存已经选出来过的订单
		Set<Integer> exists = new HashSet<Integer>();
		for (ScheduleResult s : results) {
			Order order = s.getOrder();
			if (exists.contains(order.getId()))
				continue;
			exists.add(order.getId());
			orders.add(new OrderModel(order));
		}
		// 加上生产单元调整后的订单
		DispatchUnit sduModel = new DispatchUnit();
		sduModel.setUnit(w.getUnit());
		List<DispatchUnit> dus = this.duDao.listAll(sduModel, -1, -1);
		for (DispatchUnit d : dus) {
			Order order = d.getOrder();
			if (exists.contains(order.getId())) {
				continue;
			}
			exists.add(order.getId());
			orders.add(new OrderModel(order));
		}

		return orders;
	}

	@Override
	public void toReport(ProduceReportForSaveModel model)
			throws ServiceException {
		if (model == null) {
			throw new ServiceException("缺失参数");
		}
		List<ProduceAssembingModel> prms = model.getProduceReports();
		// 如果没有报过工，则说明这是第一次报工，为报工开始时间
		Integer orderId = model.getOrderId();
		Order order = orderDao.get(orderId);
		List<ProduceAssembling> prs = this.reportDao.getPrByOrderId(orderId);
		if (prs != null && prs.size() == 0) {
			order.setReportBeginTime(Calendar.getInstance());
			order.setIsReported(ProduceAssembling.REPORTING);
		}
		Calendar udate = Calendar.getInstance();
		if (model.getReportDate() != null && !model.getReportDate().isEmpty()) {
			try {
				udate = DateUtil.parseDate(model.getReportDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 到目前为止一共完成了多少个
		// 只要找出最小那个工序组即可
		// 注意，要限制即使是外协这样的工序组，也要报多少个完成了
		int minReportNum = 0;
		for (ProduceAssembingModel prm : prms) {
			ProduceAssembling produceReport = new ProduceAssembling();
			produceReport.setReportNum(prm.getReportNum());
			produceReport.setReportTime(prm.getReportTime());
			OrderProcess op = this.orderProcessDao.get(prm.getOpid());
			if (op != null) {
				produceReport.setOrderProcess(op);
			}
			Visitor visitor = (Visitor) SecurityContextHolder.getContext()
					.getAuthentication();
			User u = this.userDao.get(visitor.getUid());
			if (u == null)
				throw new ServiceException("不存在该用户，无法进行报工");
			Worker w = u.getWorker();
			if (w == null)
				throw new ServiceException("只有工人才能进行报工操作");

			produceReport.setWorker(w);
			produceReport.setUdate(udate);
			if (produceReport.getId() == null || produceReport.getId() <= 0) {
				reportDao.save(produceReport);
			} else {
				reportDao.update(produceReport);
			}
			int num = prm.getReportedNum();
			if (num < minReportNum) {
				minReportNum = num;
			}
		}
		// 判断订单是否已经完成所有的报工，是则更新订单的“是否完成报工”属性
		if (model.getTotalReport() == model.getTotalReportNum()) {
			order.setIsReported(ProduceAssembling.REPORTED);
			order.setReportFinishedTime(Calendar.getInstance());
			orderDao.update(order);
		}
	}

	/**
	 * needAuth: 是否需要权限验证；加上权限认证，某个工人，只能对他所在的生产单元下的订单进行报工 workerId:
	 * 用于过滤出某个员工的报工情况
	 */
	@Override
	public Map<String, Object> produceReport(Integer id, boolean needAuth,
			Integer workerId) throws ServiceException {
		Order order = this.orderDao.get(id);
		if (needAuth) {
			checkProduceReportAuth(order);
		}
		if (order == null)
			throw new ServiceException("该订单不存在");
		Worker w = null;
		if (workerId != null) {
			w = this.workerDao.get(workerId);
		}
		OrderModel orderModel = new OrderModel(order, w, null, null);
		// 填充每个工序组的最早开始时间和最迟完成时间
		List<OrderProcessModel> ops = orderModel.getOrderProcesses();
		for (OrderProcessModel op : ops) {
			String groupName = op.getGroupName() + "";
			Calendar begin = this.srService.getEarlistBeginDateTime(groupName,
					id);
			op.setBeginDate(DateUtil.getDate(begin, "yyyy-MM-dd"));
			Calendar end = this.srService.getLastEndDateTime(groupName, id);
			op.setEndDate(DateUtil.getDate(end, "yyyy-MM-dd"));
		}

		// String productCode = order.getProductCode();
		// PtProductCode model = new PtProductCode(productCode);
		// PtProductCode ptProductCode = this.ptProductCodeService.find(model);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("order", JSON.toJSONString(orderModel));

		// 获取该订单所有的装配报工详情
		Set<OrderProcess> opps = order.getOps();
		List<ProduceAssembingModel> pams = new ArrayList<ProduceAssembingModel>();
		for (OrderProcess op : opps) {
			Set<ProduceAssembling> pas = op.getPrs();
			for (ProduceAssembling pa : pas) {
				if (workerId != null
						&& !pa.getWorker().getId().equals(workerId)) {
					continue;
				}
				pams.add(new ProduceAssembingModel(pa));
			}
		}
		result.put("zhuangpei", JSON.toJSONString(pams));

		// 获取该订单的试验报工详情
		List<ProduceTestModel> ptms = new ArrayList<ProduceTestModel>();
		Set<ProduceTest> pts = order.getProduceTest();
		for (ProduceTest pt : pts) {
			if (workerId != null && !pt.getWorker().getId().equals(workerId)) {
				continue;
			}
			ptms.add(new ProduceTestModel(pt));
		}
		result.put("shiyan", JSON.toJSONString(ptms));
		// 典试报工
		List<ProduceDianshiModel> pdms = new ArrayList<ProduceDianshiModel>();
		Set<ProduceDianshi> pds = order.getProduceDianshi();
		for (ProduceDianshi pd : pds) {
			if (workerId != null && !pd.getWorker().getId().equals(workerId)) {
				continue;
			}
			pdms.add(new ProduceDianshiModel(pd));
		}
		result.put("dianshi", JSON.toJSONString(pdms));
		// 其他报工
		List<ProduceOtherModel> poms = new ArrayList<ProduceOtherModel>();
		Set<ProduceOther> pos = order.getProduceother();
		for (ProduceOther po : pos) {
			if (workerId != null && !po.getWorker().getId().equals(workerId)) {
				continue;
			}
			poms.add(new ProduceOtherModel(po));
		}
		result.put("qita", JSON.toJSONString(poms));
		// result.put("ptid", ptProductCode.getPt().getId());
		result.put("ptid", -1);
		return result;
	}

	private void checkProduceReportAuth(Order order) throws ServiceException {
		Visitor visitor = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		User u = visitor.getUser();
		Worker w = u.getWorker();
		if (w == null) {
			throw new ServiceException("只有工人才能进行报工");
		}
		List<OrderModel> orders = this.getCanReportOrder(w.getId());
		for (OrderModel o : orders) {
			if (o.getId() == order.getId()) {
				return;
			}
		}
		throw new ServiceException("您无权对此订单进行报工，如有问题，请联系管理员");
	}

	@Override
	public Map<String, Object> produceReport(String code, boolean needAuth)
			throws ServiceException {
		Order order = this.orderService.getOrderByCode(code);
		return this.produceReport(order.getId(), needAuth, null);
	}

	@Override
	public void reportTinggong(TinggongModel model) throws ServiceException {
		// 1.解析订单编码
		Order order = this.orderService.getOrderByCode(model.getCode());
		if (order == null)
			throw new ServiceException("订单编码有误，请输入正确的订单编码");

		// 2.检查订单是否处于停工状态，如果是，则拒绝停工
		if (tgDao.isTinggong(order.getId())) {
			throw new ServiceException("当前订单处于停工状态，请等待停工重启后才能再次报停工");
		}
		// 2.保存停工信息
		Visitor visitor = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		User u = new User(visitor.getUid());
		ProduceTinggong tg = new ProduceTinggong(model);
		// 谁报的工
		tg.setTinggongReporter(u);
		tg.setTinggongStatus(ProduceTinggong.TINGGONG_REPORT);
		tg.setOrder(order);
		tg.setUdate(Calendar.getInstance());
		tgDao.save(tg);

	}

	@Override
	public List<ScheduledResultModel> findOrderSrs(String code)
			throws ServiceException {
		Order order = this.orderService.getOrderByCode(code);
		Set<ScheduleResult> srs = order.getSrs();
		List<ScheduledResultModel> result = new ArrayList<ScheduledResultModel>();
		Set<Integer> existSr = new HashSet<Integer>();
		for (ScheduleResult sr : srs) {
			Integer gupId = sr.getGup().getId();
			if (existSr.contains(gupId))
				continue;
			existSr.add(gupId);
			result.add(new ScheduledResultModel(sr));
		}
		return result;
	}

	@Override
	public List<TinggongModel> getTinggongReportOrders()
			throws ServiceException {
		ProduceTinggong model = new ProduceTinggong();
		model.setTinggongStatus(ProduceTinggong.TINGGONG_REPORT);
		List<ProduceTinggong> pts = this.tgDao.listAll(model, -1, -1);
		List<TinggongModel> tms = new ArrayList<TinggongModel>();
		for (ProduceTinggong pt : pts) {
			tms.add(new TinggongModel(pt));
		}
		return tms;
	}

	@Override
	public List<TinggongModel> getTinggongCommitOrders()
			throws ServiceException {
		ProduceTinggong model = new ProduceTinggong();
		model.setTinggongStatus(ProduceTinggong.TINGGONG_COMMIT);
		List<ProduceTinggong> pts = this.tgDao.listAll(model, -1, -1);
		List<TinggongModel> tms = new ArrayList<TinggongModel>();
		for (ProduceTinggong pt : pts) {
			tms.add(new TinggongModel(pt));
		}
		return tms;
	}

	@Override
	public GridModel<TinggongModel> getTinggongHistory(TgSearchForm form)
			throws ServiceException {
		ItemModelHelper<ProduceTinggong, TinggongModel> helper = new ItemModelHelper<ProduceTinggong, TinggongModel>() {
			@Override
			public TinggongModel transfer(ProduceTinggong bo) {
				return new TinggongModel(bo);
			}
		};
		if (form == null) {
			return tghService.list(null, -1, -1, helper);
		}
		ProduceTinggong model = new ProduceTinggong();
		if (form.getOrderNo() != null && !form.getOrderNo().isEmpty()) {
			Order o = new Order();
			o.setNo(form.getOrderNo());
			model.setOrder(o);
		}
		return tghService.list(model, form.getPage(), form.getLimit(), helper);
	}

	@Override
	public GridModel<ProduceAssembingModel> getProduceReportByForm(
			WorkerKpiSearchForm form) throws ServiceException {
		if (form.getDateBegin() == null || form.getDateEnd() == null)
			throw new ServiceException("开始时间或结束时间不能为空");
		try {
			Date begin = DateUtil.parseDate(form.getDateBegin()).getTime();
			Date end = DateUtil.parseDate(form.getDateEnd()).getTime();
			String hql = "from " + ProduceAssembling.class.getSimpleName()
					+ " as pro where pro.udate >= ? and pro.udate <= ?";
			List<ProduceAssembling> prs = this.assemblingDao.list(hql,
					form.getPage(), form.getLimit(), begin, end);
			GridModel<ProduceAssembingModel> grid = new GridModel<ProduceAssembingModel>();
			List<ProduceAssembingModel> items = new ArrayList<ProduceAssembingModel>();
			for (ProduceAssembling pr : prs) {
				items.add(new ProduceAssembingModel(pr));
			}
			grid.setItems(items);
			hql = "select count(*) from "
					+ ProduceAssembling.class.getSimpleName()
					+ " as pro where pro.udate >= ? and pro.udate <= ?";
			int total = this.assemblingDao.count(hql, begin, end);
			grid.setTotalNum(total);
			return grid;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		throw new ServiceException("系统异常");
	}

	@Override
	public OrderModel getFinishedNum(Integer orderId) throws ServiceException {
		Order order = this.orderDao.get(orderId);
		if (order == null)
			throw new ServiceException("该订单不存在");
		OrderModel orderModel = new OrderModel(order);
		// 填充每个工序组的最早开始时间和最迟完成时间
		List<OrderProcessModel> ops = orderModel.getOrderProcesses();
		int minFinishedNum = -1;
		for (OrderProcessModel op : ops) {
			int num = op.getReportedNum();
			if (minFinishedNum == -1) {
				minFinishedNum = num;
			} else if (num < minFinishedNum) {
				minFinishedNum = num;
			}
		}
		orderModel.setFinishedNum(minFinishedNum);
		return orderModel;
	}

	@Override
	public void produceAssembledReport(Integer orderId, Integer num,
			Integer userId, String reportDate) throws ServiceException {
		Order o = this.orderDao.get(orderId);
		if (o == null)
			throw new ServiceException("不存在该订单");
		User u = this.userDao.get(userId);
		Worker w = u.getWorker();
		if (w == null)
			throw new ServiceException("只有工人才能进行装配报工");
		Calendar udate = Calendar.getInstance();
		if (reportDate != null && !reportDate.isEmpty()) {
			try {
				udate = DateUtil.parseDate(reportDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 投产数量 >= 在制数量 >= 完成数量
		// 先查找该订单的在制数量
		int assemblingNum = o.getAssemblingNum();
		int assembledNum = o.getAssembledNum();
		if (assemblingNum < num - assembledNum) {
			throw new ServiceException("提交完成的数量不能大于当前在制的数量");
		}
		ProduceAssembled pa = new ProduceAssembled();
		pa.setAssembleNum(num);
		pa.setOrder(o);
		pa.setReportDate(udate);
		pa.setWorker(w);
		this.assembledDao.save(pa);
	}

	@Override
	public void reportTestingNum(Integer orderId, Integer num, String content,
			String reportDate) throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		Order o = this.orderDao.get(orderId);
		if (o == null)
			throw new ServiceException("该订单不存在");
		User u = this.userDao.get(v.getUid());
		Worker w = u.getWorker();
		if (w == null)
			throw new ServiceException("只有工人才可以进行实验报工");
		// 完成的装配个数 - 在验个数 - 完成试验个数 >= 可以上报的个数
		if (o.getAssembledNum() - o.getTestedNum() - o.getTestingNum() < num) {
			throw new ServiceException("可以上报的实验个数 必须小于 完成的装配个数 - 在验个数 - 完成试验个数");
		}
		Calendar udate = Calendar.getInstance();
		if (reportDate != null && !reportDate.isEmpty()) {
			try {
				udate = DateUtil.parseDate(reportDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ProduceTest pt = new ProduceTest();
		pt.setContent(content);
		pt.setOrder(o);
		pt.setReportDate(udate);
		pt.setStatus(ProduceTest.ON_PROCESS);
		pt.setTestNum(num);
		pt.setWorker(w);
		this.ptDao.save(pt);
	}

	@Override
	public List<ProduceTestModel> getTestReportByWorker(Integer orderId)
			throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		Order o = this.orderDao.get(orderId);
		if (o == null)
			throw new ServiceException("该订单不存在");
		User u = this.userDao.get(v.getUid());
		Worker w = u.getWorker();
		if (w == null)
			throw new ServiceException("只有工人才可以进行查看");
		int wId = w.getId();
		Set<ProduceTest> pts = o.getProduceTest();
		List<ProduceTestModel> ptms = new ArrayList<ProduceTestModel>();
		for (ProduceTest pt : pts) {
			if (pt.getWorker().getId() == wId) {
				ptms.add(new ProduceTestModel(pt));
			}
		}
		return ptms;
	}

	@Override
	public void confirmProduceTest(Integer id, String reportDate) throws ServiceException {
		if (id == null)
			throw new ServiceException("请选择一个正确的试验报工");
		ProduceTest pt = this.ptDao.get(id);
		if (pt == null)
			throw new ServiceException("请选择一个正确的试验报工");
		Calendar udate = Calendar.getInstance();
		if (reportDate != null && !reportDate.isEmpty()) {
			try {
				udate = DateUtil.parseDate(reportDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		pt.setStatus(ProduceTest.FINISHED);
		pt.setConfirmDate(udate);
		this.ptDao.update(pt);
	}

	@Override
	public void reportDianshiingNum(Integer orderId, Integer num,
			String content, String reportDate) throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		Order o = this.orderDao.get(orderId);
		if (o == null)
			throw new ServiceException("该订单不存在");
		User u = this.userDao.get(v.getUid());
		Worker w = u.getWorker();
		if (w == null)
			throw new ServiceException("只有工人才可以进行典试报工");
		if (o.getTestNum() - o.getDianshied() - o.getDianshiing() < num) {
			throw new ServiceException("可以上报的典试个数 必须小于 全部典试个数 - 在典个数 - 已完成典试个数");
		}
		Calendar udate = Calendar.getInstance();
		if (reportDate != null && !reportDate.isEmpty()) {
			try {
				udate = DateUtil.parseDate(reportDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ProduceDianshi pd = new ProduceDianshi();
		pd.setOrder(o);
		pd.setReportDate(udate);
		pd.setStatus(ProduceDianshi.ON_PROCESS);
		pd.setTestNum(num);
		pd.setWorker(w);
		pd.setContent(content);
		;
		this.pdDao.save(pd);
	}

	@Override
	public List<ProduceDianshiModel> getDianshiReportByWorker(Integer orderId)
			throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		Order o = this.orderDao.get(orderId);
		if (o == null)
			throw new ServiceException("该订单不存在");
		User u = this.userDao.get(v.getUid());
		Worker w = u.getWorker();
		if (w == null)
			throw new ServiceException("只有工人才可以进行查看");
		int wId = w.getId();
		Set<ProduceDianshi> pts = o.getProduceDianshi();
		List<ProduceDianshiModel> ptms = new ArrayList<ProduceDianshiModel>();
		for (ProduceDianshi pt : pts) {
			if (pt.getWorker().getId() == wId) {
				ptms.add(new ProduceDianshiModel(pt));
			}
		}
		return ptms;
	}

	@Override
	public void confirmProduceDianshi(Integer id, String reportDate) throws ServiceException {
		if (id == null)
			throw new ServiceException("请选择一个正确的典试报工");
		ProduceDianshi pt = this.pdDao.get(id);
		if (pt == null)
			throw new ServiceException("请选择一个正确的典试报工");
		Calendar udate = Calendar.getInstance();
		if (reportDate != null && !reportDate.isEmpty()) {
			try {
				udate = DateUtil.parseDate(reportDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		pt.setStatus(ProduceTest.FINISHED);
		pt.setConfirmDate(udate);
		this.pdDao.update(pt);
	}

	@Override
	public void reportOther(Integer orderId, String content, String reportDate)
			throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		Order o = this.orderDao.get(orderId);
		if (o == null)
			throw new ServiceException("该订单不存在");
		User u = this.userDao.get(v.getUid());
		Worker w = u.getWorker();
		if (w == null)
			throw new ServiceException("只有工人才可以进行其他报工");
		Calendar udate = Calendar.getInstance();
		if (reportDate != null && !reportDate.isEmpty()) {
			try {
				udate = DateUtil.parseDate(reportDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		ProduceOther po = new ProduceOther();
		po.setContent(content);
		po.setOrder(o);
		po.setReportDate(udate);
		po.setWorker(w);
		this.poDao.save(po);
	}

	@Override
	public List<ProduceOtherModel> getOtherReportByWorker(Integer orderId)
			throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		Order o = this.orderDao.get(orderId);
		if (o == null)
			throw new ServiceException("该订单不存在");
		User u = this.userDao.get(v.getUid());
		Worker w = u.getWorker();
		if (w == null)
			throw new ServiceException("只有工人才可以进行查看");
		int wId = w.getId();
		Set<ProduceOther> pts = o.getProduceother();
		List<ProduceOtherModel> ptms = new ArrayList<ProduceOtherModel>();
		for (ProduceOther pt : pts) {
			if (pt.getWorker().getId() == wId) {
				ptms.add(new ProduceOtherModel(pt));
			}
		}
		return ptms;
	}

	@Override
	public Map<String, Object> getReportByDate(String beginStr, String endStr)
			throws ServiceException {
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			Date begin = DateUtil.parseDate(beginStr).getTime();
			Date end = DateUtil.parseDate(endStr).getTime();
			// 记录所有的订单数
			Set<Integer> orderIds = new HashSet<Integer>();
			// 装配报工
			String hql = "from " + ProduceAssembling.class.getSimpleName()
					+ " as pro where pro.udate >= ? and pro.udate <= ?";
			List<ProduceAssembling> prs = this.assemblingDao.list(hql, -1, -1,
					begin, end);
			int assemblingNum = 0;
			Set<Integer> tempOrderIds = new HashSet<Integer>();
			for (ProduceAssembling pa : prs) {
				// assemblingNum += pa.getReportNum();
				Integer oid = pa.getOrderProcess().getOrder().getId();
				if (!orderIds.contains(oid)) {
					orderIds.add(oid);
				}
				if (!tempOrderIds.contains(oid)) {
					tempOrderIds.add(oid);
				}
			}
			for (Integer oid : tempOrderIds) {
				Order o = this.orderDao.get(oid);
				assemblingNum += o.getAssemblingNum();
			}
			hql = "from " + ProduceAssembled.class.getSimpleName()
					+ " as pa where pa.reportDate >= ? and pa.reportDate <= ?";
			List<ProduceAssembled> pas = this.assembledDao.list(hql, -1, -1,
					begin, end);
			int assembledNum = 0;
			for (ProduceAssembled pa : pas) {
				assembledNum += pa.getAssembleNum();
				Integer oid = pa.getOrder().getId();
				if (!orderIds.contains(oid)) {
					orderIds.add(oid);
				}
			}
			// 试验报工
			hql = "from "
					+ ProduceTest.class.getSimpleName()
					+ " as pt where pt.reportDate >= ? and pt.reportDate <= ? and pt.status = ?";
			List<ProduceTest> pts = this.ptDao.list(hql, -1, -1, begin, end,
					ProduceTest.ON_PROCESS);
			int testingNum = 0;
			for (ProduceTest pt : pts) {
				testingNum += pt.getTestNum();
				Integer oid = pt.getOrder().getId();
				if (!orderIds.contains(oid)) {
					orderIds.add(oid);
				}
			}
			hql = "from "
					+ ProduceTest.class.getSimpleName()
					+ " as pt where pt.confirmDate >= ? and pt.confirmDate <= ? and pt.status = ?";
			List<ProduceTest> pts2 = this.ptDao.list(hql, -1, -1, begin, end,
					ProduceTest.FINISHED);
			int testedNum = 0;
			for (ProduceTest pt : pts2) {
				testedNum += pt.getTestNum();
				Integer oid = pt.getOrder().getId();
				if (!orderIds.contains(oid)) {
					orderIds.add(oid);
				}
			}
			// 典试报工
			hql = "from "
					+ ProduceDianshi.class.getSimpleName()
					+ " as pd where pd.reportDate >= ? and pd.reportDate <= ? and pd.status = ?";
			int dianshiingNum = 0;
			List<ProduceDianshi> pds = this.pdDao.list(hql, -1, -1, begin, end,
					ProduceDianshi.ON_PROCESS);
			for (ProduceDianshi pd : pds) {
				dianshiingNum += pd.getTestNum();
				Integer oid = pd.getOrder().getId();
				if (!orderIds.contains(oid)) {
					orderIds.add(oid);
				}
			}
			hql = "from "
					+ ProduceDianshi.class.getSimpleName()
					+ " as pd where pd.confirmDate >= ? and pd.confirmDate <= ? and pd.status = ?";
			int dianshiedNum = 0;
			List<ProduceDianshi> pds2 = this.pdDao.list(hql, -1, -1, begin,
					end, ProduceDianshi.FINISHED);
			for (ProduceDianshi pd : pds2) {
				dianshiedNum += pd.getTestNum();
				Integer oid = pd.getOrder().getId();
				if (!orderIds.contains(oid)) {
					orderIds.add(oid);
				}
			}
			// 计算所有订单的总投产数量，总典试数量
			int totalProduceNum = 0;
			int totalDianshiNum = 0;
			for (Integer oid : orderIds) {
				Order o = this.orderDao.get(oid);
				totalProduceNum += o.getProduceNum();
				totalDianshiNum += o.getTestNum();
			}
			res.put("totalProduceNum", totalProduceNum);
			res.put("totalDianshiNum", totalDianshiNum);
			res.put("assemblingNum", assemblingNum);
			res.put("assembledNum", assembledNum);
			res.put("testingNum", testingNum);
			res.put("testedNum", testedNum);
			res.put("dianshiingNum", dianshiingNum);
			res.put("dianshiedNum", dianshiedNum);
			return res;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public Integer getOrderIdByCode(String code) throws ServiceException {
		Order order = this.orderService.getOrderByCode(code);
		return order.getId();
	}

	@Override
	public GridModel<OrderModel> getOrder(Integer id) throws ServiceException {
		Order order = this.orderDao.get(id);
		if (order == null) {
			throw new ServiceException("不存在该订单");
		}
		checkProduceReportAuth(order);
		GridModel<OrderModel> data = new GridModel<OrderModel>();
		List<OrderModel> items = new ArrayList<OrderModel>();
		items.add(new OrderModel(order));
		data.setItems(items);
		return data;
	}

	@Override
	public Map<String, Object> produceReport(WorkerKpiSearchForm form)
			throws ServiceException {
		Integer id = form.getOrderId();
		Integer workerId = form.getWorkerId();
		Calendar begin = null;
		Calendar end = null;
		try {
			if (form.getDateBegin() != null && !form.getDateBegin().isEmpty())
				begin = DateUtil.parseDate(form.getDateBegin());
			if (form.getDateEnd() != null && !form.getDateEnd().isEmpty())
				end = DateUtil.parseDate(form.getDateEnd());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Order order = this.orderDao.get(id);
		if (order == null)
			throw new ServiceException("该订单不存在");
		Worker w = null;
		if (workerId != null) {
			w = this.workerDao.get(workerId);
		}
		OrderModel orderModel = new OrderModel(order, w, begin, end);
		// 填充每个工序组的最早开始时间和最迟完成时间
		List<OrderProcessModel> ops = orderModel.getOrderProcesses();
		for (OrderProcessModel op : ops) {
			String groupName = op.getGroupName() + "";
			Calendar opbegin = this.srService.getEarlistBeginDateTime(
					groupName, id);
			op.setBeginDate(DateUtil.getDate(opbegin, "yyyy-MM-dd"));
			Calendar opend = this.srService.getLastEndDateTime(groupName, id);
			op.setEndDate(DateUtil.getDate(opend, "yyyy-MM-dd"));
		}

		// String productCode = order.getProductCode();
		// PtProductCode model = new PtProductCode(productCode);
		// PtProductCode ptProductCode = this.ptProductCodeService.find(model);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("order", JSON.toJSONString(orderModel));

		// 获取该订单所有的装配报工详情
		Set<OrderProcess> opps = order.getOps();
		List<ProduceAssembingModel> pams = new ArrayList<ProduceAssembingModel>();
		for (OrderProcess op : opps) {
			Set<ProduceAssembling> pas = op.getPrs();
			for (ProduceAssembling pa : pas) {
				if (workerId != null
						&& !pa.getWorker().getId().equals(workerId)) {
					continue;
				}
				if (end != null && pa.getUdate().after(end)) {
					// 如果是报工时间之后的，忽略该报工
					continue;
				}
				if (begin != null && pa.getUdate().before(begin)) {
					// 如果报工时间是查询时间之前的，忽略该报工
					continue;
				}
				pams.add(new ProduceAssembingModel(pa));
			}
		}
		result.put("zhuangpei", JSON.toJSONString(pams));

		// 获取该订单的试验报工详情
		List<ProduceTestModel> ptms = new ArrayList<ProduceTestModel>();
		Set<ProduceTest> pts = order.getProduceTest();
		for (ProduceTest pt : pts) {
			if (workerId != null && !pt.getWorker().getId().equals(workerId)) {
				continue;
			}
			if (end != null && pt.getReportDate().after(end)) {
				// 如果是报工时间之后的，忽略该报工
				continue;
			}
			if (begin != null && pt.getReportDate().before(begin)) {
				// 如果报工时间是查询时间之前的，忽略该报工
				continue;
			}
			ptms.add(new ProduceTestModel(pt));
		}
		result.put("shiyan", JSON.toJSONString(ptms));
		// 典试报工
		List<ProduceDianshiModel> pdms = new ArrayList<ProduceDianshiModel>();
		Set<ProduceDianshi> pds = order.getProduceDianshi();
		for (ProduceDianshi pd : pds) {
			if (workerId != null && !pd.getWorker().getId().equals(workerId)) {
				continue;
			}
			if (end != null && pd.getReportDate().after(end)) {
				// 如果是报工时间之后的，忽略该报工
				continue;
			}
			if (begin != null && pd.getReportDate().before(begin)) {
				// 如果报工时间是查询时间之前的，忽略该报工
				continue;
			}
			pdms.add(new ProduceDianshiModel(pd));
		}
		result.put("dianshi", JSON.toJSONString(pdms));
		// 其他报工
		List<ProduceOtherModel> poms = new ArrayList<ProduceOtherModel>();
		Set<ProduceOther> pos = order.getProduceother();
		for (ProduceOther po : pos) {
			if (workerId != null && !po.getWorker().getId().equals(workerId)) {
				continue;
			}
			if (end != null && po.getReportDate().after(end)) {
				// 如果是报工时间之后的，忽略该报工
				continue;
			}
			if (begin != null && po.getReportDate().before(begin)) {
				// 如果报工时间是查询时间之前的，忽略该报工
				continue;
			}
			poms.add(new ProduceOtherModel(po));
		}
		result.put("qita", JSON.toJSONString(poms));
		// result.put("ptid", ptProductCode.getPt().getId());
		result.put("ptid", -1);
		return result;
	}

	@Override
	public GridModel<ProduceOtherModel> listProduceOther(
			ProduceOtherSearchForm form) throws ServiceException {
		ProduceOther model = new ProduceOther();
		if (form.getOrderNo() != null && !form.getOrderNo().isEmpty()) {
			Order order = new Order();
			order.setNo(form.getOrderNo());
			model.setOrder(order);
		}
		if (form.getWorkerId() != null) {
			Worker worker = new Worker(form.getWorkerId());
			model.setWorker(worker);
		}
		GridModel<ProduceOtherModel> grid = new GridModel<ProduceOtherModel>();
		Integer count = this.poDao.count(model);
    	List<ProduceOther> items = this.poDao.listAll(model, form.getPage(), form.getLimit());
		grid.setTotalNum(count);
		List<ProduceOtherModel> itModel = new ArrayList<ProduceOtherModel>();
		for (ProduceOther m : items) {
			itModel.add(new ProduceOtherModel(m));
		}
		grid.setItems(itModel);
		return grid;

	}

}
