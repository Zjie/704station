package cn.edu.ustb.sem.assign.web.model;

import java.util.List;

public class AssignForSaveModel {
	//订单的id
	private int orderId;
	//订单所有物料已配套数量与将要配套数量之和
	private int totalAssign;
	//订单所有物料需要配套的数量
	private int totalSingleNum; 
	//每个orderMaterial的配套情况
	private List<AssignModel> assigns;
	//配套时间
	private String reportDate;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getTotalAssign() {
		return totalAssign;
	}

	public void setTotalAssign(int totalAssign) {
		this.totalAssign = totalAssign;
	}

	public int getTotalSingleNum() {
		return totalSingleNum;
	}

	public void setTotalSingleNum(int totalSingleNum) {
		this.totalSingleNum = totalSingleNum;
	}

	public List<AssignModel> getAssigns() {
		return assigns;
	}

	public void setAssigns(List<AssignModel> assigns) {
		this.assigns = assigns;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	
}
