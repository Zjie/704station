package cn.edu.ustb.sem.produce.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.kpi.web.model.WorkerKpiSearchForm;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.produce.service.ReportService;
import cn.edu.ustb.sem.produce.web.model.ProduceReportForSaveModel;
import cn.edu.ustb.sem.produce.web.model.ScheduledResultModel;
import cn.edu.ustb.sem.produce.web.model.TinggongModel;

@Controller
@Scope("prototype")
@RequestMapping("/produce")
public class ProduceReportController extends BaseController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	ReportService reportService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(Integer id){
		try {
			GridModel<OrderModel> data = this.reportService.getOrder(id);
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/report")
	public ModelAndView produceReport(Integer oid){
		try {
			Map<String, Object> result = this.reportService.produceReport(oid, true, null);
			return new ModelAndView("/produce/edit", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			result.put("errorMsg", e.getMessage());
			return new ModelAndView("/error.jsp", result);
		}
	}
	@RequestMapping(value = "/reportByCode")
	public ModelAndView reportByCode(String code) {
		try {
			Map<String, Object> result = this.reportService.produceReport(code, true);
			return new ModelAndView("/produce/edit", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			result.put("errorMsg", e.getMessage());
			return new ModelAndView("/error.jsp", result);
		}
	}
	@RequestMapping(value = "/getOrderIdByCode")
	@ResponseBody
	public Map<String, Object> getOrderIdByCode(String code) {
		try {
			Integer orderId = this.reportService.getOrderIdByCode(code);
			result.put("orderId", orderId);
			return getSuccessJsonResult("", result);
		} catch (Exception e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value="toReport")
	@ResponseBody
	public String toReport(ProduceReportForSaveModel model){
		try{
			reportService.toReport(model);
		}catch(ServiceException e){
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + "保存失败", null);
		}
		return getSuccessJsonMsg("保存成功", null);
	}
	@RequestMapping(value="toTinggong")
	@ResponseBody
	public String toTinggong(TinggongModel tg) {
		try {
			reportService.reportTinggong(tg);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + "上报失败", null);
		}
		return getSuccessJsonMsg("上报成功", null);
	}
	@RequestMapping(value="findOrderSr")
	@ResponseBody
	public Map<String, Object> findOrderSr(String code) {
		try {
			List<ScheduledResultModel> srs = reportService.findOrderSrs(code);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("data", srs);
			return getSuccessJsonResult("", data);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	//报完成装配的个数
	@RequestMapping(value="assembledNumReport")
	@ResponseBody
	public Map<String, Object> produceAssembledReport(Integer orderId, Integer num, String reportDate) {
		Integer userId = this.getVisitor().getUid();
		try {
			this.reportService.produceAssembledReport(orderId, num, userId, reportDate);
			return getSuccessJsonResult("操作成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/myReportDetail")
	public ModelAndView produceReport(WorkerKpiSearchForm form){
		try {
			Visitor v = this.getVisitor();
			User u = v.getUser();
			form.setWorkerId(u.getWorker().getId());
			Map<String, Object> result = this.reportService.produceReport(form);
			return new ModelAndView("/produce/myReportDetail", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			result.put("errorMsg", e.getMessage());
			return new ModelAndView("/error", result);
		}
	}
}
