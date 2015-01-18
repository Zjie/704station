package cn.edu.ustb.sem.produce.entity;

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

import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.Worker;
@Entity
@Table(name = "pro_print_history")
public class PrintHistory implements Serializable {
	private static final long serialVersionUID = 6014763055770979190L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="gup_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private GroupUnitProcess gup;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="order_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private Order order;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="printer_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private Worker printer;
	@Column(name = "print_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar printDate;
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
	public Worker getPrinter() {
		return printer;
	}
	public void setPrinter(Worker printer) {
		this.printer = printer;
	}
	public Calendar getPrintDate() {
		return printDate;
	}
	public void setPrintDate(Calendar printDate) {
		this.printDate = printDate;
	}
	
}
