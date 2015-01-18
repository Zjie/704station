package cn.edu.ustb.sem.kpi.web.model;

public class GroupProduceInfo {
	private String code;
	private int planNum;
	private int actualNum;
	private int diff;
	private float finishedRate;
	private String remark;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getPlanNum() {
		return planNum;
	}
	public void setPlanNum(int planNum) {
		this.planNum = planNum;
	}
	public int getActualNum() {
		return actualNum;
	}
	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}
	public int getDiff() {
		return diff;
	}
	public void setDiff(int diff) {
		this.diff = diff;
	}
	public float getFinishedRate() {
		return finishedRate;
	}
	public void setFinishedRate(float finishedRate) {
		this.finishedRate = finishedRate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
