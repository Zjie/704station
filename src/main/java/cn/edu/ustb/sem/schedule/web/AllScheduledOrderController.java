package cn.edu.ustb.sem.schedule.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;
import cn.edu.ustb.sem.schedule.service.ScheduleService;
import cn.edu.ustb.sem.schedule.web.model.GanttModel;

import com.alibaba.fastjson.JSON;
@Controller
@Scope("prototype")
@RequestMapping("/schedule")
public class AllScheduledOrderController extends BaseController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ScheduleResultService scheduleResultService;
	
	@RequestMapping(value="/allScheduledOrder")
	public ModelAndView addToSchedule(String dateBegin, String dateEnd) throws ServiceException{
		try {
			GanttModel data = this.scheduleResultService.getScheduledResultByDate(dateBegin, dateEnd);
			result.put("scheduleResult", JSON.toJSONString(data));
			return new ModelAndView("/schedule/allScheduledOrder", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
		}
		return null;
	}
	
}
