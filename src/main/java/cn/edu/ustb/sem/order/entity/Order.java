package cn.edu.ustb.sem.order.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.web.model.OrderMaterialModel;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderProcessModel;
import cn.edu.ustb.sem.produce.entity.ProduceAssembled;
import cn.edu.ustb.sem.produce.entity.ProduceDianshi;
import cn.edu.ustb.sem.produce.entity.ProduceOther;
import cn.edu.ustb.sem.produce.entity.ProduceTest;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;
import cn.edu.ustb.sem.schedule.entity.GroupProcessTree;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
import cn.edu.ustb.sem.schedule.entity.Worker;

@Entity
@Table(name = "o_order")
public class Order implements Serializable {
	private static final long serialVersionUID = 4955281354735952309L;
	//工序信息是否被确认
	public static final byte PROCESS_IS_CHECKED = 1;
	public static final byte PROCESS_IS_NOT_CHECKED = 0;
	//物料信息是否被确认
	public static final byte MATERIAL_IS_CHECKED = 1;
	public static final byte MATERIAL_IS_NOT_CHECKED = 0;
	//物料配套信息
	public static final byte ASSIGN_STATUS_NO = 0;
	public static final byte ASSIGN_STATUS_PROCESS = 1;
	public static final byte ASSIGN_STATUS_FINISHED = 2;
	
	public Order() {}
	public Order(int id) {
		this.id = id;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GenericGenerator(name = "generator", strategy = "native")
	@GeneratedValue(generator = "generator")
	private Integer id;
	
	@Column(name = "project")
	private String project;
	@Column(name = "process")
	private String process;
	@Column(name = "model")
	private String model;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "product_code")
	private String productCode;
	
	@Column(name = "plan_to_adjust")
	private String planToAdjust;
	/**
	 * 这个是车间计划数量，区分于科二的投产数量，取Y列
	 */
	@Column(name = "produce_num")
	private Integer produceNum;
	/**
	 * 交付数量
	 */
	@Column(name = "delivery_num")
	private Integer deliveryNum;
	/**
	 * 这个是典试计划数量，区分于科二的典试数量，取AF列
	 */
	@Column(name = "test_num")
	private Integer testNum;
	
	//新加两个科二的字段，投产数量和典试数量，仅用于显示
	@Column(name = "ke2_produce_num")
	private String ke2produceNum;
	@Column(name = "ke2_dianshi_num")
	private String ke2DianshiNum;
	
