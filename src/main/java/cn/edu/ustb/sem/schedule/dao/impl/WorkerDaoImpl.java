package cn.edu.ustb.sem.schedule.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.schedule.dao.WorkerDao;
import cn.edu.ustb.sem.schedule.entity.Worker;
@Repository("workerDao")
public class WorkerDaoImpl extends BaseDaoImpl<Worker, Integer> implements WorkerDao {
	@Override
	public Map<String, List<Worker>> findAllUnit() {
		List<Worker> allWorker = this.listAll();
		Map<String, List<Worker>> result = new HashMap<String, List<Worker>>();
		for (Worker w : allWorker) {
			String unit = w.getUnit();
			List<Worker> ws = result.get(unit);
			if (ws != null) {
				ws.add(w);
			} else {
				ws = new ArrayList<Worker>();
				ws.add(w);
				result.put(unit, ws);
			}
		}
		return result;
	}

	@Override
	public String getRealName(Integer userId) {
		String hql = "from Worker w where w.user.id = ?";
		Worker w = this.find(hql, userId);
		if (w == null) return "";
		return w.getRealName();
	}

}
