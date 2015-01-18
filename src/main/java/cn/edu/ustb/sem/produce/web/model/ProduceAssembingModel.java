package cn.edu.ustb.sem.produce.web.model;

import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderProcess;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;
import cn.edu.ustb.sem.schedule.entity.Worker;

public class ProduceAssembingModel {

	private int id;
	private long opid;
	private int orderId;
	private String orderNo;
	private String phase;
	private String content;
	private String groupName;
	private int reportNum;
	private int reportTime;
	private String udate;
	private String updater;
	private int reportedNum;
	public ProduceAssembingModel(long opid, int reportNum){
		this.opid = opid;
		this.reportNum = reportNum;
	}
	
	public int getReportedNum() {
		return reportedNum;
	}

	public void setReportedNum(int reportedNum) {
		this.reportedNum = reportedNum;
	}

	public ProduceAssembingModel(){}
	
	public ProduceAssembingModel(ProduceAssembling bo){
		this.id = bo.getId();
		OrderProcess orderProcess = bo.getOrderProcess();
		if(orderProcess != null){
			this.opid = orderProcess.getId();
			this.phase = orderProcess.getPhase();
			this.content = orderProcess.getContent();
			this.groupName = orderProcess.getGroup() + "";
		}
		Order o = orderProcess.getOrder();
		this.orderId = o.getId();
		this.orderNo = o.getNo();
		this.reportNum = bo.getReportNum();
		this.reportTime = bo.getReportTime();
		this.udate = DateUtil.getDateTime(bo.getUdate());
		Worker updater = bo.getWorker();
		if(updater != null){
			this.updater = updater.getRealName();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getOpid() {
		return opid;
	}

	public void setOpid(long opid) {
		this.opid = opid;
	}

	public int getReportNum() {
		return reportNum;
	}

	public void setReportNum(int reportNum) {
		this.reportNum = reportNum;
	}

	public int getReportTime() {
		return reportTime;
	}

	public void setReportTime(int reportTime) {
		this.reportTime = reportTime;
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

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
