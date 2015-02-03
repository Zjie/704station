package cn.edu.ustb.sem.process.web.model;

import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.process.entity.PProcess;

public class ProcessModel {
	private int id;
	private String phase;
	private String content;
	private int groupName;
	private double measure;
	private String remark;
	private String updater;
	private String udate;
	private int base;
	public ProcessModel() {}
	public ProcessModel(PProcess bo) {
		this.id = bo.getId();
		this.phase = bo.getPhase();
		this.content = bo.getContent();
		this.groupName = bo.getGroup();
		this.measure = bo.getMeasure();
		this.remark = bo.getRemark();
		this.base = bo.getBase();
		this.udate = DateUtil.getDateTime(bo.getUdate());
		if (bo.getUpdater() !=null) {
			this.updater = bo.getUpdater().getUserName();
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getGroupName() {
		return groupName;
	}
	public void setGroupName(int groupName) {
		this.groupName = groupName;
	}
	public double getMeasure() {
		return measure;
	}
	public void setMeasure(double measure) {
		this.measure = measure;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public int getBase() {
		return base;
	}
	public void setBase(int base) {
		this.base = base;
	}
	
}
