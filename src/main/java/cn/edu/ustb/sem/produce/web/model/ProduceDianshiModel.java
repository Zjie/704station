package cn.edu.ustb.sem.produce.web.model;

import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.produce.entity.ProduceDianshi;

public class ProduceDianshiModel {
	private Integer id;
	private String worker;
	private String udate;
	private Byte status;
	private Integer num;
	private String confirmDate;
	private String statusName;
	private String content;
	
	public ProduceDianshiModel() {}
	public ProduceDianshiModel(ProduceDianshi pt) {
		this.id = pt.getId();
		this.worker = pt.getWorker().getRealName();
		this.udate = DateUtil.getDateTime(pt.getReportDate());
		this.confirmDate = DateUtil.getDateTime(pt.getConfirmDate());
		this.num = pt.getTestNum();;
		this.status = pt.getStatus();
		if (this.status == ProduceDianshi.FINISHED) {
			this.statusName = "是";
		} else {
			this.statusName = "否";
		}
		this.content = pt.getContent();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
