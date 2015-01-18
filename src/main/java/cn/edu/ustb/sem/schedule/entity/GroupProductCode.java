package cn.edu.ustb.sem.schedule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.edu.ustb.sem.schedule.web.model.ProductGroupModel;

@Entity
@Table(name = "s_group_product_code")
public class GroupProductCode implements Serializable {
	
	private static final long serialVersionUID = -6783849300064577232L;
	public GroupProductCode(){}
	public GroupProductCode(ProductGroupModel pgm) {
		if (pgm.getId() > 0)
			this.id = pgm.getId();
		this.groupId = pgm.getGroupId();
		this.groupName = pgm.getGroupName();
		this.productCode = pgm.getProductCode();
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@Column(name = "product_code")
	private String productCode;
	@Column(name="group_id")
	private Integer groupId;
	@Column(name = "group_name")
	private String groupName;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
