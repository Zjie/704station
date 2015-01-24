package cn.edu.ustb.sem.produce.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.produce.service.ReportService;

@Controller
@Scope("prototype")
@RequestMapping("/produce/dianshi")
public class ProduceDianshiController extends BaseController {
	@Autowired
	ReportService reportService;
	//报在典数量
	@RequestMapping(value="report")
	@ResponseBody
	public Map<String, Object> report(Integer orderId, Integer num, String content, String reportDate) {
		try {
			this.reportService.reportDianshiingNum(orderId, num, content, reportDate);
			return getSuccessJsonResult("操作成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	//获取一个订单的某个员工的全部典试报工情况
	@RequestMapping(value="getReportByWorker")
	@ResponseBody
	public Map<String, Object> getReportByWorker(Integer orderId) {
		try {
			result.put("data", this.reportService.getDianshiReportByWorker(orderId));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	//确认一个典试报工已完成
	@RequestMapping(value="confirm")
	@ResponseBody
	public Map<String, Object> confirm(Integer pdId, String reportDate) {
		try {
			this.reportService.confirmProduceDianshi(pdId, reportDate);
			return getSuccessJsonResult("", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
