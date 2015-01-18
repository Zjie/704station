package cn.edu.ustb.sem.schedule.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.cons.OrderStatus;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderSearchForm;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;
import cn.edu.ustb.sem.schedule.service.ScheduleService;
import cn.edu.ustb.sem.schedule.web.model.GanttModel;

import com.alibaba.fastjson.JSON;

@Controller
@Scope("prototype")
@RequestMapping("/schedule/adjust")
public class AdjustController extends BaseController {

		@Autowired
		private OrderService orderService;
		@Autowired
		private ScheduleService scheduleService;
		@Autowired
		private ScheduleResultService scheduleResultService;
		/**
		 * 列出可调整的订单
		 * @param form
		 * @return
		 */
		@RequestMapping(value = "/list", method = RequestMethod.POST)
		@ResponseBody
		public Map<String, Object> list(OrderSearchForm form) {
			form.setStatus(OrderStatus.INITIAL.getIndex());
			try {
				GridModel<OrderModel> data = this.orderService.listCanAdjustOrder(form);
				result.put("data", data);
				return getSuccessJsonResult("", result);
			} catch (ServiceException e) {
				logger.warn(e + "");
				return getErrorJsonResult(e.getMessage(), null);
			}
		}

		/**
		 * 找出可以进行重排的订单进行重排
		 * @param ids
		 * @return
		 */
		@RequestMapping(value="/listSelected",method=RequestMethod.POST)
		@ResponseBody
		public Map<String, Object>addToSchedule(String ids, String begin) {
			try{
				GridModel<OrderModel> data = this.scheduleService.getScheduleOrderBeforeDate(begin, ids);
				result.put("data", data);
				return getSuccessJsonResult("", result);
			}catch(ServiceException e){
				logger.warn(e + "");
				return getErrorJsonResult(e.getMessage(), null);
			}
		}
		
		@RequestMapping(value="/adjustSchedulePlan")
		public ModelAndView addToSchedule(String ids, HttpServletRequest request) throws ServiceException{
			try{
				GanttModel data = this.scheduleService.adjustPlan(ids, request);
				result.put("scheduleResult", JSON.toJSONString(data));
				return new ModelAndView("/schedule/adjust/ganttChart", result);
			}catch(ServiceException e){
				logger.warn(e + "");
			}
			return null;
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
		@RequestMapping(value = "/cancel")
		@ResponseBody
		public Map<String, Object> cancel(HttpServletRequest request) {
			try {
				this.scheduleService.cancelAdjust(request);
				return getSuccessJsonResult("", result);
			} catch (ServiceException e) {
				e.printStackTrace();
				return getErrorJsonResult(e.getMessage(), null);
			}
		}
		@RequestMapping(value = "/dispatchUnit")
		@ResponseBody
		public Map<String, Object> dispatchUnit(String unit, Integer oid) {
			Visitor v = this.getVisitor();
			try {
				this.scheduleService.dispatchUnit(oid, unit, v.getUid());
				return getSuccessJsonResult("", null);
			} catch (ServiceException e) {
				e.printStackTrace();
				return getErrorJsonResult(e.getMessage(), null);
			}
		}
		@RequestMapping(value = "/selectUnit")
		@ResponseBody
		public Map<String, Object> selectUnit() {
			try {
				List<String> allUnit = this.scheduleService.listAllUnit();
				result.put("allUnit", allUnit);
				return getSuccessJsonResult("", result);
			} catch (ServiceException e) {
				e.printStackTrace();
				return getErrorJsonResult(e.getMessage(), null);
			}
		}
		@RequestMapping(value = "/listScheduleOrder")
		@ResponseBody
		public Map<String, Object> listScheduleOrder() {
			result.put("data", this.orderService.listScheduledOrders());
			return getSuccessJsonResult("", result);
		}
}
