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
import cn.edu.ustb.sem.order.entity.OrderMaterial;

@Entity
@Table(name="a_assign")
public class Assign implements Serializable{

	private static final long serialVersionUID = 172037712311394213L;

	public Assign(){}

	@Id
	@Column(name="id",unique=true,nullable=false)
	@GenericGenerator(name="generator",strategy="native")
	@GeneratedValue(generator="generator")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="omid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private OrderMaterial orderMaterial;
	
	//配套数量
	@Column(name="mating_num")
	private int matingNum;
	
	@Column(name="udate")
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

	public OrderMaterial getOrderMaterial() {
		return orderMaterial;
	}

	public void setOrderMaterial(OrderMaterial orderMaterial) {
		this.orderMaterial = orderMaterial;
	}

	public int getMatingNum() {
		return matingNum;
	}

	public void setMatingNum(int matingNum) {
		this.matingNum = matingNum;
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
