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
@RequestMapping("/produce/shiyan")
public class ProduceTestController extends BaseController {
	@Autowired
	private ReportService reportService;
	
	//报在验个数
	@RequestMapping(value="report")
	@ResponseBody
	public Map<String, Object> report(Integer orderId, String content, Integer num, String reportDate) {
		try {
			this.reportService.reportTestingNum(orderId, num, content, reportDate);
			return getSuccessJsonResult("操作成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	//获取一个订单的某个员工的全部实验报工情况
	@RequestMapping(value="getTestReportByWorker")
	@ResponseBody
	public Map<String, Object> getTestReportByWorker(Integer orderId) {
		try {
			result.put("data", this.reportService.getTestReportByWorker(orderId));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	//确认一个实验报工已完成
	@RequestMapping(value="confirm")
	@ResponseBody
	public Map<String, Object> confirm(Integer ptId, String reportDate) {
		try {
			this.reportService.confirmProduceTest(ptId, reportDate);
			return getSuccessJsonResult("", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
