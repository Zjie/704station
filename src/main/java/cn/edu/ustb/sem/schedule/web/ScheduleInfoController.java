package cn.edu.ustb.sem.schedule.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.schedule.entity.GroupProductCode;
import cn.edu.ustb.sem.schedule.service.ScheduleInfoService;
import cn.edu.ustb.sem.schedule.web.model.GroupUnitProcessModel;
import cn.edu.ustb.sem.schedule.web.model.ProductGroupModel;
@Controller
@Scope("prototype")
@RequestMapping("/schedule/info")
public class ScheduleInfoController extends BaseController {
	@Autowired
	private ScheduleInfoService siService;
	//列出产品族信息
	@RequestMapping(value = "/group/listGroup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listGroup(String groupName, String id) {
		GroupProductCode model = new GroupProductCode();
		if (groupName != null && !groupName.isEmpty()) {
			model.setGroupName(groupName);
		}
		if (id != null && !id.isEmpty()) {
			model.setId(new Integer(id));
		}
		try {
			List<ProductGroupModel> data = this.siService.list(model);
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/group/deleteGroup", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteGroup(String id) {
		try {
			this.siService.deleteGroup(new Integer(id));
			return getSuccessJsonResult("", null);
		} catch (NumberFormatException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/group/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveOrUpdateGroup(ProductGroupModel pgm) {
		try {
			this.siService.saveOrUpdateGroup(pgm);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/groupProcess/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteGroupProcess(String id) {
		try {
			this.siService.deleteGroupProcess(new Integer(id));
			return getSuccessJsonResult("", null);
		} catch (NumberFormatException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/groupProcess/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listGroupProcess(GroupUnitProcessModel form) {
		try {
			result.put("data", this.siService.list(form));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/groupProcess/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveOrUpdateGroupProcess(GroupUnitProcessModel gupm) {
		try {
			this.siService.saveOrUpdateGroupProcess(gupm);
			return getSuccessJsonResult("", null);
		}  catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
