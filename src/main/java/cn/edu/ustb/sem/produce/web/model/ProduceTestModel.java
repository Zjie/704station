package cn.edu.ustb.sem.produce.web.model;

import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.produce.entity.ProduceTest;

public class ProduceTestModel {
	private Integer id;
	private String content;
	private String worker;
	private String udate;
	private Byte status;
	private String statusName;
	private Integer num;
	private String confirmDate;
	public ProduceTestModel() {}
	public ProduceTestModel(ProduceTest pt) {
		this.id = pt.getId();
		this.content = pt.getContent();
		this.worker = pt.getWorker().getRealName();
		this.udate = DateUtil.getDateTime(pt.getReportDate());
		this.confirmDate = DateUtil.getDateTime(pt.getConfirmDate());
		this.num = pt.getTestNum();;
		this.status = pt.getStatus();
		if (this.status.byteValue() == ProduceTest.FINISHED.byteValue()) {
			this.statusName = "是";
		} else {
			this.statusName = "否";
		}
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String comfirmDate) {
		this.confirmDate = comfirmDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
}
