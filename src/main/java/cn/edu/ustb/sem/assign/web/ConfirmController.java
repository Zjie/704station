package cn.edu.ustb.sem.assign.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.order.service.OrderService;

@Controller
@Scope("prototype")
@RequestMapping("/assign/confirm")
public class ConfirmController extends BaseController{
	@Autowired
	private OrderService orderService;
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> confirm(Integer orderId) {
		try {
			orderService.confirmOrderMaterialInfo(orderId);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
