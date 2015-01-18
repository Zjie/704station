package cn.edu.ustb.sem.kpi.web.model;

import java.util.Calendar;

/**
 * 订单的完工情况
 * @author zhoujie04
 *
 */
public class OrderProduceInfo {
	public int id;
	//生产单元
	public String unit;
	//订单编号
	public String orderNo;
	//投产数量
	public int produceNum;
	//完工数量，这个是从报工的信息中提取
	public int processNum;
	public String code;
	//完成率=完工数量/投产数量
	public String finishedRate;
	//生产状态
	//如果int(完成率)=int((当前时间-排产开始时间)/(排产结束时间-排产开始时间))，状态为正常
	//如果int(完成率)>int((当前时间-排产开始时间)/(排产结束时间-排产开始时间))，状态为超前完成，超前率=(前者-后者)/后者
	//如果int(完成率)<int((当前时间-排产开始时间)/(排产结束时间-排产开始时间))，状态为延期完成，延期率=(后者-前者)/后者
	//另外一些特别的状态，外协，停工
	public String status;
	//备注
	public String remark;
	
	public Calendar begin;
	public Calendar end;
	//差额
	public int diff;
	
	public int getDiff() {
		return diff;
	}
	public void setDiff(int diff) {
		this.diff = diff;
	}
	public Calendar getBegin() {
		return begin;
	}
	public void setBegin(Calendar begin) {
		this.begin = begin;
	}
	public Calendar getEnd() {
		return end;
	}
	public void setEnd(Calendar end) {
		this.end = end;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getProduceNum() {
		return produceNum;
	}
	public void setProduceNum(int produceNum) {
		this.produceNum = produceNum;
	}
	public int getProcessNum() {
		return processNum;
	}
	public void setProcessNum(int processNum) {
		this.processNum = processNum;
	}
	public String getFinishedRate() {
		return finishedRate;
	}
	public void setFinishedRate(String finishedRate) {
		this.finishedRate = finishedRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
