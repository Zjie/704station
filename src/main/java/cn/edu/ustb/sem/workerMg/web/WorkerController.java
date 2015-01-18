package cn.edu.ustb.sem.workerMg.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.account.service.AccountService;
import cn.edu.ustb.sem.account.web.model.UserListModel;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.schedule.entity.Worker;
import cn.edu.ustb.sem.workerMg.service.WorkerService;
import cn.edu.ustb.sem.workerMg.web.model.WorkerModel;

@Controller
@Scope("prototype")
@RequestMapping("/worker")
public class WorkerController extends BaseController{

	@Autowired
	private WorkerService workerService;

	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(GridModel<WorkerModel> form){
		try{
			GridModel<WorkerModel> data = workerService.list(null, form.getPage(), form.getLimit(), new ItemModelHelper<Worker, WorkerModel>() {
				@Override
				public WorkerModel transfer(Worker bo) {
					return new WorkerModel(bo);
				}
			});
			result.put("data", data);
			return getSuccessJsonResult("", result);
		}catch(ServiceException e){
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/listAll",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listAll() {
		try {
			List<WorkerModel> workers = new ArrayList<WorkerModel>();
			List<Worker> data = workerService.listAll();
			for (Worker w : data) {
				workers.add(new WorkerModel(w));
			}
			result.put("data", workers);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}

	@RequestMapping(value = "/listUser", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listUser(Integer workerId) {
		try {
			List<UserListModel> users = accountService.getUserService().getUnbindedUsers(workerId);
			result.put("data", users);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	
	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getById(Integer id){
		try {
			Worker worker = workerService.get(id);
			WorkerModel model = null;
			if(worker != null){
				model = new WorkerModel(worker);
			}
			result.put("data", model);
			return getSuccessJsonResult("", result);
		} catch(ServiceException e){
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdate(WorkerModel model){
		try{
			workerService.saveOrUpdate(model);
		}catch(ServiceException e){
			logger.warn(e+"");
			return getErrorJsonMsg(e.getMessage()+",保存失败", null);
		}
		return getSuccessJsonMsg("保存成功", null);
	}
	
	@RequestMapping(value = "/freeze", method = RequestMethod.POST)
	@ResponseBody
	public String freeze(Integer workerId){
		try{
			workerService.freeze(workerId);
			return getSuccessJsonMsg("冻结成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "" + workerId);
			return getErrorJsonMsg(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/unbind", method = RequestMethod.POST)
	@ResponseBody
	public String unbind(Integer workerId){
		try{
			workerService.unbind(workerId);
			return getSuccessJsonMsg("解绑成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "" + workerId);
			return getErrorJsonMsg("解绑失败", null);
		}
	}
}
