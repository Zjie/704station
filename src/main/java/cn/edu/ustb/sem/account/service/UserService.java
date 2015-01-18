package cn.edu.ustb.sem.account.service;

import java.util.List;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.account.web.model.UserEditModel;
import cn.edu.ustb.sem.account.web.model.UserListModel;
import cn.edu.ustb.sem.account.web.model.UserSaveModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;

public interface UserService extends BaseService<User, UserListModel, Integer> {
	public User getByUsername(String username) throws ServiceException;
	//新增用户
	public UserEditModel getUserEditModel(Integer uid) throws ServiceException;
	//更新或新增用户
	public void saveOrUpdate(UserSaveModel usm, Visitor visitor) throws ServiceException;
	//查找为绑定员工的账户
	public List<UserListModel> getUnbindedUsers(Integer workerId) throws ServiceException;
	public void unbindRole(Integer userId) throws ServiceException;
}
