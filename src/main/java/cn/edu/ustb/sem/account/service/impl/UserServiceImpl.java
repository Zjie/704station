package cn.edu.ustb.sem.account.service.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.dao.RoleDao;
import cn.edu.ustb.sem.account.dao.UserDao;
import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.account.service.UserService;
import cn.edu.ustb.sem.account.web.model.UserEditModel;
import cn.edu.ustb.sem.account.web.model.UserListModel;
import cn.edu.ustb.sem.account.web.model.UserSaveModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.schedule.dao.WorkerDao;
import cn.edu.ustb.sem.schedule.entity.Worker;
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User, UserListModel, Integer> implements UserService {
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private WorkerDao workerDao;
	
    @Autowired
    @Qualifier("userDao")
    @Override
    public void setBaseDao(BaseDao<User, Integer> userDao) {
        this.baseDao = userDao;
        this.userDao = (UserDao) userDao;
    }
	@Override
	public User getByUsername(String username) throws ServiceException {
		User model = new User();
		model.setUserName(username);
		List<User> users = userDao.listAll(model, -1, -1);
		if (users == null || users.isEmpty()) {
			throw new ServiceException("没有该用户");
		}
		User u = users.get(0);
		return u;
	}

	@Override
	public UserEditModel getUserEditModel(Integer uid) throws ServiceException {
		List<Role> roles = roleDao.listAll();
		UserEditModel uem = new UserEditModel();
		List<UserEditModel.Role> ueR = new ArrayList<UserEditModel.Role>();
		for (Role role : roles) {
			UserEditModel.Role uer = new UserEditModel.Role();
			uer.setRoleId(role.getId());
			uer.setRoleName(role.getName());
			ueR.add(uer);
		}
		uem.setRoles(ueR);
		if (uid != null && uid > 0) {
			uem.setUid(uid);
			User user = userDao.get(uid);
			if (user != null) {
				uem.setNickName(user.getNickName());
				uem.setPassword(user.getPassword());
				uem.setUserName(user.getUserName());
				uem.setReadOnly(true);
				Role role = user.getRole();
				if (role != null) {
					uem.setRoleId(role.getId());
				}
			}
		}
		return uem;
	}
	@Override
	public void saveOrUpdate(UserSaveModel usm, Visitor visitor) throws ServiceException {
		if (usm.isSaveOrNot()) {
			//如果是编辑，则不需要验证用户名是否重复
			saveUsm(usm, visitor);
		} else {
			updateUsm(usm, visitor);
		}
	}
	private void updateUsm(UserSaveModel usm, Visitor visitor) throws ServiceException {
		User user = new User();
		user.setId(usm.getUid());
		user.setUserName(usm.getUserName());
		user.setPassword(usm.getPassword());
		user.setUdate(Calendar.getInstance());
		user.setUpdater(new User(visitor.getUid()));
		user.setRole(new Role(usm.getRoleId()));
		user.setNickName(usm.getNickName());
		userDao.update(user);
	}
	private void saveUsm(UserSaveModel usm, Visitor visitor) throws ServiceException {
		User user = new User();
		user.setUserName(usm.getUserName());
		//验证用户名不能重复
		List<User> existsUser = userDao.listAll(user, -1, -1);
		if (existsUser != null && !existsUser.isEmpty()) {
			throw new ServiceException("用户名不能重复");
		}
		user.setPassword(usm.getPassword());
		user.setUdate(Calendar.getInstance());
		user.setUpdater(new User(visitor.getUid()));
		user.setRole(new Role(usm.getRoleId()));
		user.setNickName(usm.getNickName());
		userDao.save(user);
	}
	@Override
	public List<UserListModel> getUnbindedUsers(Integer workerId) throws ServiceException {
		List<UserListModel> users = new ArrayList<UserListModel>();
		if (workerId > 0) {
			Worker model = new Worker(workerId);
			Worker w = workerDao.find(model);
			User u = w.getUser();
			if (u != null) {
				users.add(new UserListModel(u));
			}
		}
		List<Worker> allWorkers = this.workerDao.listAll();
		Set<Integer> ids = new HashSet<Integer>();
		for (Worker w : allWorkers) {
			User u = w.getUser();
			if (u != null) ids.add(u.getId());
		}
		List<User> uu = userDao.listAll();
		for (User u : uu) {
			if (ids.contains(u.getId())) continue;
			if (u.getIsDeleted()  == User.DELETED) continue;
			Role r = u.getRole();
			//只要工人角色
			if (r == null || !r.getName().contains("工人")) continue;
			UserListModel ulm = new UserListModel(u);
			users.add(ulm);
		}
		
		return users;
	}
	@Override
	public void unbindRole(Integer userId) throws ServiceException {
		if (userId == null) throw new ServiceException("用户编号不能为空");
		User u = this.userDao.get(userId);
		if (u == null) throw new ServiceException("不存在该用户");
		u.setRole(null);
		this.userDao.update(u);
	}
	@Override
	public void delete(Integer userId) throws ServiceException {
		if (userId == null) throw new ServiceException("用户编号不能为空");
		User u = this.userDao.get(userId);
		if (u == null) throw new ServiceException("不存在该用户");
		Worker w = u.getWorker();
		if (w != null) throw new ServiceException("该账户被别的员工所关联，请先解除员工和该账户的绑定关系");
		u.setIsDeleted(User.DELETED);
		this.userDao.update(u);
	}
}
