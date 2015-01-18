package cn.edu.ustb.sem.schedule.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.core.cons.Colors;
import cn.edu.ustb.sem.core.cons.DayOfWeek;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderProcessModel;
import cn.edu.ustb.sem.schedule.dao.GroupUnitProcessDao;
import cn.edu.ustb.sem.schedule.dao.ScheduleResultDao;
import cn.edu.ustb.sem.schedule.dao.UnscheduleProcessGroupDao;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
import cn.edu.ustb.sem.schedule.entity.ScheduleResultGup;
import cn.edu.ustb.sem.schedule.entity.UnscheduleProcessGroup;
import cn.edu.ustb.sem.schedule.entity.UnscheduleProcessGroupStatus;
import cn.edu.ustb.sem.schedule.entity.UnscheduleProcessGroupType;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;
import cn.edu.ustb.sem.schedule.web.model.DataTable;
import cn.edu.ustb.sem.schedule.web.model.DataTable.DataColumn;
import cn.edu.ustb.sem.schedule.web.model.DataTable.Text;
import cn.edu.ustb.sem.schedule.web.model.GanttModel;
import cn.edu.ustb.sem.schedule.web.model.GanttModel.Categories;
import cn.edu.ustb.sem.schedule.web.model.GanttModel.Category;
import cn.edu.ustb.sem.schedule.web.model.GanttModel.Chart;
import cn.edu.ustb.sem.schedule.web.model.GanttModel.Connectors;
import cn.edu.ustb.sem.schedule.web.model.Processes;
import cn.edu.ustb.sem.schedule.web.model.Processes.Process;
import cn.edu.ustb.sem.schedule.web.model.TaskWithTime;
import cn.edu.ustb.sem.schedule.web.model.Tasks;
import cn.edu.ustb.sem.schedule.web.model.Tasks.Task;

@Service("scheduleResultService")
public class ScheduleResultServiceImpl implements ScheduleResultService {

	public static final String DATEFORMAT = "dd/MM/yyyy";
	public static final SimpleDateFormat df = new SimpleDateFormat(DATEFORMAT);
	@Autowired
	private GroupUnitProcessDao gupDao;
	@Autowired
	private UnscheduleProcessGroupDao upgDao;
	
	@Autowired
	private ScheduleResultDao srDao;

	@Autowired
	private OrderService orderService;

