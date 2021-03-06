package cn.edu.ustb.sem.material.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cn.edu.ustb.sem.account.entity.User;

@Entity
@Table(name = "m_material_template")
public class MaterialTemplate implements Serializable {
	public MaterialTemplate() {}
	public MaterialTemplate(int id) {
		this.id = id;
	}
	
	private static final long serialVersionUID = 8239081111861325415L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	
	//物料模板名称
	@Column(name = "name")
	private String name;
	//	族名称
	@Column(name = "group_name")
	private String groupName;
	
	//对应的产品代号
	@OneToMany(mappedBy = "mt", fetch = FetchType.EAGER, targetEntity=MtProductCode.class)
	@Cascade(value={ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<MtProductCode> productCodes;
	
	//包含的物料品类
	@OneToMany(mappedBy = "mt", fetch = FetchType.EAGER, targetEntity=Mtm.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Mtm> mtms;
	
	@Column(name = "product_code_string")
	private String productCodeString;
	
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User updater;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Set<MtProductCode> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(Set<MtProductCode> productCodes) {
		this.productCodes = productCodes;
	}

	public Set<Mtm> getMtms() {
		return mtms;
	}

	public void setMtms(Set<Mtm> mtms) {
		this.mtms = mtms;
	}

	public Calendar getUdate() {
		return udate;
	}

	public void setUdate(Calendar udate) {
		this.udate = udate;
	}

	public User getUpdater() {
		return updater;
	}

	public void setUpdater(User updater) {
		this.updater = updater;
	}
	@Override
	public String toString() {
		return name;
	}

	public String getProductCodeString() {
		return productCodeString;
	}

	public void setProductCodeString(String productCodeString) {
		this.productCodeString = productCodeString;
	}
}