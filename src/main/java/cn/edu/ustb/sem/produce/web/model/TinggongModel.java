package cn.edu.ustb.sem.produce.web.model;

import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;

public class TinggongModel {
	//订单编码
	private String code;
	private Integer id = 0;
	private Integer orderId = 0;
	private String reason = "";
	private String desc = "";
	private String start = "";
	private String end = "";
	private String udate = "";
	//重启时间
	private String restartDate = "";
	//确认时间
	private String commitDate = "";
	//暂停工序组的id
	private Integer srId = 0;
	private String srName = "";
	//订单编号
	private String orderNo = "";
	private String reporter = "";
	private String commiter = "";
	private String restarter = "";
	public TinggongModel() {}
	public TinggongModel(ProduceTinggong pt)
	{
		this.id = pt.getId();
		if (pt.getTinggongCommiter() != null)
			this.commiter = pt.getTinggongCommiter().getUserName();
		this.desc = pt.getDesc();
		this.end = DateUtil.getDateTime(pt.getEnd());
		if (pt.getOrder() != null)
		{
			this.orderNo = pt.getOrder().getNo();
			this.orderId = pt.getOrder().getId();
		}
		this.reason = pt.getReason();
		if (pt.getTinggongReporter() != null)
			this.reporter = pt.getTinggongReporter().getUserName();
		if (pt.getTinggongRestarter() != null)
			this.restarter = pt.getTinggongRestarter().getUserName();
		this.start = DateUtil.getDateTime(pt.getStart());
		this.srId = pt.getSr().getId();
		this.srName = "工序组" + pt.getSr().getGup().getProcessGroup();
		
		this.udate = DateUtil.getDateTime(pt.getUdate());
		this.restartDate = DateUtil.getDateTime(pt.getRestartDate());
		this.commitDate = DateUtil.getDateTime(pt.getCommitDate());
	}
	
	public String getUdate() {
		return udate;
	}
	public void setUdate(String udate) {
		this.udate = udate;
	}
	public Integer getSrId() {
		return srId;
	}
	public void setSrId(Integer srId) {
		this.srId = srId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getCommiter() {
		return commiter;
	}
	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}
	public String getRestarter() {
		return restarter;
	}
	public void setRestarter(String restarter) {
		this.restarter = restarter;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSrName() {
		return srName;
	}

	public void setSrName(String srName) {
		this.srName = srName;
	}
	public String getRestartDate() {
		return restartDate;
	}
	public void setRestartDate(String restartDate) {
		this.restartDate = restartDate;
	}
	public String getCommitDate() {
		return commitDate;
	}
	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}
	
}
