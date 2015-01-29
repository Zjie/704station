package cn.edu.ustb.sem.order.web.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import cn.edu.ustb.sem.core.cons.OrderStatus;
import cn.edu.ustb.sem.core.cons.YesOrNo;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderMaterial;
import cn.edu.ustb.sem.order.entity.OrderProcess;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;
import cn.edu.ustb.sem.schedule.entity.Worker;

public class OrderModel {
	private int id;
	private String no;
	
	private String isReported;//订单是否完成报工---cwm
	private String isAssigned;//订单配套是否已经完成---cwm
	private String isDispatchMaterial;//是否领料过
	
	
	private String status;//订单状态 0：导入订单 1：待排产--cwm
	private String project;
	private String process;
	private String model;
	private String productName;
	private String productCode;

	private String planToAdjust;
	//投产数量
	private int produceNum;
	//交付数量
	private int deliveryNum;
	//典试数量
	private int testNum;
	
	//这是为科二单开的字段
	private String ke2ProduceNum;
	private String ke2DianshiNum;
	
	//订单信息确认
	private int infoIsChecked;
	private String infoCheckTime;
	private String processChecker;
	//物料信息确认
	private int materialIsChecked;
	private String materialCheckTime;
	private String materialChecker;
	
	private String productBatchId;
	private String remark;

	private String onlineDate;
	private String finishDate;
	private String udate;

	private String typer;
	private String scheduler;

	private String materialsCompleteness;
	private String componentCompleteness;

	private String stockInDate;
	private String certificateDate;
//	private String orderNo;
	private String units;
	private String reportFinishedTime;
	private String reportBeginTime;
	private List<OrderMaterialModel> orderMaterials;

	private List<OrderProcessModel> orderProcesses;
	
	private String materialsQitaoTime;
	private int finishedNum;
	
	//装配完成数量
	private int assembledNum;
	//装配在制数量
	private int assemblingNum;
	//在验数量
	private int testingNum;
	//完成试验的数量
	private int testedNum;
	//还可以报试验的数量
	private int canReportTestNum;
	//在典数量
	private int dianshiingNum;
	//典试完成数量
	private int dianshiedNum;
	//还可以报典试的数量
	private int canDianshiNum;
	
	//班组
	private String banzu;
	//物料配套信息
	private int assignStatus;
	//物料配套新的总计
	private String assignItems;
	//物料配套总量统计
	private String assignNums;
	
	////////////////////////////////////////////////////////
	//////////////////////2015-01-20 增加额外字段
	//产品类别
	@Column(name = "product_type")
	private String productType;
	//车间备注
	@Column(name = "factory_remark")
	private String factoryRemark;
	//最后一次计划更改日期
	@Column(name = "last_adjust_date")
	private String lastAdjustDate;
	//计划更改次数
	@Column(name = "plan_adjust_num")
	private String planAdjustNum;
	//工艺员
	@Column(name = "gongyiyuan")
	private String gongyiyuan;
	
