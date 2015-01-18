package cn.edu.ustb.sem.assign.service;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.order.web.model.OrderModel;


public interface AssignModuleService {
	public AssignService getAssignService();
	public OrderModel getAssignOrderModel(Integer orderId) throws ServiceException;
}
