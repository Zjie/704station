package cn.edu.ustb.sem.order.web.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.assign.entity.Assign;
import cn.edu.ustb.sem.assign.web.model.AssignModel;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderMaterial;

public class OrderMaterialModel {
	private long id;
	private int orderNo;
	private String name;
	private String specification;
	private String level;
	private String uom;
	private double singleNum;
	private double bkNum;
	private String type;
	//添加订单物料配套情况---cwm
	private List<AssignModel> assigns;
	private double totalAssign;//已配套数量
	private double leftAssign;//剩余可配套数量
	private String udate;
	private String updater;
	//总需求量
	private double allNum;
	//差额
	private double diffNum;
	public OrderMaterialModel() {
	}

	public OrderMaterialModel(OrderMaterial bo) {//修改相应的构造函数---cwm
		this.id = bo.getId();
		this.name = bo.getName();
		this.specification = bo.getSpecification();
		this.level = bo.getLevel();
		this.uom = bo.getUom();
		this.singleNum = bo.getSingleNum()==null?0:bo.getSingleNum();
		this.bkNum = bo.getBackupNum()==null?0:bo.getBackupNum();
		this.type = bo.getType();
		List<AssignModel> ams=new ArrayList<AssignModel>();
		int total=0;
		Set<Assign> assignSet=bo.getAs();
		if(assignSet!=null){
			for(Assign assign:assignSet){
				AssignModel am = new AssignModel(assign);
				total += assign.getMatingNum();
				ams.add(am);
			}
		}
		this.assigns = ams;
		this.totalAssign = total;
		this.leftAssign = singleNum * bo.getOrder().getProduceNum() - total;
		this.udate = DateUtil.getDateTime(bo.getUdate());
		User updater = bo.getUpdater();
		if (updater != null) {
			this.updater = bo.getUpdater().getUserName();
		}
		Order order = bo.getOrder();
		if (order != null) {
			this.orderNo = order.getId();
		}
		this.allNum = order.getProduceNum() * this.singleNum;
		this.diffNum = this.allNum - this.totalAssign;
	}

	public double getBkNum() {
		return bkNum;
	}

	public long getId() {
		return id;
	}

	public String getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public double getSingleNum() {
		return singleNum;
	}

	public String getSpecification() {
		return specification;
	}

	public String getType() {
		return type;
	}

	public String getUdate() {
		return udate;
	}

	public String getUom() {
		return uom;
	}

	public String getUpdater() {
		return updater;
	}

	public void setBkNum(int bkNum) {
		this.bkNum = bkNum;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public void setSingleNum(int singleNum) {
		this.singleNum = singleNum;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AssignModel> getAssigns() {
		return assigns;
	}

	public void setAssigns(List<AssignModel> assigns) {
		this.assigns = assigns;
	}

	public double getTotalAssign() {
		return totalAssign;
	}

	public void setTotalAssign(int totalAssign) {
		this.totalAssign = totalAssign;
	}

	public double getLeftAssign() {
		return leftAssign;
	}

	public void setLeftAssign(int leftAssign) {
		this.leftAssign = leftAssign;
	}

	public void setUdate(String udate) {
		this.udate = udate;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public double getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public double getDiffNum() {
		return diffNum;
	}

	public void setDiffNum(int diffNum) {
		this.diffNum = diffNum;
	}
	
}
