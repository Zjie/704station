package cn.edu.ustb.sem.kpi.web.model;

import cn.edu.ustb.sem.core.web.model.SearchFormModel;

public class WorkerKpiSearchForm extends SearchFormModel {
	private Integer workerId;
	private String dateBegin;
	private String dateEnd;
	private Integer orderId;
	public Integer getWorkerId() {
		return workerId;
	}
	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
}
