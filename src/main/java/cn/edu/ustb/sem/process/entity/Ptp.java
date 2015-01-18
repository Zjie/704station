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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
@Entity
@Table(name = "p_ptp")
public class Ptp implements Serializable {

	private static final long serialVersionUID = -8324544589118635084L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@ManyToOne
	@JoinColumn(name="ptid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private ProcessTemplate pt;
	@ManyToOne
	@JoinColumn(name="pid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private PProcess process;
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
	public PProcess getProcess() {
		return process;
	}
	public void setProcess(PProcess process) {
		this.process = process;
	}
	
}
