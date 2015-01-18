package cn.edu.ustb.sem.schedule.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.cons.OrderStatus;
import cn.edu.ustb.sem.core.cons.ProcessGroupType;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.pagination.PageUtil;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.core.util.TreeUtil;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.order.dao.OrderDao;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.produce.dao.ProduceTinggongDao;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;
import cn.edu.ustb.sem.schedule.dao.DispatchUnitDao;
import cn.edu.ustb.sem.schedule.dao.GroupProductCodeDao;
import cn.edu.ustb.sem.schedule.dao.GroupUnitProcessDao;
import cn.edu.ustb.sem.schedule.dao.ProcessGroupWorkerDao;
import cn.edu.ustb.sem.schedule.dao.ScheduleResultDao;
import cn.edu.ustb.sem.schedule.dao.UnscheduleProcessGroupDao;
import cn.edu.ustb.sem.schedule.dao.WorkerDao;
import cn.edu.ustb.sem.schedule.dao.WorkerTimelineDao;
import cn.edu.ustb.sem.schedule.entity.GroupProcessTree;
import cn.edu.ustb.sem.schedule.entity.GroupProcessTree.GroupProcessNode;
import cn.edu.ustb.sem.schedule.entity.DispatchUnit;
import cn.edu.ustb.sem.schedule.entity.GroupProductCode;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcessType;
import cn.edu.ustb.sem.schedule.entity.ProcessGroupWorker;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
import cn.edu.ustb.sem.schedule.entity.TimeLineHelper;
import cn.edu.ustb.sem.schedule.entity.UnscheduleProcessGroup;
import cn.edu.ustb.sem.schedule.entity.UnscheduleProcessGroupStatus;
import cn.edu.ustb.sem.schedule.entity.UnscheduleProcessGroupType;
import cn.edu.ustb.sem.schedule.entity.Worker;
import cn.edu.ustb.sem.schedule.entity.WorkerTimeLine;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;
import cn.edu.ustb.sem.schedule.service.ScheduleService;
import cn.edu.ustb.sem.schedule.web.model.GanttModel;
@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
	protected final Log logger = LogFactory.getLog(getClass());
	//一天工作8个小时
	public static final int HOUR_OF_A_DAY = 8;
	@Autowired
	private OrderService orderService;
	@Autowired
	private GroupProductCodeDao gpc;
	@Autowired
	private GroupUnitProcessDao gupd;
	@Autowired
	private WorkerDao workerDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private WorkerTimelineDao tlDao;
	@Autowired
	private UnscheduleProcessGroupDao upgDao;
	@Autowired
	private ProcessGroupWorkerDao pgwDao;
	@Autowired
	private ScheduleResultDao srDao;
	@Autowired
	private ProduceTinggongDao ptDao;
	@Autowired
	private UnscheduleProcessGroupDao unsrDao;
	@Autowired
	private ScheduleResultService scheduleResultService;
	@Autowired
	private DispatchUnitDao duDao;
	
	//输入：总工时，每天按8小时算，一周算5天
	// 传入orderIds字符串，返回排产成功的List<Integer> orderIds
	@Override
	public void autoSche(int totalDay, List<Integer> orderIds, Map<String, Object> scheResult) throws ServiceException {
		List<Integer> successOrdersId = new ArrayList<Integer>();
		//第一步，选出所有符合自动排产条件的订单
		List<Order> orders = orderService.findCanAutoScheduleOrder(orderIds);
		List<Order> successOrders = new ArrayList<Order>();
		Map<Integer, Integer> groupOrderMap = new HashMap<Integer, Integer>();
		//第二步，找到每个订单的产品族
		for (Order o : orders) {
			String pc = o.getProductCode();
			if (pc == null || pc.isEmpty()) throw new ServiceException("订单" + o.getNo() + "没有对应的产品代号，请先为其绑定产品代号。");
			GroupProductCode gProductCode = gpc.findGid(pc);
			if (gProductCode == null) throw new ServiceException("订单" + o.getNo() + "，产品代号" + pc + "，此产品代号没有对应工序组，无法排产。");
			groupOrderMap.put(o.getId(), gProductCode.getGroupId());
			successOrdersId.add(o.getId());
			successOrders.add(o);
		}
		//第三步 按不同优先级排序订单
		//按照前端传过来的id进行排产，其排序包含了时间先后顺序和用户自定义的顺序
//		step3Sort(successOrders);
		//取出全局timeline
		List<WorkerTimeLine> timelines = this.tlDao.listAll(new WorkerTimeLine(), -1, -1);
		if (timelines == null) throw new ServiceException("没有工人生产能力用于排产，请新建工人账户。");
		Map<Integer, WorkerTimeLine> wtl = new HashMap<Integer, WorkerTimeLine>(timelines.size());
		for (WorkerTimeLine tl : timelines) {
//					WorkerTimeLine newTl = tl.clone();
//					if (tl.getStart() == null) tl.setStart(Calendar.getInstance());
			//设置结束时间为开始时间
			tl.setCursor((Calendar)tl.getStart().clone());
			//默认8小时
			tl.setDuration(HOUR_OF_A_DAY);
			wtl.put(tl.getWorker().getId(), tl);
		}
		//第四步 进行排产
		step4Schedule(successOrders, groupOrderMap, totalDay, timelines, wtl);
		List<Order> isScheduledOrder = new ArrayList<Order>();
		for (Order o : successOrders) {
			if (o.isScheduled || o.isWaixie) isScheduledOrder.add(o);
		}
		//成功保存的订单
		scheResult.put("orders", isScheduledOrder);
		scheResult.put("timelines", timelines);
	}
	/**
	 * 
	 * @param orders 成功排产的订单
	 * @param timelines 每个员工的时间流
	 * @param srs 所有的排产结果
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveScheduleResult(Map<String, Object> scheResutl, Integer userId) {
		if (scheResutl == null) return;
		List<Order> orders = (List<Order>) scheResutl.get("orders");
		List<WorkerTimeLine> timelines = (List<WorkerTimeLine>) scheResutl.get("timelines");

		//第五步，更新订单的排产人id
		User u = new User(userId);
		for (Order o : orders) {
			//保存所有未排产的工序组
			saveUnscheduleProcessGroup(o);
			//保存所有的排产结果
			for (ScheduleResult sr : o.tempSrs) {
				sr.setStatus(ScheduleResult.COMMIT);
				this.srDao.save(sr);
			}
			if (!o.isWaixie) {
				//如果订单中没有外协工序组
				o.isScheduled = true;
				o.setStatus(OrderStatus.SCHEDULED.getIndex());
			} else {
				o.setStatus(OrderStatus.WAIXIE.getIndex());
			}
			
			o.setScheduler(u);
			orderDao.merge(o);
		}
		//更新每个工人的timeline
		for (WorkerTimeLine tl : timelines) {
			tl.setStart(tl.getCursor());
			tlDao.update(tl);
		}
	}
	
	private void step3Sort(List<Order> orders) {
		//第三步，按照时间先后顺序，对所有产品进行排序
		Collections.sort(orders, new Comparator<Order>(){
			@Override
			public int compare(Order o1, Order o2) {
				//已完成时间为标准，完成时间再前面的先排
				//如果完成时间相同，上线时间在前面的先排
				Calendar o1F = o1.getFinishDate();
				Calendar o2F = o2.getFinishDate();
				if (o1F.equals(o2F)) {
					Calendar o1S = o1.getOnlineDate();
					Calendar o2S = o2.getOnlineDate();
					return o1S.before(o2S) ? 1 : -1;
				}
				return o1F.before(o2F) ? 1 : -1;
			}
		});
	}
	/**
	 * 
	 * @param orders 需要进行排产的订单
	 * @param groupOrderMap 工序组和订单的映射
	 * @param totalDay 排产的时长，例如1周
	 * @param timelines 所有工人的时间轴
	 * @param wtl 员工id和该员工时间轴的映射
	 * @throws ServiceException
	 */
	private void step4Schedule(List<Order> orders, Map<Integer, Integer> groupOrderMap, Integer totalDay, 
			List<WorkerTimeLine> timelines, Map<Integer, WorkerTimeLine> wtl) throws ServiceException {
		//第四步，排产
		for (Order o : orders) {
			boolean isFull = true;
			for (WorkerTimeLine tl : timelines) {
				//只要有一个人的工作未排满totalDay天，则不停止排产
				if (tl.getDayCount() < totalDay) {
					isFull = isFull & false;
					break;
				}
			}
			if (isFull) {
				o.isScheduled = false;
				continue;
			}
			o.tempSrs = new HashSet<ScheduleResult>();
			//提取产品族信息
			Integer groupId = groupOrderMap.get(o.getId());
			if (groupId == null) throw new ServiceException("异常订单编号" + o.getNo() + "：没有对应的工序组信息，请确认其产品代号是否有对应的工序组。");
			//根据产品族信息提取所有的工序组
			GroupUnitProcess model = new GroupUnitProcess();
			model.setGroupId(groupId);
			List<GroupUnitProcess> ogs = gupd.listAll(model, -1, -1);
			if (ogs == null || ogs.isEmpty()) throw new ServiceException("异常订单编号" + o.getNo() + "：没有对应的工序组信息，请确认其产品代号是否有对应的工序组。");
			//将工序组排序，也就是只能按顺序进行排产----------一个产品族可能会对应多个生成单元
			Collections.sort(ogs, new Comparator<GroupUnitProcess>(){
				@Override
				public int compare(GroupUnitProcess o1, GroupUnitProcess o2) {
					return o1.getProcessGroup() - o2.getProcessGroup();
				}});
			//对应每一道工序组
			GroupProcessTree tree = TreeUtil.generateProcessGroupTree(ogs, o);
			o.tree = tree;
			//遍历每一棵分支
			for (GroupProcessNode gup : tree.leaves) {
				iterateProcessTree(gup, wtl, totalDay);
			}
			//如果没有外协工序组，则订单被排产完毕
			if (!o.isWaixie) o.isScheduled = true;
			//保存所有未排产的工序组
//			saveUnscheduleProcessGroup(tree, o);
		}
		//更新每个工人的timeline
//		for (WorkerTimeLine tl : timelines) {
//			tl.setStart(tl.getCursor());
//			tlDao.update(tl);
//		}
	}
	
	/**
	 * 遍历一棵分支
	 * @param leaf
	 * @throws ServiceException 
	 */
	private void iterateProcessTree(GroupProcessNode leaf, Map<Integer, WorkerTimeLine> wtlMap, int totalDay) throws ServiceException {
		do {
			//如果该节点有多个前序节点，而且至少有一个前序节点没有进入排产状态，则该节点不能排产
			Set<GroupProcessNode> beforeNodes = leaf.before;
			if (beforeNodes != null) {
				for (GroupProcessNode g : beforeNodes) {
					if (!g.isScheduled) return;
				}
			}
			//如果该节点已经排产过了，则跳过（在排特殊工序组的时候会把别的工序组拿过来排产）
			if (leaf.isScheduled) return;
			//提取能完成这个工序组的员工timeline
			Set<ProcessGroupWorker> pgws = leaf.gup.getPgw();
			//外协工序组和特殊工序组忽略排产
			if (leaf.gup.getType() == GroupUnitProcessType.WAIXIE) {
				return;
			} else if (leaf.gup.getType() == GroupUnitProcessType.SPEC) {
				
			} else {
				if (pgws == null || pgws.isEmpty()) throw new ServiceException("异常订单编号" + leaf.tree.order.getNo() + "：没有工人可以完成" + leaf.gup.getGroupName() + "的工序组" + leaf.gup.getProcessGroup());
			}
			//工人生产工序组的生产能力
			Map<Integer, ProcessGroupWorker> wgMap = new HashMap<Integer, ProcessGroupWorker>(pgws.size());
			//所有能生产该工序组的员工时间轴
			List<WorkerTimeLine> wtls = new ArrayList<WorkerTimeLine>();
			for (ProcessGroupWorker pg : pgws) {
				Worker worker = pg.getWorker();
				//如果是外协工序组，则没有员工对应该工序组
				if (worker == null) {
					leaf.tree.order.isWaixie = true;
					continue;
				}
				Integer workerId = worker.getId();
				//由于可能指定生产单元，导致该员工没有指定的timeline，所以要跳过
				WorkerTimeLine wtl = wtlMap.get(workerId);
				if (wtl == null) continue;
				wtls.add(wtl);
				wgMap.put(workerId, pg);
			}
			if (wtls.isEmpty()) throw new ServiceException("异常订单编号" + leaf.tree.order.getNo() + "：没有工人可以完成" + leaf.gup.getGroupName() + "的工序组" + leaf.gup.getProcessGroup());
			TimeLineHelper helper = new TimeLineHelper(wtls);
			leaf.helper = helper;
			Integer type = leaf.gup.getType();
			switch (type) {
				case ProcessGroupType.WAI_XIE: {
					//如果是外协工序组，该分支的排产结束
					return;
				}
				case ProcessGroupType.SPECIAL: {
					//如果是特殊工序组，则把别的分支未进行排产的工序组拿来排产
					//先忽略特殊工序组的情况
//					specialProcessGroupDispatch(leaf, wtlMap, o, totalDay, wgMap, wtls);
					commonProcessGroupDispatch(leaf, totalDay, wgMap);
					break;
				}
				case ProcessGroupType.COMMON: {
					commonProcessGroupDispatch(leaf, totalDay, wgMap);
					break;
				}
				default: {
					throw new ServiceException(leaf.gup.getGroupName() + "，工序组" + leaf.gup.getProcessGroup() + "工序组类型有误");
				}
			}
			//如果没有后序工序组，则退出
			if (leaf.after == null) break;
			leaf = leaf.after;
		} while (true);
	}
	/**
	 * 后续再做
	 * @param leaf
	 * @param wtlMap
	 * @param o
	 * @param totalDay
	 * @param wgMap
	 * @param wtls
	 * @throws ServiceException
	 */
	@SuppressWarnings("unused")
	private void specialProcessGroupDispatch(GroupProcessNode leaf, Map<Integer, WorkerTimeLine> wtlMap, Order o, int totalDay
			, Map<Integer, ProcessGroupWorker> wgMap, List<WorkerTimeLine> wtls, List<ScheduleResult> srs) throws ServiceException {
		List<GroupProcessNode> nodes = leaf.tree.leaves;
		for (GroupProcessNode node : nodes) {
			do {
				int type = node.gup.getType();
				switch (type) {
					case ProcessGroupType.WAI_XIE: {
						//如果是外协工序组，该分支的排产结束
						return;
					}
					case ProcessGroupType.SPECIAL: {
						//如果是特殊工序组，且已经排产过来，则可以选择下一个工序组
						if (node.isScheduled) break;
						//否则，这条分支结束排产
						return;
					}
					case ProcessGroupType.COMMON: {
//						commonProcessGroupDispatch(node, o, totalDay, wgMap, wtls);
						break;
					}
					default: {
						throw new ServiceException("工序组类型有误");
					}
				}
				if (node.after == null) break;
				node = node.after;
			} while (true);
		}
	}
	
	/**
	 * 正常工序组排产
	 * @param leaf
	 * @param wtlMap
	 * @param o
	 * @param totalDay
	 * @param wgMap
	 * @param wtls
	 * @throws ServiceException
	 */
	private void commonProcessGroupDispatch(GroupProcessNode leaf, int totalDay
			, Map<Integer, ProcessGroupWorker> wgMap) throws ServiceException {
		TimeLineHelper helper = leaf.helper;
		Order o = leaf.tree.order;
		//计算工时，排产
		Integer produceNum = o.getProduceNum();
		Integer originNum = o.getProduceNum();
		if (produceNum == null || produceNum == 0) throw new ServiceException("订单" + o.getNo() + "的投产数量不能为0");
		Calendar begin = null, end = null;
		do {
			//选择最早的能进行生产的员工来排产，每次排一个人一天的工作量
//			WorkerTimeLine earliest = getEarliestWorkerTimeLine(wtls, totalDay);
			if (o.unit == null) // 如果还没有为订单选择一个生产单元，则
				o.unit = helper.findTheUnit();
			WorkerTimeLine earliest = helper.getEarliestWorkerTimeLine(o.unit);
			//保存工序组的开始时间
			if (produceNum == originNum) begin = earliest.getCursor();
			Worker worker = earliest.getWorker();
			//计算该员工生产该工序组，一天能生产多少个元件
			ProcessGroupWorker pgw = wgMap.get(worker.getId());
			Integer base = pgw.getBase();
			produceNum = produceNum - base;
			if (produceNum < 0) {
				base = base + produceNum;
				dispatchToAWorker(earliest, o, base, leaf);
				//保存工序组的结束时间
				end = earliest.getCursor();
				break;
			} else {
				dispatchToAWorker(earliest, o, base, leaf);
			}
		} while (true);
		leaf.isScheduled = true;
		leaf.begin = begin;
		leaf.end = end;
	}
	
	//将一个工序组排到指定的工人上
	private void dispatchToAWorker(WorkerTimeLine earliest, Order o, int base, GroupProcessNode leaf) {
		//排产
		Calendar cur = getEariestBeginDate(leaf, earliest);
		//2.生成排产结果
		ScheduleResult sr = new ScheduleResult();
		sr.setStart((Calendar)cur.clone());
		sr.setWorker(earliest.getWorker());
		sr.setGup(leaf.gup);
		sr.setOrder(o);
		sr.setNum(base);
		sr.setDuration(HOUR_OF_A_DAY);
		//3.保存到数据库 --> 这里先不要保存到数据库，等用户确认排产后才能保存入库
//		srDao.save(sr);
		o.tempSrs.add(sr);
	}
	/**
	 * 获取该工序组最早开工时间
	 * @param leaf 工序组节点
	 * @param workerEarliest 员工最早开工时间
	 * @return
	 */
	private Calendar getEariestBeginDate(GroupProcessNode leaf, WorkerTimeLine workerEarliest) {
		//对比上一个工序组的排产结束时间，前序工序组做完了，后序的工序组才能开工，不能同一天开工
		Calendar max = null;
		for (GroupProcessNode gpn : leaf.before) {
			if (max == null) max = gpn.end;
			if (gpn.end.after(max)) max = gpn.end;
		}
		if (max == null) max = workerEarliest.getCursor();
		Calendar cur = null;
		if (workerEarliest.getCursor().after(max)) {
			cur = workerEarliest.getCursor();
		} else {
			cur = max;
		}
		//找到最早的开工时间
		//1.增加员工的负荷
		int day = cur.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.SATURDAY) {
			//如果是周六
			cur.add(Calendar.DATE, 3);
		} else if (day == Calendar.SUNDAY) {
			//如果是周日
			cur.add(Calendar.DATE, 2);
		} else {
			cur.add(Calendar.DATE, 1);
		}
		workerEarliest.dayCount += 1;
		return cur;
	}
	
	/**
	 * 从N个员工的时间轴中，找到能最早开工的员工时间轴
	 * @param wtls
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unused")
	private WorkerTimeLine getEarliestWorkerTimeLine(List<WorkerTimeLine> wtls, int totalDay) throws ServiceException {
		if (wtls == null || wtls.size() == 0) throw new ServiceException("排产出错，缺少员工的工作负荷信息");
		//按时间指针进行排序，最早的排前面
		Collections.sort(wtls, new Comparator<WorkerTimeLine>() {
			@Override
			public int compare(WorkerTimeLine o1, WorkerTimeLine o2) {
				return o1.getCursor().compareTo(o2.getCursor());
			}});
		for (int i = 0; i < wtls.size(); i++) {
			WorkerTimeLine wtl = wtls.get(i);
			//如果该员工的时间已经排满，继续往后排，不能中断排产，必须将订单排完
			if (wtl.getDayCount() >= totalDay) {
				if (i == wtls.size() - 1) return wtl;
				continue;
			}
			return wtl;
		}
		throw new ServiceException("系统出错--排产--获取生产负荷能力");
	}
	/**
	 * 保存因外协而导致无法排产的工序组
	 * @param gup
	 */
	private void saveUnscheduleProcessGroup(Order o) {
		List<GroupProcessNode> gup = o.tree.leaves;
		for (GroupProcessNode g : gup) {
			do {
				if (g.isScheduled || g.isSaveUnScheduledState) {
					//如果工序组已经排产了或者已经保存过未排产状态
					if (g.after == null) break;
					g = g.after;
				} else {
					//如果该工序组是外协工序组，则只保存它后面的工序组
					if (g.gup.getType().equals(GroupUnitProcessType.WAIXIE)) {
						if (g.after == null) break;
						g = g.after;
						continue;
					}
					UnscheduleProcessGroup upg = new UnscheduleProcessGroup();
					upg.setGup(g.gup);
					upg.setType(UnscheduleProcessGroupType.WAIEXIE);
					upg.setOrder(o);
					upg.setStatus(UnscheduleProcessGroupStatus.INITIAL);
					upg.setUnit(o.unit);
					upgDao.saveOrUpdate(upg);
					g.isSaveUnScheduledState = true;
					if (g.after == null) break;
					g = g.after;
				}
			} while (true);
		}
	}
	
	/**
	 * @author caiwenming
	 * 强制排产
	 */
	@Override
	public void addToSchedule(String ids) throws ServiceException {
		String[] idArr = ids.split(",");
		for(int i = 0; i < idArr.length; i ++){
			int id = Integer.parseInt(idArr[i]);
			Order order = this.orderService.get(id);
			order.setStatus(OrderStatus.FORCESCHEDULE.getIndex());
			this.orderService.saveOrUpdate(order);
		}
	}
	
	/**
	 * @author caiwenming
	 * 获取自动排产订单
	 * @param pn
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	private Page<Order> listAutoScheduleOrder() throws ServiceException{
		Integer totalCount = this.orderService.getAutoScheduleOrderCount();
		List<Order> items = this.orderService.getAutoScheduleOrder(-1, -1);
		return PageUtil.getPage(totalCount, -1, items, totalCount);
	}
	
	@Override
	public GridModel<OrderModel> getAutoScheduleOrder() throws ServiceException{
		ItemModelHelper<Order, OrderModel> helper =new ItemModelHelper<Order, OrderModel>() {
			@Override
			public OrderModel transfer(Order bo) {
				return new OrderModel(bo);
			}
		};
		GridModel<OrderModel> grid = new GridModel<OrderModel>();
		Page<Order> pages = this.listAutoScheduleOrder();
		grid.setTotalNum(pages.getContext().getTotal());
		List<Order> items = pages.getItems();
		List<OrderModel> itModel = new ArrayList<OrderModel>();
		for(Order o : items){
			itModel.add(helper.transfer(o));
		}
		grid.setItems(itModel);
		return grid;
	}
	
	
	
	/**
	 * 获取用户选择的订单列表
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	private List<Order> listSelected(String ids) throws ServiceException{
		String[] idArr = ids.split(",");
		List<Order> orders = new ArrayList<Order>();
		for(String str : idArr){
			int id = Integer.parseInt(str);
			Order order = this.orderService.get(id);
			orders.add(order);
		}
		return orders;
	}
	
	@Override
	public GridModel<OrderModel> selectedGrid(String ids)
			throws ServiceException {
		ItemModelHelper<Order, OrderModel> helper = new ItemModelHelper<Order, OrderModel>() {
			
			@Override
			public OrderModel transfer(Order bo) {
				return new OrderModel(bo);
			}
		};
		GridModel<OrderModel> grid = new GridModel<OrderModel>();
		List<Order> orders = this.listSelected(ids);
		this.step3Sort(orders);
		List<OrderModel> orderModels = new ArrayList<OrderModel>();
		for(Order order : orders){
			OrderModel orderModel = helper.transfer(order);
			orderModels.add(orderModel);
		}
		grid.setTotalNum(orderModels.size());
		grid.setItems(orderModels);
		return grid;
	}
	@Override
	public void waixieRestart(Integer orderId, Integer userId)
			throws ServiceException {
		if (orderId == null) throw new ServiceException("订单id不能为空");
		Order order = this.orderService.get(orderId);
		if (order == null) throw new ServiceException("订单不存在");
		//如果订单处于确认停工状态。不能外协重启
		if (ptDao.isTinggong(orderId))
			throw new ServiceException("处于停工状态的订单不能进行外协重启");
		//1.提取改订单没有排产的工序组，在unschedule_process_group表中
		List<UnscheduleProcessGroup> upgs = getUnscheduleWaixieProcessGroup(orderId);
		//2.构造订单的工序组树(只包含外协后面的工序组)
		List<GroupUnitProcess> gups = new ArrayList<GroupUnitProcess>(upgs.size());
		for (UnscheduleProcessGroup upg : upgs) {
			gups.add(upg.getGup());
			//在排产前先把这些未排产的工序组状态给改了
			upg.setStatus(UnscheduleProcessGroupStatus.WAIEXIE_RESTART);
			this.upgDao.merge(upg);
		}
		GroupProcessTree gpt = TreeUtil.generateProcessGroupTree(gups, order);
		//3.对工序组树进行排产		
		scheduleGroupProcessTree(order, gpt, userId);
	}
	/**
	 * 获取处于外协状态的工序组
	 * @param orderId
	 * @return
	 */
	private List<UnscheduleProcessGroup> getUnscheduleWaixieProcessGroup(Integer orderId) {
		UnscheduleProcessGroup model = new UnscheduleProcessGroup();
		model.setOrder(new Order(orderId));
		model.setType(UnscheduleProcessGroupType.WAIEXIE);
		model.setStatus(UnscheduleProcessGroupStatus.INITIAL);
		return this.upgDao.listAll(model, -1, -1);
	}
	/**
	 * 获取处于停工状态的工序组
	 * @param orderId
	 * @return
	 */
	private List<UnscheduleProcessGroup> getUnscheduleTinggongProcessGroup(Integer orderId) {
		UnscheduleProcessGroup model = new UnscheduleProcessGroup();
		model.setOrder(new Order(orderId));
		model.setType(UnscheduleProcessGroupType.TINGGONG);
		model.setStatus(UnscheduleProcessGroupStatus.TINGGONG_BEGIN);
		return this.upgDao.listAll(model, -1, -1);
	}
	
	private void scheduleGroupProcessTree(Order o, GroupProcessTree gpt, Integer userId) throws ServiceException {
		//取出全局timeline
		List<WorkerTimeLine> timelines = this.tlDao.listAll(new WorkerTimeLine(), -1, -1);
		//在订单重启的时候，会指定生产单元，因此要过滤掉其他生产单元的timeline
		if (o.unit != null && !o.unit.equals("")) {
			List<WorkerTimeLine> newlines = new ArrayList<WorkerTimeLine>();
			for (WorkerTimeLine tl : timelines) {
				String unit = tl.getWorker().getUnit();
				if (unit.equals(o.unit)) {
					newlines.add(tl);
				}
			}
			timelines = newlines;
		}
		Map<Integer, WorkerTimeLine> wtl = new HashMap<Integer, WorkerTimeLine>(timelines.size());
		for (WorkerTimeLine tl : timelines) {
			//设置结束时间为开始时间
			tl.setCursor((Calendar)tl.getStart().clone());
			//默认8小时
			tl.setDuration(HOUR_OF_A_DAY);
			wtl.put(tl.getWorker().getId(), tl);
		}
		o.tempSrs = new HashSet<ScheduleResult>();
		o.tree = gpt;
		//遍历每一棵分支
		for (GroupProcessNode gup : o.tree.leaves) {
			iterateProcessTree(gup, wtl, 14);
		}
		//如果没有外协工序组，则订单被排产完毕
		if (!o.isWaixie) o.isScheduled = true;
		//保存订单的排产情况
		User u = new User(userId);
		//保存所有未排产的工序组
		saveUnscheduleProcessGroup(o);
		//保存所有的排产结果
		for (ScheduleResult sr : o.tempSrs) {
			sr.setStatus(ScheduleResult.COMMIT);
			this.srDao.save(sr);
		}
		if (!o.isWaixie) {
			//如果订单中没有外协工序组
			o.isScheduled = true;
			o.setStatus(OrderStatus.SCHEDULED.getIndex());
		} else {
			o.setStatus(OrderStatus.WAIXIE.getIndex());
		}
		//外协重启人
		o.setWaixieRestarter(u);
		orderDao.merge(o);
		//更新每个工人的timeline
		for (WorkerTimeLine tl : timelines) {
			tl.setStart(tl.getCursor());
			tlDao.update(tl);
		}
	}
	@Override
	public void tinggongCommit(Integer ptid) throws ServiceException {
		ProduceTinggong model = new ProduceTinggong(ptid);
		ProduceTinggong exist = this.ptDao.find(model);
		if (exist == null)
			throw new ServiceException("不存在该停工报工");
		exist.setTinggongStatus(ProduceTinggong.TINGGONG_COMMIT);
		Visitor v = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		exist.setTinggongCommiter(new User(v.getUid()));
		exist.setCommitDate(Calendar.getInstance());
		ptDao.merge(exist);
		//1.修改订单状态，让管理员能看到，当管理员确认停工是，进行重新排产，先释放工人生产能力，再把工序组保存起来
		//2.找到已排产的工序组，从暂停点开始，把后面（包括暂停点）的工序组放到挂起区，方便管理员重启的时候能够直接排产
		moveOtherScheduleResultToHandUpZone(exist);		
	}
	
	private void moveOtherScheduleResultToHandUpZone(ProduceTinggong pt) {
		ScheduleResult sr = pt.getSr();
		//要保留好以前的生产单元，在重启的时候排产到以前的生产单元
		String unit = sr.getWorker().getUnit();
		//获得改工序组的id
		Integer gupId = sr.getGup().getId();
		Set<ScheduleResult> srs = sr.getOrder().getSrs();
		//去重得到所有的工序组
		Set<Integer> gupIds = new HashSet<Integer>();
		List<GroupUnitProcess> gups = new ArrayList<GroupUnitProcess>();
		for (ScheduleResult srrr : srs) {
			GroupUnitProcess gup = srrr.getGup();
			if (gupIds.contains(gup.getId())) continue;
			gupIds.add(gup.getId());
			gups.add(gup);
		}
		//构造工序组树
		GroupProcessTree tree = TreeUtil.generateProcessGroupTree(gups, sr.getOrder());
		//遍历所有子树，把后面的工序组找出来
		Set<Integer> gupNeedToHandup = new HashSet<Integer>();
		for (GroupProcessNode gupNode : tree.leaves) {
			boolean isFind = false;
			do {
				if (gupNode.gup.getId() == gupId) {
					//找到该工序组，把它后面的工序组都拿出来
					gupNeedToHandup.add(gupId);
					do {
						gupNeedToHandup.add(gupNode.gup.getId());
						if (gupNode.after == null) break;
						gupNode = gupNode.after;
					} while (true);
					isFind = true;
				}
				//如果没有后序工序组，则退出
				if (gupNode.after == null) break;
				gupNode = gupNode.after;
			} while (true);
			//因为一次报停工只会报一个工序组，所以找到后直接退出
			if (isFind) break;
		}
		//把排产区已排产的工序组移到挂起区
		this.srDao.deleteByGupidsAndOrderId(gupNeedToHandup, sr.getOrder().getId());
		for (Integer gupid : gupNeedToHandup) {
			UnscheduleProcessGroup unsr = new UnscheduleProcessGroup();
			unsr.setGup(new GroupUnitProcess(gupid));
			unsr.setOrder(sr.getOrder());
			unsr.setType(UnscheduleProcessGroupType.TINGGONG);
			unsr.setStatus(UnscheduleProcessGroupStatus.TINGGONG_BEGIN);
			unsr.setUnit(unit);
			this.unsrDao.save(unsr);
		}
	}
	@Override
	public void tinggongRestart(Integer ptid) throws ServiceException {
		ProduceTinggong pt = this.ptDao.get(ptid);
		if (pt == null) throw new ServiceException("该停工报工不存在，请联系管理员");
		Order order = pt.getOrder();
		//1.提取处于停工期间的工序组，在unschedule_process_group表中
		List<UnscheduleProcessGroup> upgs = getUnscheduleTinggongProcessGroup(order.getId());
		if (upgs != null && upgs.size() > 0) {
			order.unit = upgs.get(0).getUnit();
		}
		//2.构造订单的工序组树(只包含外协后面的工序组)
		List<GroupUnitProcess> gups = new ArrayList<GroupUnitProcess>(upgs.size());
		for (UnscheduleProcessGroup upg : upgs) {
			gups.add(upg.getGup());
			//在排产前先把这些未排产的工序组状态给改了
			upg.setStatus(UnscheduleProcessGroupStatus.TINGGONG_RESTART);
			this.upgDao.merge(upg);
		}
		GroupProcessTree gpt = TreeUtil.generateProcessGroupTree(gups, order);
		Visitor v = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		//3.对工序组树进行排产，重启的时候，要指定为以前的生成单元
		scheduleGroupProcessTree(order, gpt, v.getUid());
	
		//4.更新报工历史
		pt.setTinggongStatus(ProduceTinggong.TINGGONG_RESTART);
		pt.setTinggongRestarter(new User(v.getUid()));
		pt.setRestartDate(Calendar.getInstance());
		this.ptDao.merge(pt);
	}
	@Override
	public GridModel<OrderModel> getScheduleOrderBeforeDate(String date,
			String addIds) throws ServiceException {
		Calendar start;
		try {
			start = DateUtil.parseDate(date);
//			Set<Integer> orderIds = new HashSet<Integer>();
			StringBuilder sb = new StringBuilder();
			List<ScheduleResult> srs = this.srDao.getSRByDate(start, null);
			for (ScheduleResult sr : srs) {
				int status = sr.getStatus();
				if (status == ScheduleResult.INIT || status == ScheduleResult.CANCEL) {
					continue;
				}
				sb.append(sr.getOrder().getId()).append(",");
			}
			sb.append(addIds);
			return this.selectedGrid(sb.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	@Override
	public GanttModel adjustPlan(String ids, HttpServletRequest request) throws ServiceException {
		int duration = 365 * 2;
		List<Integer> orderIds = new ArrayList<Integer>();
		String[] strs = ids.split(",");
		for(String str : strs){
			int id = Integer.parseInt(str);
			orderIds.add(id);
		}
		//1.先把以前的排产结果删除掉 更新员工的timeline
		deleteScheduledOrder(orderIds, request);
		//2.重新排产
		try {
			Map<String, Object> scheResult = new HashMap<String, Object>();
			this.autoSche(duration, orderIds, scheResult);
			@SuppressWarnings("unchecked")
			List<Order> successOrder = (List<Order>) scheResult.get("orders");
			request.getSession(true).setAttribute("scheResult", scheResult);
			GanttModel data = this.scheduleResultService.getNewlyScheduleResult(successOrder);
			return data;
		} catch (ServiceException e) {
			logger.warn(e + "");
		}
		throw new ServiceException("系统异常");
	}
	
	private void deleteScheduledOrder(List<Integer> orderIds, HttpServletRequest request) {
		List<ScheduleResult> srs = this.srDao.findOrderSchedultResult(orderIds);
		//获取每个订单的最晚开始时间，把每个员工的timeline更新为该时间
		Map<Integer, Calendar> orderBeginDate = new HashMap<Integer, Calendar>();
		Set<Integer> srNeedToDelete = new HashSet<Integer>();
		for (ScheduleResult sr : srs) {
			int status = sr.getStatus();
			if (status == ScheduleResult.INIT || status == ScheduleResult.CANCEL) {
				continue;
			}
			srNeedToDelete.add(sr.getId());
			Integer oid = sr.getOrder().getId();
			Calendar begin = sr.getStart();
			if (orderBeginDate.containsKey(oid)) {
				if (begin.before(orderBeginDate.get(oid))) {
					orderBeginDate.put(oid, begin);
				}
			} else {
				orderBeginDate.put(oid, begin);
			}
		}
		//找到订单的最晚开始时间
		Calendar lastBeginDate = null;
		Set<Entry<Integer, Calendar>> entries = orderBeginDate.entrySet();
		for (Entry<Integer, Calendar> entry : entries) {
			if (lastBeginDate == null) {
				lastBeginDate = entry.getValue();
			} else if (lastBeginDate.before(entry.getValue())) {
				lastBeginDate = entry.getValue();
			}
		}
		//保存以前排产过的计划
		Collection<ScheduleResult> srEntityNeedToDelete = this.srDao.get(srNeedToDelete);
		//软删除
		Iterator<ScheduleResult> srsIter = srEntityNeedToDelete.iterator();
		Map<Integer, Integer> srsStatus = new HashMap<Integer, Integer>();
		while (srsIter.hasNext()) {
			ScheduleResult next = srsIter.next();
			srsStatus.put(next.getId(), next.getStatus());
			next.setStatus(ScheduleResult.ADJUST_DELETE);
			this.srDao.merge(next);
		}
		this.srDao.flush();
//		this.srDao.deleteAll(srNeedToDelete);
		request.getSession(true).setAttribute("srsStatus", srsStatus);
		//更新员工timeline
		List<WorkerTimeLine> wtls = this.tlDao.listAll();
		Map<Integer, Calendar> oldWorkerWTL = new HashMap<Integer, Calendar>();
		for (WorkerTimeLine wtl : wtls) {
			oldWorkerWTL.put(wtl.getId(), wtl.getStart());
			wtl.setStart(lastBeginDate);
			this.tlDao.merge(wtl);
		}
		this.tlDao.flush();
		request.getSession(true).setAttribute("oldWorkerWTL", oldWorkerWTL);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void cancelAdjust(HttpServletRequest request)
			throws ServiceException {
		HttpSession session = request.getSession(true);
		Map<Integer, Calendar> oldWorkerWTL = (Map<Integer, Calendar>) session.getAttribute("oldWorkerWTL");
		Map<Integer, Integer> srsStatus = (Map<Integer, Integer>) session.getAttribute("srsStatus");
		Set<Entry<Integer, Integer>> srs = srsStatus.entrySet();
		//恢复删掉掉的排产计划
		Iterator<Entry<Integer, Integer>> srsIter = srs.iterator();
		while (srsIter.hasNext()) {
			Entry<Integer, Integer> kv = srsIter.next();
			ScheduleResult sr = this.srDao.get(kv.getKey());
			sr.setStatus(kv.getValue());
			this.srDao.merge(sr);
		}
		//恢复被更改的员工timeline
		Iterator<Entry<Integer, Calendar>> wtlsIter = oldWorkerWTL.entrySet().iterator();
		while (wtlsIter.hasNext()) {
			Entry<Integer, Calendar> kv = wtlsIter.next();
			WorkerTimeLine wtl = this.tlDao.get(kv.getKey());
			wtl.setStart(kv.getValue());
			this.tlDao.merge(wtl);
		}
		session.removeAttribute("oldWorkerWTL");
		session.removeAttribute("srEntityNeedToDelete");
	}
	@Override
	public List<String> listAllUnit() throws ServiceException {
		List<Worker> workers = this.workerDao.listAll();
		Set<String> units = new HashSet<String>();
		for (Worker w : workers) {
			units.add(w.getUnit());
		}
		List<String> result = new ArrayList<String>(units.size());
		result.addAll(units);
		return result;
	}
	@Override
	public void dispatchUnit(Integer orderId, String unit, Integer userId)
			throws ServiceException {
		if (orderId == null || orderId <= 0) throw new ServiceException("订单id为非法值，订单id不能为空或者小于零。");
		if (unit == null || unit.isEmpty()) throw new ServiceException("生产单元不能为空");
		Order o = new Order(orderId);
		DispatchUnit du = new DispatchUnit();
		du.setUnit(unit);
		du.setOrder(o);
		du.setUdate(Calendar.getInstance());
		du.setDispatcher(new User(userId));
		this.duDao.save(du);
	}
}
