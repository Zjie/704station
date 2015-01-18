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

import cn.edu.ustb.sem.order.entity.OrderProcess;
import cn.edu.ustb.sem.schedule.entity.Worker;

@Entity
@Table(name="pro_produce_assembling_report")
public class ProduceAssembling implements Serializable{

	private static final long serialVersionUID = -2983623697727272132L;
	//还未开始报工
	public static final Byte NOT_REPORT = 0;
	//报工完成
	public static final Byte REPORTED = 1;
	//正在报工
	public static final Byte REPORTING = 2;
	public ProduceAssembling(){}
	
	@Id
	@Column(name="id",unique=true,nullable=false)
	@GenericGenerator(name="generator",strategy="native")
	@GeneratedValue(generator="generator")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="opid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private OrderProcess orderProcess;
	
	@Column(name="report_num")
	private Integer reportNum;
	
	@Column(name="report_time")
	private Integer reportTime;
	
	@Column(name="udate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	
	@ManyToOne
	@JoinColumn(name="worker_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private Worker worker;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OrderProcess getOrderProcess() {
		return orderProcess;
	}

	public void setOrderProcess(OrderProcess orderProcess) {
		this.orderProcess = orderProcess;
	}

	public Integer getReportNum() {
		return reportNum;
	}

	public void setReportNum(Integer reportNum) {
		this.reportNum = reportNum;
	}

	public Integer getReportTime() {
		return reportTime;
	}

	public void setReportTime(Integer reportTime) {
		this.reportTime = reportTime;
	}

	public Calendar getUdate() {
		return udate;
	}

	public void setUdate(Calendar udate) {
		this.udate = udate;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}
}
