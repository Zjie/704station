package cn.edu.ustb.sem.order.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderSearchForm;

public interface OrderService extends BaseService<Order, OrderModel, Integer> {
	/**
	 * 
	 * @param hssfWorkbook
	 * @return 返回出错的行数
	 * @throws ServiceException
	 */
	public boolean saveRow(Row row, Map<String, Order> excelExistOrder) throws ServiceException;
	public GridModel<OrderModel> list(OrderSearchForm form, ItemModelHelper<Order, OrderModel> helper) throws ServiceException;
	public GridModel<OrderModel> listCanAdjustOrder(OrderSearchForm form) throws ServiceException;
	public List<Order> getAutoScheduleOrder(int pn, int pageSize) throws ServiceException;
	public int getAutoScheduleOrderCount();

	/**
	 * 查找可以进行自动排产的订单
	 * @param orderIds TODO
	 * @return
	 */
	public List<Order> findCanAutoScheduleOrder(List<Integer> orderIds);
	
	public void saveOrUpdate(OrderModel order) throws ServiceException;
	
	/**
	 * 通过订单的条形码查找订单
	 * @param code 条形码
	 * @return
	 * @throws ServiceException
	 */
	public Order getOrderByCode(String code) throws ServiceException;
	
	public List<OrderModel> listScheduledOrders();
	
	public void confirmOrderProcessInfo(Integer orderId) throws ServiceException;
	
	public void deleteOrderMaterial(Integer omid) throws ServiceException;
	
	public void deleteOrderProcess(Integer opid) throws ServiceException;
	
	public void confirmOrderMaterialInfo(Integer orderId) throws ServiceException;
	
	public GridModel<OrderModel> listForceScheduleOrder() throws ServiceException;
}
