package cn.edu.ustb.sem.assign.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.assign.service.AssignModuleService;
import cn.edu.ustb.sem.assign.service.AssignService;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;

@Service("assignModuleService")
public class AssignModuleServiceImpl implements AssignModuleService{

	@Autowired
	private AssignService assignService;
	@Autowired
	private OrderService os;
	@Override
	public AssignService getAssignService() {
		return assignService;
	}
	@Override
	public OrderModel getAssignOrderModel(Integer orderId) throws ServiceException {
		Order order = this.os.get(orderId);
		if (order.getMaterialIsChecked() == null || order.getMaterialIsChecked() == Order.MATERIAL_IS_NOT_CHECKED) {
			throw new ServiceException("该订单的物料信息还未被确认，请先确认");
		}
		return new OrderModel(order);
	}
}
