package cn.edu.ustb.sem.schedule.web.model;

import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcessType;
import cn.edu.ustb.sem.schedule.entity.ProcessGroupWorker;
import cn.edu.ustb.sem.schedule.entity.Worker;

public class ProcessWorkerModel {
	private int id;
	private String workerName;
	private int workerId;
	private int gupId;
	private String gupName;
	//粒度为天
	private int timeToConsume;
	//基数
	private int base;
	public ProcessWorkerModel() {}
	public ProcessWorkerModel(ProcessGroupWorker model) {
		this.id = model.getId();
		this.base = model.getBase();
		GroupUnitProcess gup = model.getGup();
		this.gupId = gup.getId();
		this.timeToConsume = model.getTimeToConsume();
		Worker w = model.getWorker();
		if (w != null && gup.getType() != GroupUnitProcessType.WAIXIE) {
			this.workerId = w.getId();
			this.workerName = w.getRealName();
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public int getWorkerId() {
		return workerId;
	}
	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}
	public int getGupId() {
		return gupId;
	}
	public void setGupId(int gupId) {
		this.gupId = gupId;
	}
	public String getGupName() {
		return gupName;
	}
	public void setGupName(String gupName) {
		this.gupName = gupName;
	}
	public int getTimeToConsume() {
		return timeToConsume;
	}
	public void setTimeToConsume(int timeToConsume) {
		this.timeToConsume = timeToConsume;
	}
	public int getBase() {
		return base;
	}
	public void setBase(int base) {
		this.base = base;
	}
	
}
