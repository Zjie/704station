package cn.edu.ustb.sem.account.web.model;

import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.util.DateUtil;

public class UserListModel {
	private int id;
	private String userName;
	private String roleName;
	private String udate;
	private String updater;
	private String nickName;
	public UserListModel(User u) {
		this.id = u.getId();
		this.userName = u.getUserName();
		this.nickName = u.getNickName();
		Role role = u.getRole();
		if (role != null) {
			this.roleName = u.getRole().getName();
		}
		this.udate = DateUtil.getDateTime(u.getUdate());
		User updater = u.getUpdater();
		if (updater != null) {
			this.updater = u.getUpdater().getUserName();
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