	public int getFinishedNum() {
		return finishedNum;
	}
	public void setFinishedNum(int finishedNum) {
		this.finishedNum = finishedNum;
	}
	public OrderModel() {}
	public OrderModel(Order bo, Worker w, Calendar begin, Calendar end) {
		this.id = bo.getId();
		
		Byte finished = bo.getIsAssigned();
		this.isAssigned = YesOrNo.getName(finished);
		Byte isDispatchMaterial = bo.getIsDispatchMaterial();
		this.isDispatchMaterial = YesOrNo.getName(isDispatchMaterial);
		
		Byte status = bo.getStatus();
		this.status = OrderStatus.getName(status);
		this.project = bo.getProject();
		this.process = bo.getProcess();
		this.productName = bo.getProductName();
		this.model = bo.getModel();
		this.productCode = bo.getProductCode();

		this.planToAdjust = bo.getPlanToAdjust();
		this.produceNum = bo.getProduceNum();
		this.deliveryNum = bo.getDeliveryNum();
		this.testNum = bo.getTestNum();

		this.productBatchId = bo.getProductBatchId();
		this.remark = bo.getRemark();

		this.onlineDate = DateUtil.getDate(bo.getOnlineDate());
		this.finishDate = DateUtil.getDate(bo.getFinishDate());
		this.udate = DateUtil.getDate(bo.getUdate());
		this.reportFinishedTime = DateUtil.getDateTime(bo.getReportFinishedTime());
		this.reportBeginTime = DateUtil.getDateTime(bo.getReportBeginTime());
		
		
		if (bo.getTyper() != null) {
			this.typer = bo.getTyper().getUserName();
		}
		if (bo.getScheduler() != null) {
			this.scheduler = bo.getScheduler().getUserName();
		}
		if (bo.getMaterialsCompleteness() != null) {
			this.materialsCompleteness = DateUtil.getDateTime(bo.getMaterialsCompleteness());
		} else {
			this.materialsCompleteness = "";
		}
		this.no = bo.getNo();

		this.stockInDate = bo.getStockInDate();
		this.certificateDate = bo.getCertificateDate();
		
//		this.orderNo = bo.getOrderNo();

		List<OrderMaterialModel> oms = new ArrayList<OrderMaterialModel>();
		Set<OrderMaterial> mset = bo.getOms();
		int onAsssigned = 0;
		int totalItem = 0;
		int totalNum = 0;
		int assignNum = 0;
		int produceNum = bo.getProduceNum();
		if (mset != null)
			for (OrderMaterial o : mset) {
				totalItem++;
				totalNum += produceNum * o.getSingleNum();
				assignNum += o.getAssignedNum();
				if (o.getSingleNum() == 0 || o.getStatus() == Order.ASSIGN_STATUS_PROCESS || o.getStatus() == Order.ASSIGN_STATUS_FINISHED) {
					onAsssigned++;
				}
				oms.add(new OrderMaterialModel(o));
			}
		//配置项统计
		this.assignItems = onAsssigned + "/" + totalItem;
		//数量统计
		this.assignNums = assignNum + "/" + totalNum;
		this.orderMaterials=oms;
		List<OrderProcessModel> pms = new ArrayList<OrderProcessModel>();
		Set<OrderProcess> pset = bo.getOps();
		Set<String> uns = new HashSet<String>();
		if(pset!=null)
			for(OrderProcess o:pset){
				Set<ProduceAssembling> prt = o.getPrs();
				for (ProduceAssembling pr : prt) {
					String unit = pr.getWorker().getUnit();
					if (uns.contains(unit)) continue;
					uns.add(unit);
				}
				pms.add(new OrderProcessModel(o, w, begin, end));
			}
		this.orderProcesses=pms;
		String temp = "";
		for (String u : uns) {
			temp += "," + u;
		}
		if (temp.startsWith(",")) {
			temp = temp.substring(1, temp.length());
		}
		this.units = temp;
		this.infoIsChecked = bo.getProcessIsCheck();
		this.infoCheckTime = DateUtil.getDateTime(bo.getProcessCheckTime());
		this.componentCompleteness = bo.getComponentCompleteness();
		this.materialsCompleteness = DateUtil.getDate(bo.getMaterialsCompleteness());
		this.materialsQitaoTime = bo.getMaterialsQitaoTime();
		//装配完成数量
		this.assembledNum = bo.getAssembledNum(w);
		//装配在制数量
		this.assemblingNum = bo.getAssemblingNum(w);
		this.testingNum = bo.getTestingNum(w);
		this.testedNum = bo.getTestedNum(w);
		//如果装配完成数量 > 试验完成数量，则可以报试验
		this.canReportTestNum = this.assembledNum - this.testedNum - this.testingNum;
		
		this.dianshiedNum = bo.getDianshied(w);
		this.dianshiingNum = bo.getDianshiing(w);
		//可以报的典试数量 = 全部典试数量 - 在典数量 - 已完成的数量
		this.canDianshiNum = bo.getTestNum() - this.dianshiedNum - this.dianshiingNum;
		
		//是否完成报工的条件为：投产数量=完成数量 且 典试数量=完成数量
		if (this.assembledNum == this.produceNum && this.dianshiedNum == this.testNum) {
			this.isReported = "是";
		} else {
			this.isReported = "否";
		}
		
		this.ke2DianshiNum = bo.getKe2DianshiNum();
		this.ke2ProduceNum = bo.getKe2produceNum();
		if (bo.getProcessChecker() != null) {
			this.processChecker = bo.getProcessChecker().getUserName();
		}
		if (bo.getMaterialIsChecked() != null) {
			this.materialIsChecked = bo.getMaterialIsChecked().intValue();
		}
		this.materialCheckTime = DateUtil.getDate(bo.getMaterialCheckTime());
		if (bo.getMaterialChecker() != null) {
			this.materialChecker = bo.getMaterialChecker().getUserName();
		}
		
		this.banzu = bo.getBanzu();
		this.assignStatus = bo.getAssignStatus().intValue();
		
		this.factoryRemark = bo.getFactoryRemark();
		this.gongyiyuan = bo.getGongyiyuan();
		this.planAdjustNum = bo.getPlanAdjustNum();
		this.productType = bo.getProductType();
		this.lastAdjustDate = bo.getLastAdjustDate();
		
	}
	public OrderModel(Order bo) {
		this(bo, null, null, null);
	}

