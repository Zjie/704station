package cn.edu.ustb.sem.process.web.model;

import cn.edu.ustb.sem.core.web.model.SearchFormModel;

public class ProcessSearchForm extends SearchFormModel {
	private int pid;
	private String phase;
	private String content;
	private int groupName;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
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
	
}
