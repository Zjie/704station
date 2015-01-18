package cn.edu.ustb.sem.schedule.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.schedule.dao.ScheduleResultDao;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
@Repository("scheduleResultDao")
public class ScheduleResultDaoImpl extends BaseDaoImpl<ScheduleResult, Integer> implements ScheduleResultDao {
	
	@Override
	public void updateSchedultResultStatus(ScheduleResult model) {
		String hql = "update " + this.tableName + " sr set sr.status = ? where sr.order.id=? and sr.gup.id=?";
		this.execute(hql, ScheduleResult.PRINTED, model.getOrder().getId(), model.getGup().getId());
	}

	@Override
	public List<ScheduleResult> findOrderSchedultResult(Integer oid) {
		String hql = "from " + this.tableName + " sr where sr.order.id = ? and sr.status in (?, ?)";
		return this.list(hql, -1, -1, oid, ScheduleResult.COMMIT, ScheduleResult.PRINTED);
	}

	@Override
	public List<ScheduleResult> getSRByDate(Calendar beg, Calendar end) {
		if (end != null) {
			String hql = "from " + this.tableName + " sr where sr.start >= ? and sr.start <= ?";
			return this.list(hql, -1, -1, beg.getTime(), end.getTime());
		} else {
			String hql = "from " + this.tableName + " sr where sr.start >= ?";
			return this.list(hql, -1, -1, beg.getTime());
		}
	}

	@Override
	public void deleteByGupidsAndOrderId(Set<Integer> gupids, Integer orderId) {
		//只能去掉在排产区的工序组，也就是status=1
		//一个一个的更新吧
		String hql = "from " + this.tableName + " sr where sr.order.id = ? and sr.status = ?";
		List<ScheduleResult> srs = this.list(hql, -1, -1, orderId, ScheduleResult.COMMIT);
		for (ScheduleResult sr : srs) {
			Integer gupid = sr.getGup().getId();
			if (!gupids.contains(gupid)) continue;
			sr.setStatus(ScheduleResult.TINGGONG);
			this.merge(sr);
		}
	}

	@Override
	public List<ScheduleResult> findOrderSchedultResult(List<Integer> orderIds) {
		List<ScheduleResult> data = new ArrayList<ScheduleResult>();
		if (orderIds == null || orderIds.isEmpty()) return data;
		String ids = "";
		for (Integer id : orderIds) {
			ids += id.toString() + ",";
		}
		ids = ids.substring(0, ids.length() - 1);
		String hql = "from " + this.tableName + " as sr where sr.order.id in (" + ids + ")";
		List<ScheduleResult> srs = this.list(hql, -1, -1);
		for (ScheduleResult sr : srs) {
			data.add(sr);
		}
		return data;
	}
	
	@Override
	public List<ScheduleResult> findByOrderAndGup(Integer oid, Integer gupId) {
		String hql = "from " + this.tableName + " as sr where sr.gup.id = ? and sr.order.id = ?";
		return this.list(hql, -1, -1, gupId, oid);
	}

}