	@Column(name = "product_batch_id")
	private String productBatchId;
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "online_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar onlineDate;
	@Column(name = "finish_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar finishDate;
	
	@Column(name = "udate", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar udate;
	
	@ManyToOne
	@JoinColumn(name="typer_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User typer;
	//排产员
	@ManyToOne
	@JoinColumn(name="scheduler_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User scheduler;
	//外协重启人
	@ManyToOne
	@JoinColumn(name="waixie_restart_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User waixieRestarter;
	
	
	//物料齐套时间
	@Column(name = "materials_completeness", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar materialsCompleteness;
	
	@Column(name = "component_completeness", nullable = true)
	private Calendar componentCompleteness;
	
	@Column(name = "materials_qitao_time", nullable = true)
	private Calendar materialsQitaoTime;
	
	//物料是否已经配齐---cwm
	@Column(name = "is_assigned")
	private Byte isAssigned;
	
	//订单状态 0：导入订单 1：待排产--cwm
	@Column(name = "status")
	private Byte status;
	
	
	//订单是否完成报工---cwm
	@Column(name = "is_reported")
	private Byte isReported;
	@Column(name = "reported_num")
	private Integer reportedNum;
	//订单是否领料过
	@Column(name = "is_dispatch_material")
	private Byte isDispatchMaterial;
	
	@Column(name="no")
	private String no;
	//入库进度
	@Column(name="stock_in_date", nullable = true)
	private String stockInDate;
	//证书完成时间
	@Column(name="certificate_date", nullable = true)
	private String certificateDate;
	
	//订单编号，唯一索引
	@Column(name="order_no", nullable = false)
	private String orderNo;
	
	
	//物料关系
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, targetEntity=OrderMaterial.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<OrderMaterial> oms;
	
	
	//过程关系---ddh
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, targetEntity=OrderProcess.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<OrderProcess> ops;

	//订单与排产结果关系---cwm
	@OneToMany(mappedBy = "order",fetch = FetchType.LAZY, targetEntity = ScheduleResult.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<ScheduleResult> srs;
	//报停工
	@OneToMany(mappedBy = "order",fetch = FetchType.LAZY, targetEntity = ProduceTinggong.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<ProduceTinggong> pts;
	//报装配的完成个数
	@OneToMany(mappedBy = "order",fetch = FetchType.LAZY, targetEntity = ProduceAssembled.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<ProduceAssembled> assembled;
	//试验报工
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, targetEntity = ProduceTest.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<ProduceTest> produceTest;
	//典试报工
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, targetEntity = ProduceDianshi.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<ProduceDianshi> produceDianshi;
	//其他报工
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, targetEntity = ProduceOther.class)
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<ProduceOther> produceother;
	
	public Set<ProduceTinggong> getPts() {
		return pts;
	}
	public void setPts(Set<ProduceTinggong> pts) {
		this.pts = pts;
	}
	//报工完成时间
	@Column(name="report_finished_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar reportFinishedTime;
	//报工开始时间
	@Column(name="report_begin_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar reportBeginTime;
	
	
	//用于标记工艺员是否确认该订单的工序信息信息
	@Column(name="process_is_checked")
	private Byte processIsCheck;
	@Column(name="process_check_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar processCheckTime;
	//工序信息确认者
	@ManyToOne
	@JoinColumn(name="process_checker")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User processChecker;
	
	//物料信息确认
	@Column(name = "material_is_checked")
	private Byte materialIsChecked;
	@Column(name="material_check_time", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar materialCheckTime;
	//物料信息确认者
	@ManyToOne
	@JoinColumn(name="material_checker")
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.PROXY)
	private User materialChecker;
	
	//班组信息
	@Column(name = "banzu")
	private String banzu;
	//物料配套信息, 0未配套 1配套中 2已齐套
	@Column(name = "assign_status", nullable = false)
	private Byte assignStatus;
	
	public Byte getMaterialIsChecked() {
		return materialIsChecked;
	}
	public void setMaterialIsChecked(Byte materialIsChecked) {
		this.materialIsChecked = materialIsChecked;
	}
	public Calendar getMaterialCheckTime() {
		return materialCheckTime;
	}
	public void setMaterialCheckTime(Calendar materialCheckTime) {
		this.materialCheckTime = materialCheckTime;
	}
	public Calendar getProcessCheckTime() {
		return processCheckTime;
	}
	public void setProcessCheckTime(Calendar infoCheckTime) {
		this.processCheckTime = infoCheckTime;
	}
	public Byte getProcessIsCheck() {
		return processIsCheck;
	}
	public void setProcessIsCheck(Byte infoIsCheck) {
		this.processIsCheck = infoIsCheck;
	}
	public Calendar getReportFinishedTime() {
		return reportFinishedTime;
	}
	public void setReportFinishedTime(Calendar reportFinishedTime) {
		this.reportFinishedTime = reportFinishedTime;
	}
	//排产用的辅助变量
	//保存工序组树
	@Transient
	public GroupProcessTree tree;
	//临时保存排产好的结果，用来生成甘特图
	@Transient
	public Set<ScheduleResult> tempSrs;
	//该订单将在哪个生产单元上进行排产
	@Transient
	public String unit;
	@Transient //是否被排产了
	public boolean isScheduled;
	@Transient
	public boolean isWaixie;
	
	public Set<OrderProcess> getOps() {
		return ops;
	}
	public void setOps(Set<OrderProcess> ops) {
		this.ops = ops;
	}
	public Set<OrderMaterial> getOms() {
		return oms;
	}

	public void setOms(Set<OrderMaterial> oms) {
		this.oms = oms;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getNo() {
		return no;
	}

	public String getStockInDate() {
		return stockInDate;
	}

	public void setStockInDate(String stockInDate) {
		this.stockInDate = stockInDate;
	}

	public String getCertificateDate() {
		return certificateDate;
	}

	public void setCertificateDate(String certificateDate) {
		this.certificateDate = certificateDate;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPlanToAdjust() {
		return planToAdjust;
	}

	public void setPlanToAdjust(String planToAdjust) {
		this.planToAdjust = planToAdjust;
	}

	public Integer getProduceNum() {
		return produceNum;
	}

	public void setProduceNum(Integer produceNum) {
		this.produceNum = produceNum;
	}

	public Integer getDeliveryNum() {
		return deliveryNum;
	}

	public void setDeliveryNum(Integer deliveryNum) {
		this.deliveryNum = deliveryNum;
	}

	public Integer getTestNum() {
		return testNum;
	}

	public void setTestNum(Integer testNum) {
		this.testNum = testNum;
	}

	public String getProductBatchId() {
		return productBatchId;
	}

	public void setProductBatchId(String productBatchId) {
		this.productBatchId = productBatchId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Calendar getOnlineDate() {
		return onlineDate;
	}

	public void setOnlineDate(Calendar onlineDate) {
		this.onlineDate = onlineDate;
	}

	public Calendar getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Calendar finishDate) {
		this.finishDate = finishDate;
	}

	public Calendar getUdate() {
		return udate;
	}

	public void setUdate(Calendar udate) {
		this.udate = udate;
	}

	public User getTyper() {
		return typer;
	}

	public void setTyper(User typer) {
		this.typer = typer;
	}

	public User getScheduler() {
		return scheduler;
	}

	public void setScheduler(User scheduler) {
		this.scheduler = scheduler;
	}

	public Calendar getMaterialsCompleteness() {
		return materialsCompleteness;
	}

	public void setMaterialsCompleteness(Calendar materialsCompleteness) {
		this.materialsCompleteness = materialsCompleteness;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Byte getIsAssigned() {
		return isAssigned;
	}
	
	public void setIsAssigned(Byte isAssigned) {
		this.isAssigned = isAssigned;
	}
	
	public Byte getStatus() {
		return status;
	}
	
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Byte getIsReported() {
		return isReported;
	}
	public void setIsReported(Byte isReported) {
		this.isReported = isReported;
	}
	public Set<ScheduleResult> getSrs() {
		return srs;
	}
	public void setSrs(Set<ScheduleResult> srs) {
		this.srs = srs;
	}
	
	public User getWaixieRestarter() {
		return waixieRestarter;
	}
	public void setWaixieRestarter(User waixieRestarter) {
		this.waixieRestarter = waixieRestarter;
	}
	public Byte getIsDispatchMaterial() {
		return isDispatchMaterial;
	}
	public void setIsDispatchMaterial(Byte isDispatchMaterial) {
		this.isDispatchMaterial = isDispatchMaterial;
	}
	public Order(OrderModel om) {
		this.certificateDate = om.getCertificateDate();
		this.deliveryNum = om.getDeliveryNum();
		try {
			this.finishDate = DateUtil.parseDate(om.getFinishDate());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		this.id = om.getId();
		this.model = om.getModel();
		this.no = om.getNo();
		List<OrderMaterialModel> omms = om.getOrderMaterials();
		Set<OrderMaterial> tt = new HashSet<OrderMaterial>();
		this.oms = tt;
		if (omms != null) {
			for (OrderMaterialModel omm : omms) {
				OrderMaterial ome = new OrderMaterial(omm);
				ome.setOrder(this);
				ome.setUdate(this.udate);
				ome.setUpdater(this.typer);
				tt.add(ome);
			}
		}
		
		try {
			this.onlineDate = DateUtil.parseDate(om.getOnlineDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<OrderProcessModel> opms = om.getOrderProcesses();
		Set<OrderProcess> tt2 = new HashSet<OrderProcess>();
		this.ops = tt2;
		if (opms != null) {
			for (OrderProcessModel opm : opms) {
				OrderProcess op = new OrderProcess(opm);
				op.setOrder(this);
				op.setUdate(this.udate);
				op.setUpdater(this.typer);
				tt2.add(op);
			}
		}
//		this.orderNo = om.getOrderNo();
		this.planToAdjust = om.getPlanToAdjust();
		this.process = om.getProcess();
		this.produceNum = om.getProduceNum();
		this.productBatchId = om.getProductBatchId();
		this.productCode = om.getProductCode();
		this.productName = om.getProductName();
		this.project = om.getProject();
		this.remark = om.getRemark();
		
		this.stockInDate = om.getStockInDate();
		this.testNum = om.getTestNum();
		try {
			this.reportFinishedTime = DateUtil.parseDateTime(om.getReportFinishedTime());
			this.reportBeginTime = DateUtil.parseDateTime(om.getReportFinishedTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		this.ke2DianshiNum = om.getKe2DianshiNum();
		this.ke2produceNum = om.getKe2ProduceNum();
		
		this.banzu = om.getBanzu();
		this.assignStatus = new Byte(om.getAssignStatus() + "");
	}
	public Calendar getReportBeginTime() {
		return reportBeginTime;
	}
	public void setReportBeginTime(Calendar reportBeginTime) {
		this.reportBeginTime = reportBeginTime;
	}
	public Integer getReportedNum() {
		return reportedNum;
	}
	public void setReportedNum(Integer reportedNum) {
		this.reportedNum = reportedNum;
	}
	public Calendar getComponentCompleteness() {
		return componentCompleteness;
	}
	public void setComponentCompleteness(Calendar componentCompleteness) {
		this.componentCompleteness = componentCompleteness;
	}
	public Calendar getMaterialsQitaoTime() {
		return materialsQitaoTime;
	}
	public void setMaterialsQitaoTime(Calendar materialsQitaoTime) {
		this.materialsQitaoTime = materialsQitaoTime;
	}
	public Set<ProduceAssembled> getAssembled() {
		return assembled;
	}
	public void setAssembled(Set<ProduceAssembled> assembled) {
		this.assembled = assembled;
	}
	public int getAssemblingNum() {
		return getAssemblingNum(null);
	}
	/**
	 * 
	 * @return 返回当前的在制数量，如果有3个工序，分别对应报工数量为10 12 13，则在制数量为13
	 */
	public int getAssemblingNum(Worker w) {
		int max = 0;
		for (OrderProcess op : this.ops) {
			int num = op.getReportNum(w);
			if (num > max) {
				max = num;
			}
		}
		//在制数量要减去已经完成的数量
		max = max - this.getAssembledNum(w);
		return max;
	}
	public int getAssembledNum() {
		return getAssembledNum(null);
	}
	/**
	 * 
	 * @return 返回当前装配完成的数量
	 */
	public int getAssembledNum(Worker w) {
		int num = 0;
		for (ProduceAssembled pa : this.assembled) {
			if (w != null && !w.getId().equals(pa.getWorker().getId())) {
				 continue;
			}
			num += pa.getAssembleNum();
		}
		return num;
	}
	public Set<ProduceTest> getProduceTest() {
		return produceTest;
	}
	public void setProduceTest(Set<ProduceTest> produceTest) {
		this.produceTest = produceTest;
	}
	public int getTestingNum() {
		return getTestingNum(null);
	}
	/**
	 * 返回验收在验数量
	 * @return
	 */
	public int getTestingNum(Worker w) {
		int num = 0;
		for (ProduceTest pt : this.produceTest) {
			if (w != null && !w.getId().equals(pt.getWorker().getId())) {
				 continue;
			}
			if (pt.getStatus() == ProduceTest.ON_PROCESS) {
				num += pt.getTestNum();
			}
		}
		return num;
	}
	public int getTestedNum() {
		return getTestedNum(null);
	}
	/**
	 * 返回验收完成的数量
	 * @return
	 */
	public int getTestedNum(Worker w) {
		int num = 0;
		for (ProduceTest pt : this.produceTest) {
			if (w != null && !w.getId().equals(pt.getWorker().getId())) {
				 continue;
			}
			if (pt.getStatus() == ProduceTest.FINISHED) {
				num += pt.getTestNum();
			}
		}
		return num;
	}
	public Set<ProduceDianshi> getProduceDianshi() {
		return produceDianshi;
	}
	public void setProduceDianshi(Set<ProduceDianshi> produceDianshi) {
		this.produceDianshi = produceDianshi;
	}
	public int getDianshiing() {
		return getDianshiing(null);
	}
	//返回再典数量
	public int getDianshiing(Worker w) {
		int num = 0;
		for (ProduceDianshi pd : this.produceDianshi) {
			if (w != null && !w.getId().equals(pd.getWorker().getId())) {
				 continue;
			}
			if (pd.getStatus() == ProduceTest.ON_PROCESS) {
				num += pd.getTestNum();
			}
		}
		return num;
	}
	public int getDianshied() {
		return getDianshied(null);
	}
	//返回典试完成数量
	public int getDianshied(Worker w) {
		int num = 0;
		for (ProduceDianshi pd : this.produceDianshi) {
			if (w != null && !w.getId().equals(pd.getWorker().getId())) {
				 continue;
			}
			if (pd.getStatus() == ProduceTest.FINISHED) {
				num += pd.getTestNum();
			}
		}
		return num;
	}
	public Set<ProduceOther> getProduceother() {
		return produceother;
	}
	public void setProduceother(Set<ProduceOther> produceother) {
		this.produceother = produceother;
	}
	public String getKe2produceNum() {
		return ke2produceNum;
	}
	public void setKe2produceNum(String ke2produceNum) {
		this.ke2produceNum = ke2produceNum;
	}
	public String getKe2DianshiNum() {
		return ke2DianshiNum;
	}
	public void setKe2DianshiNum(String ke2DianshiNum) {
		this.ke2DianshiNum = ke2DianshiNum;
	}
	public User getProcessChecker() {
		return processChecker;
	}
	public void setProcessChecker(User processChecker) {
		this.processChecker = processChecker;
	}
	public User getMaterialChecker() {
		return materialChecker;
	}
	public void setMaterialChecker(User materialChecker) {
		this.materialChecker = materialChecker;
	}
	public String getBanzu() {
		return banzu;
	}
	public void setBanzu(String banzu) {
		this.banzu = banzu;
	}
	public Byte getAssignStatus() {
		return assignStatus;
	}
	public void setAssignStatus(Byte assignStatus) {
		this.assignStatus = assignStatus;
	}
	
}
