package cn.edu.ustb.sem.order.dao;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.order.entity.OrderProcess;

public interface OrderProcessDao extends BaseDao<OrderProcess, Long> {
	public void deleteOrderProcess(Integer orderId);
}
