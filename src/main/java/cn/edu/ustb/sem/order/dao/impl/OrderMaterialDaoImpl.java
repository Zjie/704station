package cn.edu.ustb.sem.order.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.order.dao.OrderMaterialDao;
import cn.edu.ustb.sem.order.entity.OrderMaterial;
@Repository("orderMaterialDao")
public class OrderMaterialDaoImpl extends BaseDaoImpl<OrderMaterial, Long> implements OrderMaterialDao {

	@Override
	public void deleteOrderMaterial(Integer orderId) {
		String hql = "delete from OrderMaterial om where om.order.id = ?";
		this.execute(hql, orderId);
	}

}