	@Override
	public GanttModel getNewlyScheduleResult(List<Order> orderList)
			throws ServiceException {
		GanttModel ganttModel = new GanttModel();
		// 图表
		Chart chart = new Chart();
		chart.caption = "排产结果";
		// 时间维度
		List<Categories> categories = new ArrayList<Categories>();
		// 工序间的连线
		Connectors connectors = new Connectors();

		//甘特图的表格
		DataTable dataTable = new DataTable();
		
		dataTable.datacolumn = new ArrayList<DataColumn>();
		DataColumn startColumn = new DataColumn();
		startColumn.headertext = "开始\\n时间";
		List<Text> startTexts = new ArrayList<Text>();
		
		DataColumn endColumn = new DataColumn();
		endColumn.headertext = "结束\\n时间";
		List<Text> endTexts = new ArrayList<Text>();
		
		List<TaskWithTime> list = new ArrayList<TaskWithTime>();
		// 找出所有订单，以及其对应的所有工序
		
		int color = 1;//用颜色区分不同订单的颜色
		for (Order order : orderList) {
			String orderNo = order.getNo();
			// 获取订单的所有工序组，一个工序组可能由多个功能完成，则需要算出一个工序组的始末时间
			Set<ScheduleResult> results = order.tempSrs;
			if(results.size() == 0){
				continue;
			}
			// 获取一个订单的所有工序组，工序没有重复（即将重复工序叠加，算出一道工序的开始、结束时间）
			List<ScheduleResultGup> srgups = getBeginAndEnd(orderNo, results, color);
			// 将一个订单的工序组转换成Tasks
			TaskWithTime tasksForOneOrder = getTasks(srgups);
			list.add(tasksForOneOrder);
			color++;
		}
		
		// 生产单元，固定
		Processes processes = new Processes();
		processes.headertext = "订单\\n工序";
		processes.headerfontsize = "16";
		processes.fontsize = "12";
		List<Process> process = new ArrayList<>();
		
		//所有订单的所有工序组的最早开始时间
		String startTemp = null;
		//所有订单的所有工序组的最晚结束时间
		String endTemp = null;
		List<Task> taskList = new ArrayList<Task>();
		for (TaskWithTime t : list) {
			String s = t.getStart();
			String e = t.getEnd();
			if (startTemp == null && endTemp == null) {
				startTemp = s;
				endTemp = e;
			}
			try {
				startTemp = DateUtil.getBefore(s, startTemp, DATEFORMAT);
				endTemp = DateUtil.getAfter(e, endTemp, DATEFORMAT);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			taskList.addAll(t.getTasks());
			
			//设置甘特图的工序
			List<Process> processList = t.getProcesses();
			process.addAll(processList);
			
			//设置开始时间列和结束时间列
			startTexts.addAll(t.getStartTexts());
			endTexts.addAll(t.getEndTexts());
		}

		processes.process = process;
		startColumn.text = startTexts;
		endColumn.text = endTexts;
		dataTable.datacolumn.add(startColumn);
		dataTable.datacolumn.add(endColumn);
		// 时间维度总时长
		Categories categories1 = new Categories();
		categories1.category = new ArrayList<>();
		Category category = new Category();
		category.label = "日期";
		category.start = startTemp;
		category.end = endTemp;
		categories1.category.add(category);
		List<Category> categoriyList = new ArrayList<>();
		// 总时长细分
		try {
			categoriyList = generateDay(startTemp, endTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Categories categories2 = new Categories();
		categories2.category = categoriyList;
//		for (Category c : categoriyList) {
//			categories2.category.add(c);
//		}
		categories.add(categories1);
		categories.add(categories2);

		//生产tasks
		Tasks tasks = new Tasks();
		tasks.task = taskList;

		ganttModel.chart = chart;
		ganttModel.categories = categories;
		ganttModel.processes = processes;
		ganttModel.datatable = dataTable;
		ganttModel.tasks = tasks;
		ganttModel.connectors = connectors;
		return ganttModel;
	}

	// 获取所有工序组以及始末时间（没有重复工序）
	private List<ScheduleResultGup> getBeginAndEnd(String orderNo,
			Set<ScheduleResult> results, int color) {
		List<ScheduleResultGup> srGupList = new ArrayList<ScheduleResultGup>();

		// 获取所有工序组的id
		Set<Integer> gupIds = new HashSet<Integer>();
		for (ScheduleResult result : results) {
			gupIds.add(result.getGup().getId());
		}

		// 将所有工序组按id分组,最终得到一个工序组以及该工序组的开始时间
		for (Integer id : gupIds) {
			List<ScheduleResultGup> srGups = new ArrayList<ScheduleResultGup>();
			for (ScheduleResult result : results) {
				GroupUnitProcess gup = result.getGup();
				int gupId = gup.getId();
				if (id == gupId) {
					ScheduleResultGup srGup = new ScheduleResultGup();
					srGup.setOrderNo(orderNo);
					srGup.setGup(gup);
					srGup.setProcessGroup(gup.getProcessGroup());
					Calendar start = result.getStart();
					srGup.setBegin(start);
					srGup.setUnit(gup.getUnit());
					//计算工序组的结束时间
//					Calendar end = (Calendar) start.clone();
//					end.add(result.getDuration(), Calendar.DATE);
//					srGup.setEnd(end);
					srGups.add(srGup);
				}
			}
			// 按开始时间排序
			Collections.sort(srGups, new Comparator<ScheduleResultGup>() {
				@Override
				public int compare(ScheduleResultGup o1, ScheduleResultGup o2) {
					Calendar o1B = o1.getBegin();
					Calendar o2B = o2.getBegin();
					return o1B.before(o2B) ? -1 : 1;
				}
			});
			//该工序组的最早开工时间
			Calendar firstBeg = srGups.get(0).getBegin();
			/*
			Collections.sort(srGups, new Comparator<ScheduleResultGup>() {
				@Override
				public int compare(ScheduleResultGup o1, ScheduleResultGup o2) {
					Calendar o1B = o1.getEnd();
					Calendar o2B = o2.getEnd();
					return o1B.before(o2B) ? -1 : 1;
				}
			});
			*/
			//该工序组的最迟结束时间
			Calendar lastEnd = srGups.get(srGups.size() - 1).getBegin();
			
			ScheduleResultGup data = srGups.get(0);
			data.setBegin(firstBeg);
			data.setEnd(lastEnd);
			data.setColor(Colors.getName(color%10));
			srGupList.add(data);
		}
		// 按工序组前后排序
		Collections.sort(srGupList, new Comparator<ScheduleResultGup>() {
			@Override
			public int compare(ScheduleResultGup o1, ScheduleResultGup o2) {
				int o1B = o1.getProcessGroup();
				int o2B = o2.getProcessGroup();
				return o1B - o2B;
			}
		});
		return srGupList;
	}

	// 将工序组转换成task列表
	private TaskWithTime getTasks(List<ScheduleResultGup> srGups) {
		TaskWithTime time = new TaskWithTime();
		List<Task> tasks = new ArrayList<>();
		List<Process> processes = new ArrayList<>();
		List<Text> startTemp = new ArrayList<>();
		List<Text> endTemp = new ArrayList<>();
		
		for (ScheduleResultGup s : srGups) {
			String id = "o" + s.getOrderNo() + "—" + "g" + s.getProcessGroup();
			String label = "订单" + s.getOrderNo() + "—" + "工序组"
					+ s.getProcessGroup() + "—" + "生产单元" + s.getUnit();
			String start = df.format(s.getBegin().getTime());
			String end = df.format(s.getEnd().getTime());
			
			
			Task task = new Task();
			Process process = new Process();
			Text startText = new Text();
			Text endText = new Text();
			
			task.label = label;
			task.processid = id;
			task.id = id;
			task.start = start;
			task.end = end;
			task.color = s.getColor();
			task.height = "50%";
			task.toppadding = "25%";
			
			process.label = label;
			process.id = id;
			
			startText.label = start;
			endText.label = end;
			
			tasks.add(task);
			processes.add(process);
			startTemp.add(startText);
			endTemp.add(endText);
		}
		
		// 按开始时间排序
		Collections.sort(tasks, new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				Calendar o1B;
				try {
					o1B = DateUtil.parseDate(o1.start, DATEFORMAT);
					Calendar o2B = DateUtil.parseDate(o2.start, DATEFORMAT);
					return o1B.before(o2B) ? -1 : 1;
				} catch (ParseException e) {
					e.printStackTrace();
					return 1;
				}
			}
		});
		time.setStart(tasks.get(0).start);
		
		// 按结束时间排序
		Collections.sort(tasks, new Comparator<Task>() {
			@Override
			public int compare(Task o1, Task o2) {
				Calendar o1B;
				try {
					o1B = DateUtil.parseDate(o1.end, DATEFORMAT);
					Calendar o2B = DateUtil.parseDate(o2.end, DATEFORMAT);
					return o1B.before(o2B) ? 1 : -1;
				} catch (ParseException e) {
					e.printStackTrace();
					return 1;
				}
			}
		});
		time.setEnd(tasks.get(0).end);
		time.setTasks(tasks);
		time.setProcesses(processes);
		time.setStartTexts(startTemp);
		time.setEndTexts(endTemp);
		return time;
	}

	//以最前开始时间和最后结束时间为参数，生成时间“横坐标”，
	private List<Category> generateDay(String start, String end)
			throws ParseException {
		List<Category> categories = new ArrayList<>();
		Calendar cal1 = DateUtil.parseDate(start, DATEFORMAT);
		Calendar cal2 = DateUtil.parseDate(end, DATEFORMAT);
		Calendar cal3 = (Calendar) cal1.clone();
		while (cal3.before(cal2)) {
			Category category = new Category();
			category.label = DayOfWeek.getName(cal3.get(Calendar.DAY_OF_WEEK));
			category.start = df.format(cal3.getTime());
			cal3.add(Calendar.DATE, 1);
			category.end = df.format(cal3.getTime());
			categories.add(category);
		}
		return categories;
	}

	@Override
	public List<OrderModel> getWaixieOrder() throws ServiceException {
		UnscheduleProcessGroup model = new UnscheduleProcessGroup();
		model.setStatus(UnscheduleProcessGroupStatus.INITIAL);
		model.setType(UnscheduleProcessGroupType.WAIEXIE);
		List<UnscheduleProcessGroup> upgs = upgDao.listAll(model, -1, -1);
		List<OrderModel> orders = new ArrayList<OrderModel>();
		for (UnscheduleProcessGroup upg : upgs) {
			orders.add(new OrderModel(upg.getOrder()));
		}
		return orders;
	}

	@Override
	public GanttModel getScheduledResultByDate(String dateBegin, String dateEnd)
			throws ServiceException {
		Calendar beg = null;
		Calendar end = null;
		if (dateBegin == null || dateBegin.isEmpty() || dateEnd == null || dateEnd.isEmpty()) {
			//以最近一个月为
			end = Calendar.getInstance();
			beg = Calendar.getInstance();
			beg.add(Calendar.MONTH, -1);
		} else {
			try {
				beg = DateUtil.parseDate(dateBegin);
				end = DateUtil.parseDate(dateEnd);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<ScheduleResult> srs = this.srDao.getSRByDate(beg, end);
		Set<Integer> orderids = new HashSet<Integer>();
		List<Order> orders = new ArrayList<Order>();
		for (ScheduleResult sr : srs) {
			Order o = sr.getOrder();
			Integer oid = o.getId();
			if (orderids.contains(oid)) continue;
			orderids.add(oid);
			//只需要排产区中的工序组
			Set<ScheduleResult> tt = new HashSet<ScheduleResult>();
			for (ScheduleResult osr : o.getSrs()) {
				if (osr.getStatus() != ScheduleResult.COMMIT) continue;
				tt.add(osr);
			}
			o.tempSrs = tt;
			orders.add(o);
		}
		return this.getNewlyScheduleResult(orders);
	}

	@Override
	public Calendar getEarlistBeginDateTime(String groupName, Integer oid)
			throws ServiceException {
		List<ScheduleResult> srs = this.srDao.findOrderSchedultResult(oid);
		Calendar earlistBegin = null;
		for (ScheduleResult sr : srs) {
			int status = sr.getStatus();
			if (status == ScheduleResult.COMMIT || status == ScheduleResult.PRINTED || status == ScheduleResult.TINGGONG) {
				GroupUnitProcess gup = sr.getGup();
				if (!groupName.equals("" + gup.getProcessGroup())) continue;
				Calendar srBegin = sr.getStart();
				if (earlistBegin == null) {
					earlistBegin = srBegin;
				} else if (earlistBegin.after(srBegin)) {
					earlistBegin = srBegin;
				}
			}
		}
		return earlistBegin;
	}

	@Override
	public Calendar getLastEndDateTime(String groupName, Integer oid) throws ServiceException {
		List<ScheduleResult> srs = this.srDao.findOrderSchedultResult(oid);
		Calendar lastEnd = null;
		Calendar earlistBegin = null;
		for (ScheduleResult sr : srs) {
			int status = sr.getStatus();
			if (status == ScheduleResult.COMMIT || status == ScheduleResult.PRINTED || status == ScheduleResult.TINGGONG) {
				GroupUnitProcess gup = sr.getGup();
				if (!groupName.equals("" + gup.getProcessGroup())) continue;
				//只有开始时间，最后一个开始时间即为结束时间
				Calendar srEnd = sr.getStart();
				if (lastEnd == null) {
					lastEnd = srEnd;
				} else if (lastEnd.before(srEnd)) {
					lastEnd = srEnd;
				}
				Calendar srBegin = sr.getStart();
				if (earlistBegin == null) {
					earlistBegin = srBegin;
				} else if (earlistBegin.after(srBegin)) {
					earlistBegin = srBegin;
				}
			}
		}
		return lastEnd;
	}

	@Override
	public void populateScheduleInfo(OrderProcessModel op, Integer orderId)
			throws ServiceException {
		String groupName = op.getGroupName() + "";
		List<ScheduleResult> srs = this.srDao.findOrderSchedultResult(orderId);
		Calendar lastEnd = null;
		Calendar earlistBegin = null;
		for (ScheduleResult sr : srs) {
			int status = sr.getStatus();
			if (status == ScheduleResult.COMMIT || status == ScheduleResult.PRINTED || status == ScheduleResult.TINGGONG) {
				GroupUnitProcess gup = sr.getGup();
				if (!groupName.equals("" + gup.getProcessGroup())) continue;
				//只有开始时间，最后一个开始时间即为结束时间
				Calendar srEnd = sr.getStart();
				if (lastEnd == null) {
					lastEnd = srEnd;
				} else if (lastEnd.before(srEnd)) {
					lastEnd = srEnd;
				}
				Calendar srBegin = sr.getStart();
				if (earlistBegin == null) {
					earlistBegin = srBegin;
				} else if (earlistBegin.after(srBegin)) {
					earlistBegin = srBegin;
				}
			}
		}
		op.setBeginDate(DateUtil.getDate(earlistBegin, "yyyy-MM-dd"));
		op.setEndDate(DateUtil.getDate(lastEnd, "yyyy-MM-dd"));
	}
}
