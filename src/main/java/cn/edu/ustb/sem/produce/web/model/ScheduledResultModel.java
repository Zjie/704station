package cn.edu.ustb.sem.produce.web.model;

import cn.edu.ustb.sem.schedule.entity.ScheduleResult;

public class ScheduledResultModel {
	private Integer id;
	private String name;
	private String remark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ScheduledResultModel(){}
	public ScheduledResultModel(ScheduleResult sr) {
		this.id = sr.getId();
		this.name = "工序组" + sr.getGup().getProcessGroup();
		this.remark = sr.getGup().getRemark();
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
