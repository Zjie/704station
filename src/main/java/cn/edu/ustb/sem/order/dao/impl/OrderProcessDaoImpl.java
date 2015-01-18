package cn.edu.ustb.sem.order.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.order.dao.OrderProcessDao;
import cn.edu.ustb.sem.order.entity.OrderProcess;
@Repository("orderProcessDao")
public class OrderProcessDaoImpl extends BaseDaoImpl<OrderProcess, Long> implements OrderProcessDao {
	
	@Override
	public void deleteOrderProcess(Integer orderId) {
		String hql = "delete from OrderProcess op where op.order.id=?";
		this.execute(hql, orderId);
	}

}
