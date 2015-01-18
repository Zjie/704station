package cn.edu.ustb.sem.order.web.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderProcess;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;
import cn.edu.ustb.sem.produce.web.model.ProduceAssembingModel;
import cn.edu.ustb.sem.schedule.entity.Worker;

public class OrderProcessModel {
	private long id;
	private String phase;
	private String content;
	private int groupName;
	private int measure;
	private String remark;
	private int base;
	private List<ProduceAssembingModel> produceReports;
	private int reportNum;//排产数量---cwm
	private int reportedNum;//已排产数量---cwm
	private int leftNum;//未排产数量---cwm
	private int reportTime;//已排产花费的时间---cwm
	private String udate;
	private String updater;
	
	//工序组排产开始时间
	private String beginDate;
	//工序组排产结束时间
	private String endDate;
	public OrderProcessModel() {
	}
	public OrderProcessModel(OrderProcess bo, Worker w, Calendar begin, Calendar end) {
		this.id = bo.getId();
		this.phase = bo.getPhase();
		this.content = bo.getContent();
		this.groupName = bo.getGroup();
		this.measure = bo.getMeasure();
		this.remark = bo.getRemark();
		this.base = bo.getBase() == null ? 0 : bo.getBase();
		this.udate = DateUtil.getDateTime(bo.getUdate());
		if (bo.getUpdater() != null)
			this.updater = bo.getUpdater().getUserName();
		Order order = bo.getOrder();
		this.reportNum = order.getProduceNum();
		List<ProduceAssembingModel> prms = new ArrayList<ProduceAssembingModel>();
		int total = 0;
		int hours = 0;
		Set<ProduceAssembling> prSet = bo.getPrs();
		if(prSet != null){
			for(ProduceAssembling pr :prSet){
				if (w != null && !w.getId().equals(pr.getWorker().getId())) {
					//如果不是这个工人的装配报工，则跳过
					continue;
				}
				if (end != null && pr.getUdate().after(end)) {
					//如果是报工时间之后的，忽略该报工
					continue;
				}
				if (begin != null && pr.getUdate().before(begin)) {
					//如果报工时间是查询时间之前的，忽略该报工
					continue;
				}
				ProduceAssembingModel prm = new ProduceAssembingModel(pr);
				total += pr.getReportNum();
				prms.add(prm);
				//计算每一道工序排产的总时间时间
				hours += pr.getReportTime();
			}
		}
		this.reportTime = hours;
		this.produceReports = prms;
		this.reportedNum = total;
		this.leftNum = reportNum - reportedNum;
	}
	public OrderProcessModel(OrderProcess bo) {
		this(bo, null, null, null);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getGroupName() {
		return groupName;
	}

	public void setGroupName(int groupName) {
		this.groupName = groupName;
	}

	public int getMeasure() {
		return measure;
	}

	public void setMeasure(int measure) {
		this.measure = measure;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public List<ProduceAssembingModel> getProduceReports() {
		return produceReports;
	}

	public void setProduceReports(List<ProduceAssembingModel> produceReports) {
		this.produceReports = produceReports;
	}

	public int getReportNum() {
		return reportNum;
	}

	public void setReportNum(int reportNum) {
		this.reportNum = reportNum;
	}

	public int getReportedNum() {
		return reportedNum;
	}

	public void setReportedNum(int reportedNum) {
		this.reportedNum = reportedNum;
	}

	public int getLeftNum() {
		return leftNum;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public int getReportTime() {
		return reportTime;
	}

	public void setReportTime(int reportTime) {
		this.reportTime = reportTime;
	}

	public String getUdate() {
		return udate;
	}

	public void setUdate(String udate) {
		this.udate = udate;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
