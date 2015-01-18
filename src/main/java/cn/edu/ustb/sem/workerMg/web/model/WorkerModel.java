package cn.edu.ustb.sem.workerMg.web.model;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.schedule.entity.Worker;

public class WorkerModel {

	private int id;
	private String realName;
	private String unit;
	private int uid;
	private String userName;
	private String isFreezed;
	public WorkerModel(){}
	public WorkerModel(Worker bo){
		this.id = bo.getId();
		this.realName = bo.getRealName();
		this.unit = bo.getUnit();
		if (bo.getIsFreezed() == Worker.IS_FREEZED) {
			this.isFreezed = "是";
		} else {
			this.isFreezed = "否";
		}
		User user = bo.getUser();
		if (user !=null) {
			this.uid = user.getId();
			this.userName = user.getUserName();
		}
	}
	
	public String getIsFreezed() {
		return isFreezed;
	}
	public void setIsFreezed(String isFreezed) {
		this.isFreezed = isFreezed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
	
	
}
