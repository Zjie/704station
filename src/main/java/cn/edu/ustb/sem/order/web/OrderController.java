package cn.edu.ustb.sem.order.web;

import java.util.Map;

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
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderSearchForm;
import cn.edu.ustb.sem.produce.service.ReportService;
import cn.edu.ustb.sem.produce.web.model.TgSearchForm;
@Controller
@Scope("prototype")
@RequestMapping("/order")
public class OrderController extends BaseController {
	@Autowired 
	ReportService reportService;
	@Autowired
	private OrderService orderService;
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveOrUpdate(OrderModel order) {
		try {
			orderService.saveOrUpdate(order);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/report/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> reportList(OrderSearchForm form) {
		try{
			GridModel<OrderModel> data = orderService.list(form, new ItemModelHelper<Order, OrderModel>() {
				@Override
				public OrderModel transfer(Order bo) {
					return new OrderModel(bo);
				}
			});
			result.put("data",data);
			return getSuccessJsonResult("", result);
		}catch(ServiceException e){
			logger.warn(e+"");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/report/detail")
	public ModelAndView produceReport(Integer orderId){
		try {
			Map<String, Object> result = this.reportService.produceReport(orderId, false, null);
			return new ModelAndView("/order/report/detail", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			result.put("errorMsg", e.getMessage());
			return new ModelAndView("/error", result);
		}
	}
	@RequestMapping(value = "/tinggongList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> tinggongList(TgSearchForm form) {
		try {
			result.put("data", reportService.getTinggongHistory(form));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/deleteOrderMaterial", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteOrderMaterial(Integer omid) {
		try {
			this.orderService.deleteOrderMaterial(omid);
			return getSuccessJsonResult("", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/deleteOrderProcess", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteOrderProcess(Integer opid) {
		try {
			this.orderService.deleteOrderProcess(opid);
			return getSuccessJsonResult("", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	//查看报工总计
	@RequestMapping(value = "/report/findTotalReport", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findTotalReport(String begin, String end) {
		try {
			return getSuccessJsonResult("data", this.reportService.getReportByDate(begin, end));
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
