package cn.edu.ustb.sem.produce.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.order.dao.OrderProcessDao;
import cn.edu.ustb.sem.order.entity.OrderProcess;
import cn.edu.ustb.sem.produce.dao.ProduceAssemblingDao;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;

@Repository("produceAssemblingDao")
public class ProduceAssemblingDaoImpl extends BaseDaoImpl<ProduceAssembling, Integer> implements ProduceAssemblingDao{
	@Autowired
	private OrderProcessDao opDao;
	@Override
	public List<ProduceAssembling> getPrByOrderId(Integer orderId) {
		String hql = "from " + OrderProcess.class.getSimpleName() + " as op where op.order.id = ?";
		List<OrderProcess> ops = this.opDao.list(hql, -1, -1, orderId);
		StringBuilder sb = new StringBuilder();
		String idStr = "";
		for (OrderProcess op : ops) {
			sb.append(op.getId()).append(",");
		}
		idStr = sb.toString();
		if (ops.size() > 0) {
			idStr = idStr.substring(0, idStr.length() - 1);
		}
		hql = "from " + this.tableName + " as pr where pr.orderProcess.id in (" + idStr + ")";
		return this.list(hql, -1, -1);
	}

}
