package cn.edu.ustb.sem.account.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.service.AccountService;
import cn.edu.ustb.sem.account.web.model.RoleListModel;
import cn.edu.ustb.sem.account.web.model.RoleSaveModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.TreeStore;

import com.alibaba.fastjson.JSON;

@Controller
@Scope("prototype")
@RequestMapping("/account/role")
public class RoleController extends BaseController {
	private static final String BASE_PATH = "/account/role";
	@Autowired
	private AccountService accountService;
	@RequestMapping(value = "/roleList")
	public ModelAndView roles() {
		try {
			List<RoleListModel> roles = accountService.getRoleService().getAllRoleModel();
			result.put("data", JSON.toJSONString(roles));
			return new ModelAndView(BASE_PATH + "/roleList", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return null;
		}
	}
	@RequestMapping(value = "/roleEdit")
	public ModelAndView roleEdit(Integer roleId) {
		try {
			if (roleId == null) {
				roleId = -1;
			}
			Map<String, Integer> nameIdMap = new HashMap<String, Integer>();
			List<TreeStore> treeStores = accountService.getRoleService().getRoleTreeStore(roleId, nameIdMap);
			
			if (roleId > 0) {
				Role model = new Role();
				model.setId(roleId);
				model = accountService.getRoleService().get(roleId);
				result.put("remark", model.getRemark());
				result.put("roleName", model.getName());
				result.put("roleId", roleId + "");
			}
			result.put("nameIdMap", JSON.toJSONString(nameIdMap));
			result.put("allTreeStore", JSON.toJSONString(treeStores));
			
			return new ModelAndView(BASE_PATH + "/roleEdit", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return null;
		}
	}
	@RequestMapping(value = "/roleSaveOrUpdate", method=RequestMethod.POST)
	@ResponseBody
	public String roleSave(@ModelAttribute RoleSaveModel rsm) {
		try {
			Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
			accountService.getRoleService().saveOpUpdate(rsm, visitor);
			return getSuccessJsonMsg("保存成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonMsg("保存失败", null);
		}
	}
	@RequestMapping(value = "/delete", method=RequestMethod.POST)
	@ResponseBody
	public String roleDelete(@RequestParam int roleId) {
		try {
			accountService.getRoleService().deleteRoleById(roleId);
			return getSuccessJsonMsg("删除成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "" + roleId);
			return getErrorJsonMsg(e.getMessage(), null);
		}
	}
}
