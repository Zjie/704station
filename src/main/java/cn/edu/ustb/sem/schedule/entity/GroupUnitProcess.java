package cn.edu.ustb.sem.schedule.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import cn.edu.ustb.sem.schedule.web.model.GroupUnitProcessModel;
@Entity
@Table(name = "s_group_unit_process")
public class GroupUnitProcess implements Serializable {
	public GroupUnitProcess() {}
	public GroupUnitProcess(Integer id) {
		this.id = id;
	}
	public GroupUnitProcess(GroupUnitProcessModel form) {
		this.id = form.getId();
		this.groupId = form.getGroupId();
		this.groupName = form.getGroupName();
		this.processGroup = form.getProcessGroup();
		this.unit = form.getUnit();
		this.type = form.getTypeValue();
		this.afterProcessGroup = form.getAfterProcessGroup();
	}
	private static final long serialVersionUID = 9094002424194260342L;
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	//产品族编号
	@Column(name = "group_id")
	private Integer groupId;
	//产品族名称
	@Column(name = "group_name")
	private String groupName;
	@Column(name = "unit")
	private String unit;
	//工序组编号
	@Column(name = "process_group")
	private Integer processGroup;
	@Column(name = "after_process_group")
	private Integer afterProcessGroup;
	@Column(name = "type")
	private Integer type;
	@OneToMany(mappedBy = "gup", fetch = FetchType.LAZY, targetEntity=ProcessGroupWorker.class, cascade={CascadeType.REFRESH})
	private Set<ProcessGroupWorker> pgw;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getProcessGroup() {
		return processGroup;
	}
	public void setProcessGroup(Integer processGroup) {
		this.processGroup = processGroup;
	}
	public Set<ProcessGroupWorker> getPgw() {
		return pgw;
	}
	public void setPgw(Set<ProcessGroupWorker> pgw) {
		this.pgw = pgw;
	}
	public Integer getAfterProcessGroup() {
		return afterProcessGroup;
	}
	public void setAfterProcessGroup(Integer afterProcessGroup) {
		this.afterProcessGroup = afterProcessGroup;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
