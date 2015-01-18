package cn.edu.ustb.sem.produce.entity;

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

import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.schedule.entity.Worker;

@Entity
@Table(name = "pro_produce_assembled_report")
public class ProduceAssembled implements Serializable {
	private static final long serialVersionUID = 1243635787879863827L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@ManyToOne
	@JoinColumn(name="order_id")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private Order order;
	@ManyToOne
	@JoinColumn(name="worker_id")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private Worker worker;
	@Column(name = "assemble_num")
	private Integer assembleNum;
	@Column(name = "report_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar reportDate;
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
	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	public Integer getAssembleNum() {
		return assembleNum;
	}
	public void setAssembleNum(Integer assembleNum) {
		this.assembleNum = assembleNum;
	}
	public Calendar getReportDate() {
		return reportDate;
	}
	public void setReportDate(Calendar reportDate) {
		this.reportDate = reportDate;
	}
	
}
