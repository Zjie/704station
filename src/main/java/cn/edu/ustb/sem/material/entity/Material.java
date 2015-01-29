package cn.edu.ustb.sem.material.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cn.edu.ustb.sem.account.entity.User;

@Entity
@Table(name = "m_material")
public class Material implements Serializable {
	private static final long serialVersionUID = -8209162567693676912L;
	public Material() {}
	public Material(Integer id) {
		this.id = id;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	//物料名称
	@Column(name = "name")
	private String name;
	//型号规格
	@Column(name = "specification")
	private String specification;
	//质量等级或标准要求
	@Column(name = "level")
	private String level;
	//计量单位
	@Column(name = "uom")
	private String uom;
	//单机数量
	@Column(name = "single_num")
	private Double singleNum;
	//工艺备份数量
	@Column(name = "bk_num")
	private Double backupNum;
	//物料种类
	@Column(name = "type")
	private String type;
	//序号
	@Column(name = "no")
	private Integer no;
	//对应的物料模板
	@OneToOne(mappedBy = "material", fetch = FetchType.EAGER, targetEntity=Mtm.class)
	private Mtm mtm;
	
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User updater;
	
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public Double getSingleNum() {
		return singleNum;
	}
	public void setSingleNum(Double singleNum) {
		this.singleNum = singleNum;
	}
	public Double getBackupNum() {
		return backupNum;
	}
	public void setBackupNum(Double backupNum) {
		this.backupNum = backupNum;
	}

	public Mtm getMtm() {
		return mtm;
	}
	public void setMtm(Mtm mtms) {
		this.mtm = mtms;
	}
	@Override
	public String toString() {
		return name;
	}
}
