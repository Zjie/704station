package cn.edu.ustb.sem.order.web.model;

import cn.edu.ustb.sem.core.web.model.SearchFormModel;

public class ProduceOtherSearchForm extends SearchFormModel {
	private String orderNo;
	private Integer workerId;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getWorkerId() {
		return workerId;
	}
	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}
	
}
