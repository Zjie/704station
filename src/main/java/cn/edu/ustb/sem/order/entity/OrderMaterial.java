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
import cn.edu.ustb.sem.assign.entity.Assign;
import cn.edu.ustb.sem.material.entity.Material;
import cn.edu.ustb.sem.order.web.model.OrderMaterialModel;

/**
 * 订单和物料模板和工序模板的关系
 * @author zhoujie04
 *
 */
@Entity
@Table(name = "o_order_material")
public class OrderMaterial implements Serializable {
	public OrderMaterial(Long id) {
		this.id = id;
	}
	private static final long serialVersionUID = 1143251733588930655L;
	public OrderMaterial() {}
	public OrderMaterial(Material m) {
		this.backupNum = m.getBackupNum();
		this.level = m.getLevel();
		this.name = m.getName();
		this.singleNum = m.getSingleNum();
		this.specification = m.getSpecification();
		this.type = m.getType();
		this.uom = m.getUom();
	}
	public OrderMaterial(OrderMaterialModel omm) {
		this.backupNum = omm.getBkNum();
		this.id = omm.getId();
		this.level = omm.getLevel();
		this.name = omm.getName();
		this.singleNum = omm.getSingleNum();
		this.specification = omm.getSpecification();
		this.type = omm.getType();
		this.udate = Calendar.getInstance();
		this.uom = omm.getUom();
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="oid")
	@NotFound(action=NotFoundAction.EXCEPTION)
	@LazyToOne(LazyToOneOption.PROXY)
	private Order order;
	//物料名称
	@Column(name = "name")
	private String name;
	//型号规格
	@Column(name = "specification")
	private String specification;
	//质量等级或标准要求
	@Column(name = "level")
	private String level;
	//计量单位
	@Column(name = "uom")
	private String uom;
	//单机数量
	@Column(name = "single_num")
	private Integer singleNum;
	//工艺备份数量
	@Column(name = "bk_num")
	private Integer backupNum;
	//物料种类
	@Column(name = "type")
	private String type;
	
	//订单物料与领料的关系---cwm
	@OneToMany(mappedBy="orderMaterial",fetch=FetchType.LAZY,targetEntity=Assign.class)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<Assign> as;
	
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	
	@ManyToOne
	@JoinColumn(name="uuid")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User updater;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Integer getSingleNum() {
		return singleNum;
	}

	public void setSingleNum(Integer singleNum) {
		this.singleNum = singleNum;
	}

	public Integer getBackupNum() {
		return backupNum;
	}

	public void setBackupNum(Integer backupNum) {
		this.backupNum = backupNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<Assign> getAs() {
		return as;
	}
	public void setAs(Set<Assign> as) {
		this.as = as;
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
	//获取该订单物料的配套状态 0为未配套 1为配套中 2为配套完成
	public byte getStatus() {
		if (this.as == null || this.as.size() == 0)
			return Order.ASSIGN_STATUS_NO;
		int totalAssignNum = 0;
		for (Assign as : this.as) {
			totalAssignNum += as.getMatingNum();
		}
		if (totalAssignNum == this.singleNum * this.order.getProduceNum()) {
			return Order.ASSIGN_STATUS_FINISHED;
		} else {
			return Order.ASSIGN_STATUS_PROCESS;
		}
	}
	public int getAssignedNum() {
		if (this.as == null || this.as.size() == 0)
			return 0;
		int totalAssignNum = 0;
		for (Assign as : this.as) {
			totalAssignNum += as.getMatingNum();
		}
		return totalAssignNum;
	}
}
