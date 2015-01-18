package cn.edu.ustb.sem.assign.service;

import cn.edu.ustb.sem.assign.entity.Assign;
import cn.edu.ustb.sem.assign.web.model.AssignForSaveModel;
import cn.edu.ustb.sem.assign.web.model.AssignModel;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;

public interface AssignService extends BaseService<Assign, AssignModel, Integer>{

	/**
	 * @author caiwenming
	 * 给订单配套物料
	 * @param afsm
	 * @throws ServiceException
	 */
	public void toAssign(AssignForSaveModel afsm) throws ServiceException;
	/**
	 * 派工单领料
	 * @param code ${order.no}-${order.productCode}
	 * @throws ServiceException
	 */
	public void doDispatch(String code) throws ServiceException;
}
