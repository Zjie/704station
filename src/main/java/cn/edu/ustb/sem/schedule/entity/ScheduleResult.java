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

import cn.edu.ustb.sem.order.entity.Order;
@Entity
@Table(name = "s_schedule_result")
public class ScheduleResult implements Serializable {
	public ScheduleResult(){}
	public ScheduleResult(Integer id) {
		this.id = id;
	}
	//首次排产
	public static final int INIT = 0;
	//用户确认该排产结果
	public static final int COMMIT = 1;
	//用户取消该排产结果
	public static final int CANCEL = 2;
	//工人打印该派工单
	public static final int PRINTED = 3;
	//由于报停工而导致该工序组挂起
	public static final int TINGGONG = 4;
	//由于动态调整而导致该工序组被删除
	public static final int ADJUST_DELETE = 5;
	
	public String getStatusName() {
		switch (this.status.intValue())
		{
			case INIT:
				return "未排产";
			case COMMIT:
				return "已排产";
			case PRINTED:
				return "已打印派工单";
			case TINGGONG:
				return "停工";
			default:
				return "未排产";
		}
	}
	private static final long serialVersionUID = -2611148706701441066L;
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
	@Column(name = "start", nullable = true)
	@Temporal(TemporalType.DATE)
	private Calendar start;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="worker_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private Worker worker;
	@Column(name = "num")
	private Integer num;
	@Column(name = "duration")
	private Integer duration;
	@Column(name = "status")
	private Integer status;
	
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
	public Calendar getStart() {
		return start;
	}
	public void setStart(Calendar start) {
		this.start = start;
	}
	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
}
