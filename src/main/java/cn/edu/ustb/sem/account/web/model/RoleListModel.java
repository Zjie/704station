package cn.edu.ustb.sem.account.web.model;

import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.core.util.DateUtil;

public class RoleListModel {
	private int id;
	private String name;
	private String remark;
	private String udate;
	private String updater;
	public RoleListModel(Role role) {
		id = role.getId();
		name = role.getName();
		remark = role.getRemark();
		this.udate = DateUtil.getDateTime(role.getUdate());
		if (role.getUpdater() != null) {
			updater = role.getUpdater().getUserName();
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	
}
