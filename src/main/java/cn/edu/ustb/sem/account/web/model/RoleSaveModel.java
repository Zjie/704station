package cn.edu.ustb.sem.account.web.model;

import java.util.List;

public class RoleSaveModel {
	private String roleName;
	private String desc;
	private List<Integer> appIds;
	private int roleId;
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Integer> getAppIds() {
		return appIds;
	}
	public void setAppIds(List<Integer> appIds) {
		this.appIds = appIds;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
