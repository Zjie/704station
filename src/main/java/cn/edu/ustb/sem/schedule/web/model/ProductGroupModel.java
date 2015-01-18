package cn.edu.ustb.sem.schedule.web.model;

import cn.edu.ustb.sem.schedule.entity.GroupProductCode;

public class ProductGroupModel {
	private int id;
	private int groupId;
	private String groupName;
	private String productCode;
	public ProductGroupModel(){}
	public ProductGroupModel(GroupProductCode gpc) {
		this.id = gpc.getId();
		this.groupId = gpc.getGroupId();
		this.productCode = gpc.getProductCode();
		this.groupName = gpc.getGroupName();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
