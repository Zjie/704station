package cn.edu.ustb.sem.schedule.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.ustb.sem.core.exception.ServiceException;
/**
 * 辅助类，用于在找出最早能进行生产的工人，一个工序组对应到一个timeLineHelper上
 * 订单的工序组不能跨生产单元，也就是如果一个订单的所有工序组都会在一个生产单元里面进行生产
 * @author zhoujie
 *
 */
public class TimeLineHelper {
	//               生产单元  该生产单元对应的所有工人
	private Map<String, List<WorkerTimeLine>> timelines;
	/**
	 * 能完成某个工序组的所有员工
	 * @param wtl
	 */
	public TimeLineHelper(List<WorkerTimeLine> wtl) {
		timelines = new HashMap<String, List<WorkerTimeLine>>();
		for (WorkerTimeLine tl : wtl) {
			Worker w = tl.getWorker();
			String unit = w.getUnit();
			if (timelines.containsKey(unit)) {
				timelines.get(unit).add(tl);
			} else {
				List<WorkerTimeLine> newWtl = new ArrayList<WorkerTimeLine>();
				newWtl.add(tl);
				timelines.put(unit, newWtl);
			}
		}
	}
	
	public WorkerTimeLine getEarliestWorkerTimeLine(String unit) throws ServiceException {
		List<WorkerTimeLine> wtls = this.timelines.get(unit);
		return getEarliestInternal(wtls);
	}
	/**
	 * 找出最早能生产的员工所属的生产单元
	 * @return
	 */
	public String findTheUnit() {
		Set<Entry<String, List<WorkerTimeLine>>> es = this.timelines.entrySet();
		WorkerTimeLine min = null;
		for (Entry<String, List<WorkerTimeLine>> entry : es) {
			//找出这个生产单元中最早能进行生产的员工的时间轴
			List<WorkerTimeLine> wtls = entry.getValue();
			WorkerTimeLine tmp = getEarliestInternal(wtls);
			if (min == null) {
				min = tmp;
				continue;
			}
			if (tmp.getCursor().compareTo(min.getCursor()) < 0) {
				min = tmp;
			}
		}
		return min.getWorker().getUnit();
	}
	
	private WorkerTimeLine getEarliestInternal(List<WorkerTimeLine> wtls) {
		if (wtls == null || wtls.size() == 0) return null;
		WorkerTimeLine min = wtls.get(0);
		for (int i = 1; i < wtls.size(); i++) {
			WorkerTimeLine wtl = wtls.get(i);
			//如果wtl的时间比min还早
			if (wtl.getCursor().compareTo(min.getCursor()) < 0) {
				min = wtl;
			}
		}
		return min;
	}
}
