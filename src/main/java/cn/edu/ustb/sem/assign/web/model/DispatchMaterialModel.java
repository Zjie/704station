package cn.edu.ustb.sem.assign.web.model;

import cn.edu.ustb.sem.assign.entity.DispatchMaterial;
import cn.edu.ustb.sem.core.util.DateUtil;

public class DispatchMaterialModel {
	private int id;
	private int orderId;
	//订单编号
	private String no;
	private String workerName;
	private String remark;
	private String udate;
	private String updater;
	
	public DispatchMaterialModel() {}
	
	public DispatchMaterialModel(DispatchMaterial dm) {
		this.id = dm.getId();
		this.orderId = dm.getOrder().getId();
		this.no = dm.getOrder().getNo();
		this.workerName = dm.getWorker().getRealName();
		this.remark = dm.getRemark();
		this.updater = dm.getUpdater().getUserName();
		this.udate = DateUtil.getDate(dm.getUdate());
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
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
