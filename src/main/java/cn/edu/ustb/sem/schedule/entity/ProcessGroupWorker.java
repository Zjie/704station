package cn.edu.ustb.sem.schedule.entity;

import java.io.Serializable;

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

import cn.edu.ustb.sem.schedule.web.model.ProcessWorkerModel;
@Entity
@Table(name = "s_process_group_worker")
public class ProcessGroupWorker implements Serializable {
	private static final long serialVersionUID = 8077240197210933041L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@ManyToOne
	@JoinColumn(name="gup_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private GroupUnitProcess gup;
	//如果是外协工序组，则没有worker的
	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="worker_id")
	@LazyToOne(LazyToOneOption.FALSE)
	private Worker worker;
	@Column(name = "time_to_consume")
	private Integer timeToConsume;
	@Column(name = "base")
	private Integer base;
	public ProcessGroupWorker(){}
	public ProcessGroupWorker(ProcessWorkerModel model) {
		if (model.getId() > 0) {
			this.id = model.getId();
		}
		if (model.getGupId() > 0) {
			this.gup = new GroupUnitProcess(model.getGupId());
		}
		if (model.getWorkerId() > 0) {
			this.worker = new Worker(model.getWorkerId());
		}
		this.timeToConsume = model.getTimeToConsume();
		this.base = model.getBase();
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

	public Worker getWorker() {
		return worker;
	}
	public void setWorker(Worker worker) {
		this.worker = worker;
	}
	public Integer getTimeToConsume() {
		return timeToConsume;
	}
	public void setTimeToConsume(Integer timeToConsume) {
		this.timeToConsume = timeToConsume;
	}
	public Integer getBase() {
		return base;
	}
	public void setBase(Integer base) {
		this.base = base;
	}
	
}
