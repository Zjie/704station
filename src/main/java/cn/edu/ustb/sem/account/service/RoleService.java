package cn.edu.ustb.sem.account.service;

import java.util.List;
import java.util.Map;

import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.web.model.RoleListModel;
import cn.edu.ustb.sem.account.web.model.RoleSaveModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.core.web.model.TreeStore;

public interface RoleService extends BaseService<Role, RoleListModel, Integer> {
	//生成role表单
	public List<RoleListModel> getAllRoleModel() throws ServiceException;
	//获取该role下所有权限
	public List<TreeStore> getRoleTreeStore(int roleId, Map<String, Integer> nameIdMap) throws ServiceException;
	//更新或新增一个角色
	public void saveOpUpdate(RoleSaveModel rsm, Visitor visitor) throws ServiceException;
	//删除一个角色
	public void deleteRoleById(int roleId) throws ServiceException;
}
