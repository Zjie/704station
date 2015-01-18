package cn.edu.ustb.sem.produce.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.produce.web.model.TinggongModel;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;

@Entity
@Table(name="pro_tinggong_history")
public class ProduceTinggong implements Serializable{
	//停工状态
	public static int TINGGONG_REPORT = 1;//工人上报停工
	public static int TINGGONG_COMMIT = 2;//管理员确认停工
	public static int TINGGONG_RESTART = 3;//停工重启，这时可以重启报停工
	private static final long serialVersionUID = -3070274993752200995L;
	public ProduceTinggong() {}
	public ProduceTinggong(Integer id) {this.id = id;}
	@Id
	@Column(name="id",unique=true,nullable=false)
	@GenericGenerator(name="generator",strategy="native")
	@GeneratedValue(generator="generator")
	private Integer id;
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="order_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private Order order;
	@Column(name="reason")
	private String reason;
	@Column(name="tinggong_desc")
	private String desc;
	@Column(name="start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar start;
	@Column(name="end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar end;
	@Column(name="commit_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar commitDate;
	@Column(name="udate")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	@Column(name="restart_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar restartDate;
	
	//停工重启人
	@ManyToOne
	@JoinColumn(name="restart_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User tinggongRestarter;
	//停工确认人
	@ManyToOne
	@JoinColumn(name="commiter_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User tinggongCommiter;
	//停工确认人
	@ManyToOne
	@JoinColumn(name="reporter_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User tinggongReporter;
	@OneToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name="sr_id")
	@Fetch(FetchMode.JOIN)
	private ScheduleResult sr;
	@Column(name = "status")
	private Integer tinggongStatus;
	
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
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Calendar getStart() {
		return start;
	}
	public void setStart(Calendar start) {
		this.start = start;
	}
	public Calendar getEnd() {
		return end;
	}
	public void setEnd(Calendar end) {
		this.end = end;
	}
	public ProduceTinggong(TinggongModel model) {
		this.desc = model.getDesc();
		this.reason = model.getReason();
		try {
			this.end = DateUtil.parseDateTime(model.getEnd());
			this.start = DateUtil.parseDateTime(model.getStart());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer orderId = model.getOrderId();
		if (orderId != null && orderId > 0) {
			this.order = new Order(orderId);
		}
		this.sr = new ScheduleResult(model.getSrId());
	}

	public User getTinggongRestarter() {
		return tinggongRestarter;
	}

	public void setTinggongRestarter(User tinggongRestarter) {
		this.tinggongRestarter = tinggongRestarter;
	}

	public User getTinggongCommiter() {
		return tinggongCommiter;
	}

	public void setTinggongCommiter(User tinggongCommiter) {
		this.tinggongCommiter = tinggongCommiter;
	}

	public Integer getTinggongStatus() {
		return tinggongStatus;
	}

	public void setTinggongStatus(Integer tinggongStatus) {
		this.tinggongStatus = tinggongStatus;
	}

	public User getTinggongReporter() {
		return tinggongReporter;
	}

	public void setTinggongReporter(User tinggongReporter) {
		this.tinggongReporter = tinggongReporter;
	}
	public ScheduleResult getSr() {
		return sr;
	}
	public void setSr(ScheduleResult sr) {
		this.sr = sr;
	}
	public Calendar getUdate() {
		return udate;
	}
	public void setUdate(Calendar udate) {
		this.udate = udate;
	}
	public Calendar getCommitDate() {
		return commitDate;
	}
	public void setCommitDate(Calendar commitDate) {
		this.commitDate = commitDate;
	}
	public Calendar getRestartDate() {
		return restartDate;
	}
	public void setRestartDate(Calendar restartDate) {
		this.restartDate = restartDate;
	}
	
	
}
