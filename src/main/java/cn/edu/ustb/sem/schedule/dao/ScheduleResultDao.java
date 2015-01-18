package cn.edu.ustb.sem.schedule.dao;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;

public interface ScheduleResultDao extends BaseDao<ScheduleResult, Integer> {
	public List<ScheduleResult> findOrderSchedultResult(Integer oid);
	public void updateSchedultResultStatus(ScheduleResult model);
	public List<ScheduleResult> getSRByDate(Calendar beg, Calendar end);
	//根据gupid和orderid删除已经排产的结果
	public void deleteByGupidsAndOrderId(Set<Integer> gupids, Integer orderId);
	//排产计划调整，根据orderid来删除已有的排产结果
	public List<ScheduleResult> findOrderSchedultResult(List<Integer> orderIds);
	public List<ScheduleResult> findByOrderAndGup(Integer oid, Integer gupId);
}
