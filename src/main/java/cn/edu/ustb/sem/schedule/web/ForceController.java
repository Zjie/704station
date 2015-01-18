package cn.edu.ustb.sem.schedule.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.core.cons.OrderStatus;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderSearchForm;
import cn.edu.ustb.sem.schedule.service.ScheduleService;

@Controller
@Scope("prototype")
@RequestMapping("/schedule/force")
public class ForceController extends BaseController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private ScheduleService scheduleService;

	/**
	 * @author caiwenming
	 * 列出未完成配套订单
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(OrderSearchForm form) {
		form.setStatus(OrderStatus.INITIAL.getIndex());
		try {
			GridModel<OrderModel> data = this.orderService.listForceScheduleOrder();
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}

	/**
	 * @author caiwenming
	 * 用户进行强制排产
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/addToSchedule", method = RequestMethod.POST)
	@ResponseBody
	public String addToSchedule(String ids) {
		try {
			scheduleService.addToSchedule(ids);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + ",添加失败", null);
		}
		return getSuccessJsonMsg("添加成功", null);
	}
}