	public String getReportBeginTime() {
		return reportBeginTime;
	}
	public void setReportBeginTime(String reportBeginTime) {
		this.reportBeginTime = reportBeginTime;
	}
	public String getCertificateDate() {
		return certificateDate;
	}

	public String getComponentCompleteness() {
		return componentCompleteness;
	}

	public int getDeliveryNum() {
		return deliveryNum;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public int getId() {
		return id;
	}

	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getMaterialsCompleteness() {
		return materialsCompleteness;
	}

	public String getModel() {
		return model;
	}

	public String getNo() {
		return no;
	}

	public String getOnlineDate() {
		return onlineDate;
	}

	public List<OrderMaterialModel> getOrderMaterials() {
		return orderMaterials;
	}

//	public String getOrderNo() {
//		return orderNo;
//	}

	public String getPlanToAdjust() {
		return planToAdjust;
	}

	public String getProcess() {
		return process;
	}

	public List<OrderProcessModel> getOrderProcesses() {
		return orderProcesses;
	}

	public int getProduceNum() {
		return produceNum;
	}

	public String getProductBatchId() {
		return productBatchId;
	}

	public String getProductCode() {
		return productCode;
	}

	public String getProductName() {
		return productName;
	}

	public String getProject() {
		return project;
	}

	public String getRemark() {
		return remark;
	}

	public String getScheduler() {
		return scheduler;
	}

	public String getStockInDate() {
		return stockInDate;
	}

	public int getTestNum() {
		return testNum;
	}

	public String getTyper() {
		return typer;
	}

	public String getUdate() {
		return udate;
	}

	public void setCertificateDate(String certificateDate) {
		this.certificateDate = certificateDate;
	}

	public void setComponentCompleteness(String componentCompleteness) {
		this.componentCompleteness = componentCompleteness;
	}

	public void setDeliveryNum(int deliveryNum) {
		this.deliveryNum = deliveryNum;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMaterialsCompleteness(String materialsCompleteness) {
		this.materialsCompleteness = materialsCompleteness;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setOnlineDate(String onlineDate) {
		this.onlineDate = onlineDate;
	}

	public void setOrderMaterials(List<OrderMaterialModel> orderMaterials) {
		this.orderMaterials = orderMaterials;
	}

//	public void setOrderNo(String orderNo) {
//		this.orderNo = orderNo;
//	}

	public void setPlanToAdjust(String planToAdjust) {
		this.planToAdjust = planToAdjust;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public void setOrderProcesses(List<OrderProcessModel> orderProcesses) {
		this.orderProcesses = orderProcesses;
	}

	public void setProduceNum(int produceNum) {
		this.produceNum = produceNum;
	}

	public void setProductBatchId(String productBatchId) {
		this.productBatchId = productBatchId;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}

	public void setStockInDate(String stockInDate) {
		this.stockInDate = stockInDate;
	}

	public void setTestNum(int testNum) {
		this.testNum = testNum;
	}

	public void setTyper(String typer) {
		this.typer = typer;
	}

	public void setUdate(String udate) {
		this.udate = udate;
	}

	public String getIsReported() {
		return isReported;
	}
	public void setIsReported(String isReported) {
		this.isReported = isReported;
	}
	public String getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(String isAssigned) {
		this.isAssigned = isAssigned;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsDispatchMaterial() {
		return isDispatchMaterial;
	}
	public void setIsDispatchMaterial(String isDispatchMaterial) {
		this.isDispatchMaterial = isDispatchMaterial;
	}
	public String getReportFinishedTime() {
		return reportFinishedTime;
	}
	public void setReportFinishedTime(String reportFinishedTime) {
		this.reportFinishedTime = reportFinishedTime;
	}
	public int getInfoIsChecked() {
		return infoIsChecked;
	}
	public void setInfoIsChecked(int infoIsChecked) {
		this.infoIsChecked = infoIsChecked;
	}
	public String getInfoCheckTime() {
		return infoCheckTime;
	}
	public void setInfoCheckTime(String infoCheckTime) {
		this.infoCheckTime = infoCheckTime;
	}
	public String getMaterialsQitaoTime() {
		return materialsQitaoTime;
	}
	public void setMaterialsQitaoTime(String materialsQitaoTime) {
		this.materialsQitaoTime = materialsQitaoTime;
	}
	public int getAssembledNum() {
		return assembledNum;
	}
	public void setAssembledNum(int assembledNum) {
		this.assembledNum = assembledNum;
	}
	public int getAssemblingNum() {
		return assemblingNum;
	}
	public void setAssemblingNum(int assemblingNum) {
		this.assemblingNum = assemblingNum;
	}

	public int getTestingNum() {
		return testingNum;
	}
	public void setTestingNum(int testingNum) {
		this.testingNum = testingNum;
	}
	public int getTestedNum() {
		return testedNum;
	}
	public void setTestedNum(int testedNum) {
		this.testedNum = testedNum;
	}
	public int getCanReportTestNum() {
		return canReportTestNum;
	}
	public void setCanReportTestNum(int canReportTestNum) {
		this.canReportTestNum = canReportTestNum;
	}
	public int getDianshiingNum() {
		return dianshiingNum;
	}
	public void setDianshiingNum(int dianshiingNum) {
		this.dianshiingNum = dianshiingNum;
	}
	public int getDianshiedNum() {
		return dianshiedNum;
	}
	public void setDianshiedNum(int dianshiedNum) {
		this.dianshiedNum = dianshiedNum;
	}
	public int getCanDianshiNum() {
		return canDianshiNum;
	}
	public void setCanDianshiNum(int canDianshiNum) {
		this.canDianshiNum = canDianshiNum;
	}
	public String getKe2ProduceNum() {
		return ke2ProduceNum;
	}
	public void setKe2ProduceNum(String ke2ProduceNum) {
		this.ke2ProduceNum = ke2ProduceNum;
	}
	public String getKe2DianshiNum() {
		return ke2DianshiNum;
	}
	public void setKe2DianshiNum(String ke2DianshiNum) {
		this.ke2DianshiNum = ke2DianshiNum;
	}
	public String getProcessChecker() {
		return processChecker;
	}
	public void setProcessChecker(String processChecker) {
		this.processChecker = processChecker;
	}
	public int getMaterialIsChecked() {
		return materialIsChecked;
	}
	public void setMaterialIsChecked(int materialIsChecked) {
		this.materialIsChecked = materialIsChecked;
	}
	public String getMaterialCheckTime() {
		return materialCheckTime;
	}
	public void setMaterialCheckTime(String materialCheckTime) {
		this.materialCheckTime = materialCheckTime;
	}
	public String getMaterialChecker() {
		return materialChecker;
	}
	public void setMaterialChecker(String materialChecker) {
		this.materialChecker = materialChecker;
	}
	public String getBanzu() {
		return banzu;
	}
	public void setBanzu(String banzu) {
		this.banzu = banzu;
	}
	public int getAssignStatus() {
		return assignStatus;
	}
	public void setAssignStatus(int assignStatus) {
		this.assignStatus = assignStatus;
	}
	public String getAssignItems() {
		return assignItems;
	}
	public void setAssignItems(String assignItems) {
		this.assignItems = assignItems;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getFactoryRemark() {
		return factoryRemark;
	}
	public void setFactoryRemark(String factoryRemark) {
		this.factoryRemark = factoryRemark;
	}
	public String getLastAdjustDate() {
		return lastAdjustDate;
	}
	public void setLastAdjustDate(String lastAdjustDate) {
		this.lastAdjustDate = lastAdjustDate;
	}
	public String getPlanAdjustNum() {
		return planAdjustNum;
	}
	public void setPlanAdjustNum(String planAdjustNum) {
		this.planAdjustNum = planAdjustNum;
	}
	public String getGongyiyuan() {
		return gongyiyuan;
	}
	public void setGongyiyuan(String gongyiyuan) {
		this.gongyiyuan = gongyiyuan;
	}
	public String getAssignNums() {
		return assignNums;
	}
	public void setAssignNums(String assignNums) {
		this.assignNums = assignNums;
	}

}
