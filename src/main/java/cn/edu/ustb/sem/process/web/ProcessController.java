package cn.edu.ustb.sem.process.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.process.entity.PProcess;
import cn.edu.ustb.sem.process.service.ProcessService;
import cn.edu.ustb.sem.process.web.model.ProcessModel;
import cn.edu.ustb.sem.process.web.model.ProcessSearchForm;
@Controller
@Scope("prototype")
@RequestMapping("/process/process")
public class ProcessController extends BaseController {
	@Autowired
	private ProcessService processService;
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(ProcessSearchForm form) {
		try {
			GridModel<ProcessModel> data = processService.list(form, new ItemModelHelper<PProcess, ProcessModel>(){
				@Override
				public ProcessModel transfer(PProcess bo) {
					return new ProcessModel(bo);
				}});
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam int pid) {
		try {
			processService.delete(pid);
			return getSuccessJsonMsg("删除成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "" + pid);
			return getErrorJsonMsg(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/findById")
	@ResponseBody
	public Map<String, Object> findById(@RequestParam int pid) {
		PProcess p;
		try {
			p = processService.get(pid);
			if (p != null) {
				result.put("data", new ProcessModel(p));
				return getSuccessJsonResult("", result);
			}
			return getErrorJsonResult("不存在该工序", null);
		} catch (ServiceException e) {
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(ProcessModel model) {
		try {
			processService.saveOrUpdate(model);
			return getSuccessJsonMsg("保存成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage(), null);
		}
	}
}
