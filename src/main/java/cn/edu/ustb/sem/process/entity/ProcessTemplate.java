package cn.edu.ustb.sem.process.entity;

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
@Table(name = "p_process_template")
public class ProcessTemplate implements Serializable {
	private static final long serialVersionUID = 82685047986159597L;
	public ProcessTemplate() {}
	public ProcessTemplate(int id) {
		this.id = id;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	//模板名称
	@Column(name = "name")
	private String name;
	//	族名称
	@Column(name = "group_name")
	private String groupName;
	
	//对应的产品代号
	@OneToMany(mappedBy = "pt", fetch = FetchType.EAGER, targetEntity=PtProductCode.class)
	@Cascade(value={ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	private Set<PtProductCode> productCodes;
	
	//包含的物料品类
	@OneToMany(mappedBy = "pt", fetch = FetchType.EAGER, targetEntity=Ptp.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Ptp> ptps;
	
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

	public Set<PtProductCode> getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(Set<PtProductCode> productCodes) {
		this.productCodes = productCodes;
	}

	public Set<Ptp> getPtps() {
		return ptps;
	}

	public void setPtps(Set<Ptp> ptps) {
		this.ptps = ptps;
	}

	public String getProductCodeString() {
		return productCodeString;
	}

	public void setProductCodeString(String productCodeString) {
		this.productCodeString = productCodeString;
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
	
}
