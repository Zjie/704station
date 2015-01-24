package cn.edu.ustb.sem.assign.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.schedule.entity.Worker;

@Entity
@Table(name="a_dispatch_material")
public class DispatchMaterial implements Serializable{
	private static final long serialVersionUID = -969313713121631494L;
	@Id
	@Column(name="id",unique=true,nullable=false)
	@GenericGenerator(name="generator",strategy="native")
	@GeneratedValue(generator="generator")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="oid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private Order order;
	
	@Column(name="udate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User updater;
	
	@Column(name = "remark")
	private String remark;
	
	@ManyToOne
	@JoinColumn(name="worker_id")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private Worker worker;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	
}
