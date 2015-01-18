package cn.edu.ustb.sem.order.dao;

import java.util.List;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.order.entity.Order;

public interface OrderDao extends BaseDao<Order, Integer> {
	public List<Order> getAutoScheduleOrder(int pn, int pageSize);
	public int getAutoScheduleOrderCount();
	public  Page<Order> listCanAdjustOrder(int pn, int pageSize);
	//列出已经排产的订单
	public List<Order> listScheduledOrder();
	public List<Order> listForceScheduleOrder();
}
