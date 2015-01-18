package cn.edu.ustb.sem.schedule.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.schedule.dao.WorkerTimelineDao;
import cn.edu.ustb.sem.schedule.entity.Worker;
import cn.edu.ustb.sem.schedule.entity.WorkerTimeLine;
@Repository("workerTimelineDao")
public class WorkerTimelineDaoImpl extends BaseDaoImpl<WorkerTimeLine, Integer> implements WorkerTimelineDao {
	@Override
	public List<WorkerTimeLine> listAll(WorkerTimeLine model, int begin, int end) {
		List<WorkerTimeLine> wtls = this.listAll();
		List<WorkerTimeLine> result = new ArrayList<WorkerTimeLine>();
		for (WorkerTimeLine wtl : wtls) {
			//过滤掉哪些被冻结的账户
			Worker w = wtl.getWorker();
			if (w.getIsFreezed() == Worker.IS_FREEZED) continue;
			result.add(wtl);
		}
		return result;
	}
}
