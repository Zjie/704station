package cn.edu.ustb.sem.account.web.model;

public class UserSaveModel {
	private int uid;
	private String userName;
	private String nickName;
	private String password;
	private int roleId;
	private boolean saveOrNot;
	
	public boolean isSaveOrNot() {
		return saveOrNot;
	}
	public void setSaveOrNot(boolean saveOrNot) {
		this.saveOrNot = saveOrNot;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
