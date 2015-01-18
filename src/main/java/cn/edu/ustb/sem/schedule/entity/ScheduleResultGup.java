package cn.edu.ustb.sem.schedule.entity;

import java.util.Calendar;

public class ScheduleResultGup {

//	private Integer orderId;
	
	private String orderNo;
	
	private GroupUnitProcess gup;
	
	private String unit;
	
	private Integer processGroup;
	
	private Calendar begin;
	
	private Calendar end;
	
	private String color;

//	public Integer getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(Integer orderId) {
//		this.orderId = orderId;
//	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public GroupUnitProcess getGup() {
		return gup;
	}

	public void setGup(GroupUnitProcess gup) {
		this.gup = gup;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getProcessGroup() {
		return processGroup;
	}

	public void setProcessGroup(Integer processGroup) {
		this.processGroup = processGroup;
	}

	public Calendar getBegin() {
		return begin;
	}

	public void setBegin(Calendar begin) {
		this.begin = begin;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}
