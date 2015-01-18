package cn.edu.ustb.sem.order.dao;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.order.entity.OrderMaterial;

public interface OrderMaterialDao extends BaseDao<OrderMaterial, Long> {
	public void deleteOrderMaterial(Integer orderId);
}
