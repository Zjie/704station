package cn.edu.ustb.sem.assign.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.assign.service.AssignService;
import cn.edu.ustb.sem.assign.service.DispatchMaterialService;
import cn.edu.ustb.sem.assign.web.model.DispatchMaterialModel;
import cn.edu.ustb.sem.assign.web.model.DispatchMaterialSearchForm;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.workerMg.service.WorkerService;

@Controller
@Scope("prototype")
@RequestMapping("/assign/dispatchMaterial")
public class DispatchMaterialController extends BaseController{
	@Autowired
	private AssignService assignService;
	@Autowired
	private DispatchMaterialService dmService;
	@Autowired
	private WorkerService workerService;
	@RequestMapping(value="/doDispatch",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doDispatch(String code, String remark) {
		try{
			assignService.doDispatch(code, remark);
			return getSuccessJsonResult("", result);
		}catch(ServiceException e){
			logger.warn(e+"");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value="/list", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(DispatchMaterialSearchForm form) {
		try {
			GridModel<DispatchMaterialModel> data = dmService.list(form);
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/listWorker",method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> list(){
		try{
			result.put("data", workerService.listAllModel());
			return getSuccessJsonResult("", result);
		}catch(ServiceException e){
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
