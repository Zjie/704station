package cn.edu.ustb.sem.assign.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.assign.service.AssignService;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;

@Controller
@Scope("prototype")
@RequestMapping("/assign/dispatchMaterial")
public class DispatchMaterialController extends BaseController{
	@Autowired
	private AssignService assignService;
	@RequestMapping(value="/doDispatch",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> doDispatch(String code) {
		try{
			assignService.doDispatch(code);
			return getSuccessJsonResult("", result);
		}catch(ServiceException e){
			logger.warn(e+"");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
