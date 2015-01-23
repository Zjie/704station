package cn.edu.ustb.sem.order.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.pagination.PageUtil;
import cn.edu.ustb.sem.order.dao.OrderDao;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderStatus;
@Repository("orderDao")
public class OrderDaoImpl extends BaseDaoImpl<Order, Integer> implements OrderDao {

	public List<Order> getAutoScheduleOrder(int pn, int pageSize){
		//可自动排产的订单
		//1.物料齐套，物料信息被确认，工序信息被确认
		//2.强制排产
		String hql = "from " + this.tableName + " o where (o.status = " + OrderStatus.ready + " "
				+ "and o.processIsCheck = " + Order.PROCESS_IS_CHECKED + " "
				+ "and o.materialIsChecked = " + Order.MATERIAL_IS_CHECKED + ") "
				+ "or o.status = " + OrderStatus.forced;
		return list(hql, pn, pageSize);
	}
	
	public List<Order> listForceScheduleOrder() {
		String hql = "from " + this.tableName + " o where (o.status = " + OrderStatus.initial + " "
				+ "or o.processIsCheck = " + Order.PROCESS_IS_NOT_CHECKED + " "
				+ "or o.materialIsChecked = " + Order.PROCESS_IS_NOT_CHECKED + ") ";
				//+ "or o.status = " + OrderStatus.forced;
		return list(hql, -1, -1);
	}
	
	public int getAutoScheduleOrderCount(){
		String hql = "from " + this.tableName + " o where (o.status = " + OrderStatus.ready + " "
				+ "and o.processIsCheck = " + Order.PROCESS_IS_CHECKED + " "
				+ "and o.materialIsChecked = " + Order.MATERIAL_IS_CHECKED + ") "
				+ "or o.status = " + OrderStatus.forced;
		return list(hql, -1, -1).size();
	}

	@Override
	public  Page<Order> listCanAdjustOrder(int pn, int pageSize) {
		String hql = "from " + this.tableName + " o where status in (" + OrderStatus.initial + ","
			+ OrderStatus.ready + "," + OrderStatus.forced + ")";
		String countHql = "select count(*) from " + this.tableName + " o where status in (" + OrderStatus.initial + ","
				+ OrderStatus.ready + "," + OrderStatus.forced + ")";
		return PageUtil.getPage(this.count(countHql), pn, this.list(hql, pn, pageSize), pageSize);
	}

	@Override
	public List<Order> listScheduledOrder() {
		String hql = "from " + this.tableName + " o where status in ("+ OrderStatus.scheduled + ","
				+ OrderStatus.waixie + "," + OrderStatus.waiexieEnd + ")";
		return this.list(hql, -1, -1);
	}
}
