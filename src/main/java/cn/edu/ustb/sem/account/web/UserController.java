package cn.edu.ustb.sem.account.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.account.service.AccountService;
import cn.edu.ustb.sem.account.service.UserService;
import cn.edu.ustb.sem.account.web.model.UserEditModel;
import cn.edu.ustb.sem.account.web.model.UserListModel;
import cn.edu.ustb.sem.account.web.model.UserSaveModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;

@Controller
@Scope("prototype")
@RequestMapping("/account/user")
public class UserController extends BaseController {
	private static final String BASE_PATH = "/account/user";
	@Autowired
	private AccountService accountService;
	@RequestMapping(value = "/userEdit")
	public ModelAndView userEdit(Integer uid) {
		try {
			UserEditModel uem = accountService.getUserService().getUserEditModel(uid);
			return new ModelAndView(BASE_PATH + "/userEdit", "uem", uem);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return null;
		}
	}
	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(UserSaveModel usm) {
		try {
			Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
			accountService.getUserService().saveOrUpdate(usm, visitor);
			return getSuccessJsonMsg("保存成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + ", 保存失败", null);
		}
	}
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam int uid) {
		try {
			UserService usrSer = accountService.getUserService();
			usrSer.delete(uid);
			return getSuccessJsonMsg("删除成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "" + uid);
			return getErrorJsonMsg(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/list", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(GridModel<UserListModel> form) {
		try {
			//取出第一页的
			GridModel<UserListModel> users = accountService.getUserService().list(new User(), form.getPage(), form.getLimit(), new ItemModelHelper<User, UserListModel>(){
				@Override
				public UserListModel transfer(User bo) {
					return new UserListModel(bo);
				}});
			result.put("data", users);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/unbindRole", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unbindRole(Integer uid) {
		try {
			this.accountService.getUserService().unbindRole(uid);
			return getSuccessJsonResult("解绑成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
}
