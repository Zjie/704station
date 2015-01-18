package cn.edu.ustb.sem.schedule.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.schedule.web.model.GanttModel;

public interface ScheduleService {
	//自动排产
	public void autoSche(int totalDay, List<Integer> orderIds, Map<String, Object> scheResult) throws ServiceException;
	//动态调整
	public GanttModel adjustPlan(String orderIds, HttpServletRequest request) throws ServiceException;
	/**
	 * @author caiwenming
	 * 强制添加至排产
	 * @param ids
	 * @throws ServiceException
	 */
	public void addToSchedule(String ids) throws ServiceException;
	
	/**
	 * @author caiwenming
	 * 获取可排产订单
	 * @return
	 * @throws ServiceException
	 */
	public GridModel<OrderModel> getAutoScheduleOrder() throws ServiceException;
	
	/**
	 * @author caiwenming
	 * 返回用户选择的排产列表
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	public GridModel<OrderModel> selectedGrid(String ids) throws ServiceException;
	/**
	 * 将排产结果保存到数据库中
	 * @param scheResult
	 * @param userId
	 * @throws ServiceException
	 */
	public void saveScheduleResult(Map<String, Object> scheResult, Integer userId) throws ServiceException;
	/**
	 * 外协订单重启
	 * @param orderId 订单id
	 * @param userId 重启操作执行者的id
	 * @throws ServiceException
	 */
	public void waixieRestart(Integer orderId, Integer userId) throws ServiceException;
	/**
	 * 确认该停工
	 * @param ptid
	 * @throws ServiceException
	 */
	public void tinggongCommit(Integer ptid) throws ServiceException;
	/**
	 * 停工重启
	 * @param ptid
	 * @throws ServiceException
	 */
	public void tinggongRestart(Integer ptid) throws ServiceException;
	/**
	 * 查找指定时间之后排产的计划，让用户能够重新调整排产计划
	 * @param date yyyy-MM-dd
	 * @param addIds 额外的订单id
	 * @return
	 * @throws ServiceException
	 */
	public GridModel<OrderModel> getScheduleOrderBeforeDate(String date, String addIds) throws ServiceException;
	public void cancelAdjust(HttpServletRequest request) throws ServiceException;
	
	public List<String> listAllUnit() throws ServiceException;
	
	public void dispatchUnit(Integer orderId, String unit, Integer userId) throws ServiceException;
}
