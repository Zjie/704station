package cn.edu.ustb.sem.schedule.entity;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import cn.edu.ustb.sem.order.entity.Order;

import com.alibaba.fastjson.JSON;

/**
 * 工序组生成树
 * @author zhoujie04
 *
 */
public class GroupProcessTree {
	public GroupProcessNode root;
	//叶子节点
	public List<GroupProcessNode> leaves;
	//订单
	public Order order;
	
	public static class GroupProcessNode {
		//前序节点
		public Set<GroupProcessNode> before;
		//后序节点
		public GroupProcessNode after;
		//是否进入排产状态
		public boolean isScheduled;
		public GroupUnitProcess gup;
		public GroupProcessTree tree;
		public TimeLineHelper helper;
		public int processGroup;
		//开始时间
		public Calendar begin;
		//结束时间
		public Calendar end;
		//是否保存了中止工序组状态
		public boolean isSaveUnScheduledState;
		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
