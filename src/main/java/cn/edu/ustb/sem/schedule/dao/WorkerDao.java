package cn.edu.ustb.sem.schedule.dao;

import java.util.List;
import java.util.Map;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.schedule.entity.Worker;

public interface WorkerDao extends BaseDao<Worker, Integer> {
	public Map<String, List<Worker>> findAllUnit();
	public String getRealName(Integer userId);
}
