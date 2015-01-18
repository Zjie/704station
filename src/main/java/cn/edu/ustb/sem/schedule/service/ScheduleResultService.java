package cn.edu.ustb.sem.schedule.service;

import java.util.Calendar;
import java.util.List;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderProcessModel;
import cn.edu.ustb.sem.schedule.web.model.GanttModel;

public interface ScheduleResultService {
	/**
	 * 获取最新排产结果
	 * @param successOrder
	 * @return
	 * @throws ServiceException
	 */
	public GanttModel getNewlyScheduleResult(List<Order> successOrder) throws ServiceException;
	/**
	 * 获取外协出去的订单
	 * @return
	 * @throws ServiceException
	 */
	public List<OrderModel> getWaixieOrder() throws ServiceException;
	/**
	 * 按照时间段获取排产甘特图
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 * @throws ServiceException
	 */
	public GanttModel getScheduledResultByDate(String dateBegin, String dateEnd) throws ServiceException;
	/**
	 * 获取该工序组最早的开始时间
	 * @param groupName
	 * @param oid TODO
	 * @return
	 * @throws ServiceException
	 */
	public Calendar getEarlistBeginDateTime(String groupName, Integer oid) throws ServiceException;
	/**
	 * 获取该工序组最晚的结束时间
	 * @param groupName
	 * @param oid TODO
	 * @return
	 * @throws ServiceException
	 */
	public Calendar getLastEndDateTime(String groupName, Integer oid) throws ServiceException;
	/**
	 * 将工序组的排产信息填充到工序组中，包括计划开始时间，计划完成时间，当前状态
	 * @param op
	 * @param orderId TODO
	 * @throws ServiceException
	 */
	public void populateScheduleInfo(OrderProcessModel op, Integer orderId) throws ServiceException;
}
