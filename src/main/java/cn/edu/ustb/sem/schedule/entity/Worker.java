package cn.edu.ustb.sem.schedule.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.produce.entity.ProduceAssembled;
/**
 * 工人比较特殊，只能冻结工人，不能删除工人
 * 在排产的时候，不会将被冻结的员工的产能算进去
 * 在系统管理里面，列出所有工人信息，要标注是否被冻结
 * @author zhoujie04
 *
 */
@Entity
@Table(name = "s_worker")
public class Worker implements Serializable {
	public static final Byte IS_FREEZED = 1;
	public static final Byte NOT_FREEZED = 0;
	public Worker() {}
	public Worker(Integer id) {
		this.id = id;
	}
	private static final long serialVersionUID = -4917685223136473150L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	@Column(name = "real_name")
	private String realName;
	@Column(name = "unit")
	private String unit;
	@OneToOne(cascade={CascadeType.REFRESH})
    @JoinColumn(name="uid")
	@Fetch(FetchMode.JOIN)
	private User user;
	@Column(name = "is_freezed")
	private Byte IsFreezed = NOT_FREEZED;
	//报装配的完成个数
	@OneToMany(mappedBy = "worker",fetch = FetchType.LAZY, targetEntity = ProduceAssembled.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<ProduceAssembled> assembled;
	public Byte getIsFreezed() {
		return IsFreezed;
	}
	public void setIsFreezed(Byte isFreezed) {
		IsFreezed = isFreezed;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Set<ProduceAssembled> getAssembled() {
		return assembled;
	}
	public void setAssembled(Set<ProduceAssembled> assembled) {
		this.assembled = assembled;
	}
	
}
