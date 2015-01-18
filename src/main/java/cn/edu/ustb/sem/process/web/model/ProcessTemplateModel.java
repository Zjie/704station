package cn.edu.ustb.sem.process.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.process.entity.PProcess;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
import cn.edu.ustb.sem.process.entity.Ptp;

public class ProcessTemplateModel {
	private Integer id;
	private String groupName;
	private String name;
	private String productCodeString;
	private String updater;
	private String udate;
	private List<ProcessModel> processes;
	public ProcessTemplateModel() {}
	public ProcessTemplateModel(ProcessTemplate bo) {
		this.id = bo.getId();
		this.groupName = bo.getGroupName();
		this.name = bo.getName();
		this.productCodeString = bo.getProductCodeString();
		this.udate = DateUtil.getDateTime(bo.getUdate());
		User updater = bo.getUpdater();
		if (updater != null) {
			this.updater = bo.getUpdater().getUserName();
		}
		List<ProcessModel> processes = new ArrayList<ProcessModel>();
		Set<Ptp> ptps = bo.getPtps();
		if (ptps != null) {
			for (Ptp ptp : ptps) {
				PProcess p = ptp.getProcess();
				if (p == null) {
					continue;
				}
				processes.add(new ProcessModel(p));
			}
		}
		this.processes = processes;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductCodeString() {
		return productCodeString;
	}
	public void setProductCodeString(String productCodeString) {
		this.productCodeString = productCodeString;
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
	public List<ProcessModel> getProcesses() {
		return processes;
	}
	public void setProcesses(List<ProcessModel> processes) {
		this.processes = processes;
	}
	
}
