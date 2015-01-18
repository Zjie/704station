package cn.edu.ustb.sem.schedule.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.produce.service.ReportService;
import cn.edu.ustb.sem.schedule.service.ScheduleResultService;
import cn.edu.ustb.sem.schedule.service.ScheduleService;

@Controller
@Scope("prototype")
@RequestMapping("/schedule/manual")
public class ManualController extends BaseController {
	@Autowired
	private ScheduleService sService;
	@Autowired
	private ScheduleResultService srService;
	@Autowired
	private ReportService reportService;
	@RequestMapping(value = "/waixie/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> waixieList() {
		try {
			result.put("data", srService.getWaixieOrder());
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/waixie/restart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> restart(Integer orderId) {
		try {
			sService.waixieRestart(orderId, this.getVisitor().getUid());
			return getSuccessJsonResult("", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/tinggong/tinggongReportList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> tinggongReportList() {
		try {
			result.put("data", reportService.getTinggongReportOrders());
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/tinggong/commit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> commit(Integer ptid) {
		try {
			sService.tinggongCommit(ptid);
			return getSuccessJsonResult("", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/tinggong/tinggongCommitList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> tinggongCommitList() {
		try {
			result.put("data", reportService.getTinggongCommitOrders());
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/tinggong/restart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> tinggongRestart(Integer ptid) {
		try {
			sService.tinggongRestart(ptid);
			return getSuccessJsonResult("", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
