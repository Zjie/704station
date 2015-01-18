package cn.edu.ustb.sem.schedule.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

import cn.edu.ustb.sem.order.entity.Order;

@Entity
@Table(name = "s_unschedule_process_group")
public class UnscheduleProcessGroup implements Serializable {
	private static final long serialVersionUID = -7123807746346010852L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="oid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private Order order;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="gup_id")
	@LazyToOne(LazyToOneOption.FALSE)
	@NotFound(action=NotFoundAction.EXCEPTION)
	private GroupUnitProcess gup;
	@Column(name = "type")
	private Integer type;
	@Column(name = "status")
	private Integer status;
	@Column(name = "unit")
	private String unit;
	
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public GroupUnitProcess getGup() {
		return gup;
	}
	public void setGup(GroupUnitProcess gup) {
		this.gup = gup;
	}
}
