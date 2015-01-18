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
@Table(name = "pro_produce_dianshi")
public class ProduceDianshi implements Serializable {
	private static final long serialVersionUID = 1077483926659923971L;
	//正在典试
	public static final Byte ON_PROCESS = 0;
	//典试完成
	public static final Byte FINISHED = 1;
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
	@Column(name = "num")
	private Integer testNum;
	@Column(name = "udate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar reportDate;
	@Column(name = "confirm_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar confirmDate;
	@Column(name = "status")
	private Byte status;
	@Column(name = "content")
	private String content;
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
	public Integer getTestNum() {
		return testNum;
	}
	public void setTestNum(Integer testNum) {
		this.testNum = testNum;
	}
	public Calendar getReportDate() {
		return reportDate;
	}
	public void setReportDate(Calendar reportDate) {
		this.reportDate = reportDate;
	}
	public Calendar getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(Calendar confirmDate) {
		this.confirmDate = confirmDate;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
