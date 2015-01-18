package cn.edu.ustb.sem.produce.web.model;

import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
import cn.edu.ustb.sem.schedule.web.model.GroupUnitProcessModel;

public class DispatchResultModel {
	public OrderModel order;
	public GroupUnitProcessModel gup;
	public Integer id;
	public Integer oid;
	public Integer gupId;
	public Integer status;
	public DispatchResultModel() {}
	public DispatchResultModel(ScheduleResult sr) {
		Order o = sr.getOrder();
		GroupUnitProcess gupr = sr.getGup();
		id = sr.getId();
		oid = o.getId();
		gupId = gupr.getId();
		this.order = new OrderModel(o);
		this.gup = new GroupUnitProcessModel(gupr);
		this.status = sr.getStatus();
	}
	public OrderModel getOrder() {
		return order;
	}
	public void setOrder(OrderModel order) {
		this.order = order;
	}
	public GroupUnitProcessModel getGup() {
		return gup;
	}
	public void setGup(GroupUnitProcessModel gup) {
		this.gup = gup;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	public Integer getGupId() {
		return gupId;
	}
	public void setGupId(Integer gupId) {
		this.gupId = gupId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
