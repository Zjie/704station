package cn.edu.ustb.sem.schedule.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;
import cn.edu.ustb.sem.schedule.service.ScheduleService;
import cn.edu.ustb.sem.schedule.web.model.GanttModel;

import com.alibaba.fastjson.JSON;

@Controller
@Scope("prototype")
@RequestMapping("/schedule/automatic")
public class AutomaticController extends BaseController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ScheduleResultService scheduleResultService;

	/**
	 * @author caiwenming
	 * 带排产区订单
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list() {
		try {
			GridModel<OrderModel> data = this.scheduleService.getAutoScheduleOrder(); 
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	/**
	 * @author caiwenming
	 * 用户选择订单进行自动排产
	 * @param ids
	 * @return
	 * @throws ServiceException 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(value="/addToSchedule")
	public ModelAndView addToSchedule(String ids, Integer duration, HttpServletRequest request) throws ServiceException{
		if (duration == null) duration=14;
		List<Integer> orderIds = new ArrayList<Integer>();
		String[] strs = ids.split(",");
		for (String str : strs) {
			int id = Integer.parseInt(str);
			orderIds.add(id);
		}
		try {
			Map<String, Object> scheResult = new HashMap<String, Object>();
			this.scheduleService.autoSche(duration, orderIds, scheResult);
			@SuppressWarnings("unchecked")
			List<Order> successOrder = (List<Order>) scheResult.get("orders");
			GanttModel data = this.scheduleResultService.getNewlyScheduleResult(successOrder);
			result.put("scheduleResult", JSON.toJSONString(data));
			request.getSession(true).setAttribute("scheResult", scheResult);
			return new ModelAndView("/schedule/automatic/ganttChart", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			String errorMsg = e.getMessage();
			result.put("errorMsg", errorMsg);
			return new ModelAndView("/error", result);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveScheResult")
	@ResponseBody
	public Map<String, Object> saveScheResult(HttpServletRequest request) {
		Map<String, Object> scheResult = (Map<String, Object>) request.getSession(true).getAttribute("scheResult");
		try {
			this.scheduleService.saveScheduleResult(scheResult, this.getVisitor().getUid());
			request.getSession().removeAttribute("scheResult");
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			e.printStackTrace();
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	/**
	 * @author caiwenming
	 * 用户选择的待排产区订单
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/listSelected",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listSelected(String ids, HttpServletRequest request) {
		try{
			GridModel<OrderModel> data = this.scheduleService.selectedGrid(ids);
			result.put("data", data);
			return getSuccessJsonResult("", result);
		}catch(ServiceException e){
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
