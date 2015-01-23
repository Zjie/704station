package cn.edu.ustb.sem.assign.service.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.assign.dao.AssignDao;
import cn.edu.ustb.sem.assign.dao.DispatchMaterialDao;
import cn.edu.ustb.sem.assign.entity.Assign;
import cn.edu.ustb.sem.assign.entity.DispatchMaterial;
import cn.edu.ustb.sem.assign.service.AssignService;
import cn.edu.ustb.sem.assign.web.model.AssignForSaveModel;
import cn.edu.ustb.sem.assign.web.model.AssignModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.cons.YesOrNo;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.dao.OrderDao;
import cn.edu.ustb.sem.order.dao.OrderMaterialDao;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderMaterial;
import cn.edu.ustb.sem.order.entity.OrderStatus;
import cn.edu.ustb.sem.order.service.OrderService;

@Service("assignService")
public class AssignServiceImpl extends
		BaseServiceImpl<Assign, AssignModel, Integer> implements AssignService {
	@Autowired
	private DispatchMaterialDao dmDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderMaterialDao orderMaterialDao;
	@Autowired
	private OrderDao orderDao;
	private AssignDao assignDao;

	@Autowired
	@Qualifier("assignDao")
	@Override
	public void setBaseDao(BaseDao<Assign, Integer> baseDao) {
		this.baseDao = baseDao;
		this.assignDao = (AssignDao) baseDao;
	}

	@Override
	public void toAssign(AssignForSaveModel afsm) throws ServiceException {
		if (afsm == null) {
			throw new ServiceException("缺失参数");
		}
		List<AssignModel> ams = afsm.getAssigns();
		Order o = null;
		boolean isMaterialChecked = false;
		Map<Long, Integer> uploadAssign = new HashMap<Long, Integer>();
		Calendar reportDate = Calendar.getInstance();
		if (afsm.getReportDate() != null && !afsm.getReportDate().isEmpty()) {
			try {
				reportDate = DateUtil.parseDate(afsm.getReportDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		for (AssignModel model : ams) {
			
			OrderMaterial om = this.orderMaterialDao.get(model.getOmid());
			if (om == null) {
				continue;
			}
			o = om.getOrder();
			if (!isMaterialChecked) {
				o = om.getOrder();
				if (o.getMaterialIsChecked() != null && o.getMaterialIsChecked() == Order.MATERIAL_IS_CHECKED) {
					isMaterialChecked = true;
				} else {
					throw new ServiceException("该订单的物料信息还没被物料员确认，无法进行物料配套");
				}
			}
			Assign assign = new Assign();
			assign.setOrderMaterial(om);
			assign.setMatingNum(model.getMatingNum());
			Visitor visitor = (Visitor) SecurityContextHolder.getContext()
					.getAuthentication();
			assign.setUpdater(visitor.getUser());
			assign.setUdate(reportDate);
			assignDao.save(assign);
			uploadAssign.put(om.getId(), model.getMatingNum());
			
			// 修改订单的物料配套是否配齐状态
//			if (afsm.getTotalSingleNum() == afsm.getTotalAssign()) {
//				Order order = this.orderService.get(afsm.getOrderId());
//				order.setIsAssigned(YesOrNo.YES.getIndex());
//				order.setStatus(OrderStatus.ASSIGNED.getIndex());
//				order.setMaterialsCompleteness(Calendar.getInstance());
//				this.orderService.update(order);
//			} else {
//				
//			}
		}
		//配套完后更新订单的状态
		Set<OrderMaterial> oms = o.getOms();
		byte status = Order.ASSIGN_STATUS_NO;
		boolean isFinished = true;
		for (OrderMaterial om : oms) {
//			byte omstatus = om.getStatus();
			int assigned = 0;
			if (uploadAssign.containsKey(om.getId())) {
				assigned = uploadAssign.get(om.getId());
			} else if ((om.getAs() == null || om.getAs().size() ==0) && om.getSingleNum() != 0) {
				//如果有物料没有进行配套，则
				isFinished = false;
				continue;
			}
			int alreadyAssingNum = om.getAssignedNum();
			
			if (alreadyAssingNum + assigned < om.getSingleNum() * o.getProduceNum()) {
				//只要有一个为配套中，则订单的状态为配套中
				status = Order.ASSIGN_STATUS_PROCESS;
				isFinished = false;
				break;
			}
		}
		Order oo = this.orderService.get(o.getId());
		if (isFinished) {
			status = Order.ASSIGN_STATUS_FINISHED;
			//更新订单信息
			oo.setStatus(new Byte(OrderStatus.ready + ""));
		}
		oo.setAssignStatus(status);
		this.orderService.update(oo);
	}
	/**
	 * 派工单领料
	 * @param code ${order.no}-${order.productCode}
	 * @throws ServiceException
	 */
	@Override
	public void doDispatch(String code) throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		String[] data = code.split("-");
		String no = data[0];
		if (no == null || no.isEmpty()) throw new ServiceException("参数出错");
		Order orderModel = new Order();
		orderModel.setNo(no);
		Order order = this.orderService.find(orderModel);
		//先查看此订单是否被领料过
		DispatchMaterial model = new DispatchMaterial();
		model.setOrder(new Order(order.getId()));
		DispatchMaterial dm = this.dmDao.find(model);
		if (dm != null) throw new ServiceException("该订单在" + DateUtil.getDateTime(dm.getUdate()) + "已经被确认领料过");
		DispatchMaterial toBeInsert = new DispatchMaterial();
		toBeInsert.setOrder(new Order(order.getId()));
		toBeInsert.setUdate(Calendar.getInstance());
		toBeInsert.setUpdater(v.getUser());
		this.dmDao.save(toBeInsert);
		//更新order的领料状态
		order.setIsDispatchMaterial(YesOrNo.YES.getIndex());
	}
}
