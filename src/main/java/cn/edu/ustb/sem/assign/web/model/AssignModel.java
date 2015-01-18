package cn.edu.ustb.sem.assign.web.model;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.assign.entity.Assign;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.entity.OrderMaterial;

public class AssignModel {

	private int id;
	private long omid;
	private int matingNum;
	private String udate;
	private String updater;
	public AssignModel(long omid, int matingNum) {
		this.omid = omid;
		this.matingNum = matingNum;
	}
	public AssignModel(){}
	public AssignModel(Assign bo){
		this.id=bo.getId();
		OrderMaterial orderMaterial=bo.getOrderMaterial();
		if(orderMaterial!=null){
			this.omid=orderMaterial.getId();
		}
		this.matingNum=bo.getMatingNum();
		this.udate=DateUtil.getDateTime(bo.getUdate());
		User updater=bo.getUpdater();
		if(updater!=null){
			this.updater=updater.getUserName();
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getOmid() {
		return omid;
	}
	public void setOmid(long omid) {
		this.omid = omid;
	}
	public int getMatingNum() {
		return matingNum;
	}
	public void setMatingNum(int matingNum) {
		this.matingNum = matingNum;
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
