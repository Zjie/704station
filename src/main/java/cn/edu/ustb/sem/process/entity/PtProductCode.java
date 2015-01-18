package cn.edu.ustb.sem.process.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
@Entity
@Table(name = "p_process_template_product_code")
public class PtProductCode implements Serializable {
	public PtProductCode(Integer id) {
		this.id = id;
	}
	private static final long serialVersionUID = 898758680529644040L;
	public PtProductCode() {}
	public PtProductCode(String pc) {
		this.productCode = pc;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="ptid")
	@LazyToOne(LazyToOneOption.FALSE)
	private ProcessTemplate pt;
	
	@Column(name = "product_code")
	private String productCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProcessTemplate getPt() {
		return pt;
	}

	public void setPt(ProcessTemplate pt) {
		this.pt = pt;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}
