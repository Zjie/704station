package cn.edu.ustb.sem.schedule.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "s_worker_timeline")
public class WorkerTimeLine implements Serializable, Cloneable {
	private static final long serialVersionUID = -790543002590251865L;
	public WorkerTimeLine(){}
	public WorkerTimeLine(Integer id) {
		this.id = id;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@OneToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name="worker_id")
	@Fetch(FetchMode.JOIN)
	private Worker worker;
	@Column(name = "duration")
	private Integer duration;
	@Column(name = "start", nullable = true)
	@Temporal(TemporalType.DATE)
	private Calendar start;
	//时间坐标
	@Transient
	private Calendar cursor;
	//排了多少天的活
	@Transient
	public int dayCount;
	
	public int getDayCount() {
		return dayCount;
	}
	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}
	
	public Calendar getCursor() {
		return cursor;
	}
	public void setCursor(Calendar cursor) {
		this.cursor = cursor;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Calendar getStart() {
		return start;
	}
	public void setStart(Calendar start) {
		this.start = start;
	}
	@Override
	public WorkerTimeLine clone() {
		WorkerTimeLine wtl;
		try {
			wtl = (WorkerTimeLine)super.clone();
			wtl.id = this.id;
			wtl.dayCount = this.dayCount;
			wtl.start = (Calendar) this.start.clone();
			wtl.cursor = (Calendar) wtl.start.clone();
			wtl.duration = this.duration;
			wtl.worker = this.worker;
			return wtl;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new InternalError();
		}
	}
}
