package cn.edu.ustb.sem.material.web.model;

import cn.edu.ustb.sem.core.web.model.SearchFormModel;

public class MaterialTemplateSearchModel extends SearchFormModel {
	private int id;
	private String name;
	private String groupName;
	private String productCode;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
