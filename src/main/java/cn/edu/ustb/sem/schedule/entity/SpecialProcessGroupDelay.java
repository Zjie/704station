package cn.edu.ustb.sem.schedule.entity;

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

import cn.edu.ustb.sem.order.entity.Order;
@Entity
@Table(name = "s_special_process_group_delay")
public class SpecialProcessGroupDelay implements Serializable {
	private static final long serialVersionUID = 1300893078289638910L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@ManyToOne
	@JoinColumn(name="gup_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private GroupUnitProcess gup;
	@ManyToOne
	@JoinColumn(name="order_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private Order order;
	@Column(name = "should_start_date", nullable = true)
	@Temporal(TemporalType.DATE)
	private Calendar shouldStartDate;
	@Column(name = "rest_num")
	private Integer restNum;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public GroupUnitProcess getGup() {
		return gup;
	}
	public void setGup(GroupUnitProcess gup) {
		this.gup = gup;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Calendar getShouldStartDate() {
		return shouldStartDate;
	}
	public void setShouldStartDate(Calendar shouldStartDate) {
		this.shouldStartDate = shouldStartDate;
	}
	public Integer getRestNum() {
		return restNum;
	}
	public void setRestNum(Integer restNum) {
		this.restNum = restNum;
	}
	
	
}
