package cn.edu.ustb.sem.order.web.model;

import cn.edu.ustb.sem.core.web.model.SearchFormModel;

public class OrderSearchForm extends SearchFormModel {
	private Integer id;
	//序号
	private String no;
	//课题编号
	private String project;
	//主管工艺
	private String process;
	//使用型号
	private String model;
	//产品名称
	private String productName;
	//产品代号
	private String productCode;
	//产品批次及编号
	private String productBatchId;
	//订单的物料配套是否完成---cwm
	private Byte assignStatus;
	//订单是否完成报工
	private Byte isReported;
	//订单状态--cwm
	private Byte status;
	//班组
	private String banzu;
	//是否确认物料信息
	private Byte materialIsCheck;
	
	private String reportFinishedTime;
	private String reportBeginTime;
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductBatchId() {
		return productBatchId;
	}
	public void setProductBatchId(String productBatchId) {
		this.productBatchId = productBatchId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Byte getAssignStatus() {
		return assignStatus;
	}
	public void setAssignStatus(Byte assignStatus) {
		this.assignStatus = assignStatus;
	}
	public Byte getIsReported() {
		return isReported;
	}
	public void setIsReported(Byte isReported) {
		this.isReported = isReported;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public String getReportFinishedTime() {
		return reportFinishedTime;
	}
	public void setReportFinishedTime(String reportFinishedTime) {
		this.reportFinishedTime = reportFinishedTime;
	}
	public String getReportBeginTime() {
		return reportBeginTime;
	}
	public void setReportBeginTime(String reportBeginTime) {
		this.reportBeginTime = reportBeginTime;
	}
	public String getBanzu() {
		return banzu;
	}
	public void setBanzu(String banzu) {
		this.banzu = banzu;
	}
	public Byte getMaterialIsCheck() {
		return materialIsCheck;
	}
	public void setMaterialIsCheck(Byte materialIsCheck) {
		this.materialIsCheck = materialIsCheck;
	}
	
}
