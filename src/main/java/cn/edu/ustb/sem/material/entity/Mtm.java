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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "m_mtm")
public class Mtm implements Serializable {
	private static final long serialVersionUID = 1739877624585981324L;
	public Mtm(){}
	public Mtm(Integer id) {
		this.id = id;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="mt_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private MaterialTemplate mt;
	
	@ManyToOne
	@JoinColumn(name="m_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private Material material;

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

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
	
}
