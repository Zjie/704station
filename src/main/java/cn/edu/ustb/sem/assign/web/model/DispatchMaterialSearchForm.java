package cn.edu.ustb.sem.assign.web.model;

import cn.edu.ustb.sem.core.web.model.SearchFormModel;

public class DispatchMaterialSearchForm extends SearchFormModel {
	private String no;
	private Integer workerId;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public Integer getWorkerId() {
		return workerId;
	}
	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}
	
}
