package cn.edu.ustb.sem.assign.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.assign.service.AssignModuleService;
import cn.edu.ustb.sem.assign.web.model.AssignForSaveModel;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderSearchForm;

@Controller
@Scope("prototype")
@RequestMapping("/assign/mating")
public class AssignController extends BaseController{

	@Autowired
	private AssignModuleService assignModuleService;
	@Autowired
	private OrderService orderSerivce;
	
	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> list(OrderSearchForm form) {
		try {
			GridModel<OrderModel> data = orderSerivce.list(form, new ItemModelHelper<Order, OrderModel>(){
				@Override
				public OrderModel transfer(Order bo) {
					return new OrderModel(bo);
				}});
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	/**
	 * @author caiwenming
	 * 订单列表，并可配套
	 * @param form
	 * @return
	 */
	@RequestMapping(value="/getAssignOrder",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAssignOrder(Integer id){
		try{
			result.put("order", assignModuleService.getAssignOrderModel(id));
			return getSuccessJsonResult("", result);
		}catch(ServiceException e){
			logger.warn(e+"");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	/**
	 * @author caiwenming
	 * 给订单配套
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toAssign",method=RequestMethod.POST)
	@ResponseBody
	public String toAssign(AssignForSaveModel model){
		try{
			assignModuleService.getAssignService().toAssign(model);
		}catch(ServiceException e){
			logger.warn(e+"");
			return getErrorJsonMsg(e.getMessage()+",保存失败", null);
		}
		return getSuccessJsonMsg("保存成功", null);
	}
}
