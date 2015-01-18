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
@RequestMapping("/produce/other")
public class ProduceOtherController extends BaseController {
	@Autowired
	ReportService reportService;
	@RequestMapping(value="report")
	@ResponseBody
	public Map<String, Object> report(Integer orderId, String content, String reportDate) {
		try {
			this.reportService.reportOther(orderId, content, reportDate);
			return getSuccessJsonResult("操作成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value="getReportByWorker")
	@ResponseBody
	public Map<String, Object> getReportByWorker(Integer orderId) {
		try {
			result.put("data", this.reportService.getOtherReportByWorker(orderId));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
