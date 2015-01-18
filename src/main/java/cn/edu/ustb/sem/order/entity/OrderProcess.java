package cn.edu.ustb.sem.order.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.order.web.model.OrderProcessModel;
import cn.edu.ustb.sem.process.entity.PProcess;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;
import cn.edu.ustb.sem.schedule.entity.Worker;
@Entity
@Table(name = "o_order_process")
public class OrderProcess implements Serializable {
	private static final long serialVersionUID = -9026858143741023842L;
	public OrderProcess() {}
	public OrderProcess(PProcess p) {
		this.base = p.getBase();
		this.content = p.getContent();
		this.group = p.getGroup();
		this.measure = p.getMeasure();
		this.phase = p.getPhase();
		this.remark = p.getRemark();
	}
	public OrderProcess(OrderProcessModel opm) {
		this.id = opm.getId();
		this.base = opm.getBase();
		this.content = opm.getContent();
		//工序组
		this.group = opm.getGroupName();
		this.measure = opm.getMeasure();
		//生产阶段
		this.phase = opm.getPhase();
		this.remark = opm.getRemark();
		this.udate = Calendar.getInstance();
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Long id;
	@Column(name = "phase")
	private String phase;
	@Column(name = "content")
	private String content;
	@Column(name = "group_name")
	private Integer group;
	@Column(name = "measure")
	private Integer measure;
	@Column(name = "remark")
	private String remark;
	@Column(name = "base")
	private Integer base;
	
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User updater;
	@ManyToOne
	@JoinColumn(name="oid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private Order order;
	
	//订单工序与排产的关系---cwm
	@OneToMany(mappedBy="orderProcess",fetch=FetchType.LAZY,targetEntity=ProduceAssembling.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<ProduceAssembling> prs;
	
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getGroup() {
		return group;
	}

	public void setGroup(Integer group) {
		this.group = group;
	}

	public Integer getMeasure() {
		return measure;
	}

	public void setMeasure(Integer measure) {
		this.measure = measure;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBase() {
		return base;
	}

	public void setBase(Integer base) {
		this.base = base;
	}

	public Calendar getUdate() {
		return udate;
	}

	public void setUdate(Calendar udate) {
		this.udate = udate;
	}

	public User getUpdater() {
		return updater;
	}

	public void setUpdater(User updater) {
		this.updater = updater;
	}
	public Set<ProduceAssembling> getPrs() {
		return prs;
	}
	public void setPrs(Set<ProduceAssembling> prs) {
		this.prs = prs;
	}
	/**
	 * 返回该工序装配的在制数量
	 * @return
	 */
	public int getReportNum(Worker w) {
		if (this.prs == null || this.prs.size() == 0) return 0;
		int num = 0;
		for (ProduceAssembling pa : prs) {
			if (w != null && !w.getId().equals(pa.getWorker().getId())) {
				 continue;
			}
			num += pa.getReportNum();
		}
		return num;
	}
	
}
