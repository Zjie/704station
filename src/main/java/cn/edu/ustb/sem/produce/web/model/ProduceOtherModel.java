package cn.edu.ustb.sem.produce.web.model;

import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.produce.entity.ProduceOther;

public class ProduceOtherModel {
	private Integer id;
	private String content;
	private String worker;
	private String udate;
	private Byte status;
	//订单编号
	private String orderNo;
	public ProduceOtherModel() {}
	public ProduceOtherModel(ProduceOther pt) {
		this.id = pt.getId();
		this.content = pt.getContent();
		this.worker = pt.getWorker().getRealName();
		this.udate = DateUtil.getDateTime(pt.getReportDate());
		this.orderNo = pt.getOrder().getNo();
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
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
}
