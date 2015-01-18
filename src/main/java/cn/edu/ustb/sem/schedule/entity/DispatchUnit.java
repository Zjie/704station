package cn.edu.ustb.sem.schedule.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
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

@Entity
@Table(name = "s_schedule_dispatch_unit")
public class DispatchUnit implements Serializable {
	private static final long serialVersionUID = -1232111801037620748L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="order_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private Order order;
	@Column(name = "unit", nullable = true)
	private String unit;
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User dispatcher;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Calendar getUdate() {
		return udate;
	}
	public void setUdate(Calendar udate) {
		this.udate = udate;
	}
	public User getDispatcher() {
		return dispatcher;
	}
	public void setDispatcher(User dispatcher) {
		this.dispatcher = dispatcher;
	}
	
}
