package cn.edu.ustb.sem.schedule.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.ProcessGroupWorker;


public class GroupUnitProcessModel {
	private Integer id;
	//产品族id
	private Integer groupId;
	//产品族名称
	private String groupName;
	//生成单元
	private String unit;
	//工序组
	private Integer processGroup;
	private Integer afterProcessGroup;
	private String type;
	private Integer typeValue;
	private String remark;
	
	private List<ProcessWorkerModel> pgws;
	public GroupUnitProcessModel(){}
	public GroupUnitProcessModel(GroupUnitProcess gup) {
		this.id = gup.getId();
		this.groupId = gup.getGroupId();
		this.groupName = gup.getGroupName();
		this.unit = gup.getUnit();
		this.processGroup = gup.getProcessGroup();
		this.afterProcessGroup = gup.getAfterProcessGroup();
		int type = gup.getType();
		this.typeValue = type;
		this.remark = gup.getRemark();
		if (type == 0) {
			this.type = "外协";
		} else if (type == 1) {
			this.type = "特殊";
		} else if (type == 2) {
			this.type = "普通";
		}
		Set<ProcessGroupWorker> ppp = gup.getPgw();
		if (ppp != null) {
			this.pgws = new ArrayList<ProcessWorkerModel>(ppp.size());
			for (ProcessGroupWorker pgw : ppp) {
				this.pgws.add(new ProcessWorkerModel(pgw));
			}
		}
	}
	
	public Integer getAfterProcessGroup() {
		return afterProcessGroup;
	}
	public void setAfterProcessGroup(Integer afterProcessGroup) {
		this.afterProcessGroup = afterProcessGroup;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	public Integer getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(Integer typeValue) {
		this.typeValue = typeValue;
	}
	public List<ProcessWorkerModel> getPgws() {
		return pgws;
	}
	public void setPgws(List<ProcessWorkerModel> pgws) {
		this.pgws = pgws;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
