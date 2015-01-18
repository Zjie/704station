package cn.edu.ustb.sem.produce.web.model;

import java.util.List;

public class ProduceReportForSaveModel {

	//订单Id
	private int orderId;
	//该订单所有工序数量总和
	private int totalReportNum;
	//已排产工序总数与将要排产工序总数之和
	private int totalReport;
	//报工时间
	private String reportDate;
	private List<ProduceAssembingModel> produceReports;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getTotalReportNum() {
		return totalReportNum;
	}
	public void setTotalReportNum(int totalReportNum) {
		this.totalReportNum = totalReportNum;
	}
	public int getTotalReport() {
		return totalReport;
	}
	public void setTotalReport(int totalReport) {
		this.totalReport = totalReport;
	}
	public List<ProduceAssembingModel> getProduceReports() {
		return produceReports;
	}
	public void setProduceReports(List<ProduceAssembingModel> produceReports) {
		this.produceReports = produceReports;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	
}
