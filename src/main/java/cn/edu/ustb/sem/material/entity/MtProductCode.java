package cn.edu.ustb.sem.material.entity;

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
@Table(name = "m_material_template_product_code")
public class MtProductCode implements Serializable {
	public MtProductCode(Integer id) {
		this.id = id;
	}
	private static final long serialVersionUID = -103248041193336867L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="mt_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private MaterialTemplate mt;
	
	@Column(name = "product_code")
	private String productCode;

	public MtProductCode() {}
	public MtProductCode(String pc) {
		this.productCode = pc;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MaterialTemplate getMt() {
		return mt;
	}

	public void setMt(MaterialTemplate mt) {
		this.mt = mt;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	@Override
	public String toString() {
		return productCode;
	}
}
