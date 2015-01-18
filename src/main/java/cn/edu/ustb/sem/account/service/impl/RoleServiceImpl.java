package cn.edu.ustb.sem.account.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.dao.ApplicationDao;
import cn.edu.ustb.sem.account.dao.RoleAppDao;
import cn.edu.ustb.sem.account.dao.RoleDao;
import cn.edu.ustb.sem.account.dao.UserDao;
import cn.edu.ustb.sem.account.entity.Application;
import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.RoleApplication;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.account.service.RoleService;
import cn.edu.ustb.sem.account.web.model.RoleListModel;
import cn.edu.ustb.sem.account.web.model.RoleSaveModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.core.web.model.TreeStore;
import cn.edu.ustb.sem.core.web.model.TreeStore.Node;
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleListModel, Integer> implements RoleService {
	private RoleDao roleDao;
	@Autowired
	private ApplicationDao applicationDao;
	@Autowired
	private RoleAppDao roleAppDao;
	@Autowired
	private UserDao userDao;
	@Autowired
    @Qualifier("roleDao")
	@Override
	public void setBaseDao(BaseDao<Role, Integer> baseDao) {
		this.roleDao = (RoleDao) baseDao;
		this.baseDao = baseDao;
	}
	@Override
	public List<RoleListModel> getAllRoleModel() throws ServiceException {
		List<Role> roles = roleDao.listAll();
		List<RoleListModel> roleModel = new ArrayList<RoleListModel>();
		for (Role role : roles) {
			RoleListModel rm = new RoleListModel(role);
			roleModel.add(rm);
		}
		return roleModel;
	}
	@Override
	public List<TreeStore> getRoleTreeStore(int roleId, Map<String, Integer> nameIdMap) throws ServiceException {
		//这个变量是用来标记哪些权限已经被赋予过给角色了
		Map<Integer, Application> idAppMap = getAppsByRoleId(roleId);
		List<TreeStore> tss = new ArrayList<TreeStore>();
		//得到所有app
		List<Application> allApp = getFirstLevelApp();
		for (Application curApp : allApp) {
			Node root = new Node();
			root.setChildren(new ArrayList<Node>());
			root.getChildren().add(getNodeDFS(curApp, idAppMap, nameIdMap));
			TreeStore treeStore = new TreeStore();
			treeStore.setId("treeStore_" + curApp.getId());
			treeStore.setRoot(root);
			treeStore.setModel("Item");
			tss.add(treeStore);
		}
		return tss;
	}
	/**
	 * 获取该角色下所有的application
	 * @param roleId
	 * @return
	 * @throws ServiceException
	 */
	private Map<Integer, Application> getAppsByRoleId(int roleId) throws ServiceException {
		Map<Integer, Application> idAppMap = new HashMap<Integer, Application>();
		if (roleId <= 0) {
			return idAppMap;
		}
		Role role = new Role();
		role.setId(roleId);
		List<Role> rlist = roleDao.listAll(role, 0, 5);
		if (rlist == null || rlist.isEmpty()) {
			return idAppMap;
		}
		//只考虑一个用户对应一个角色
		role = rlist.get(0);
		Set<Application> apps  = role.getApps();
		for (Application app : apps) {
			idAppMap.put(app.getId(), app);
		}
		return idAppMap;
	}
	/**
	 * 获取一级菜单
	 * @return
	 * @throws ServiceException
	 */
	private List<Application> getFirstLevelApp() throws ServiceException {
		Application model = new Application();
		model.setLevel(0);
		model.setIsMenu((byte)1);
		return applicationDao.listAll(model, -1, -1);
	}
	/**
	 * 深度遍历app树
	 * @param app
	 * @return
	 */
	private Node getNodeDFS(Application app, Map<Integer, Application> idAppMap, Map<String, Integer> nameIdMap) {
		nameIdMap.put(app.getName(), app.getId());
		Node n = new Node();
		n.setText(app.getMenuName());
		n.setExpanded(true);
		//设置是否选上
		if (idAppMap.containsKey(app.getId())) {
			n.setChecked(true);
		} else {
			n.setChecked(false);
		}
		Set<Application> childs = app.getChilds();
		if (childs == null || childs.isEmpty()) {
			n.setLeaf(true);
			n.setHrefTarget(app.getMenuUrl());
			return n;
		} else {
			List<Node> childNode = new ArrayList<Node>();
			for (Application subApp : childs) {
				Node subNode = getNodeDFS(subApp, idAppMap, nameIdMap);
				childNode.add(subNode);
			}
			n.setChildren(childNode);
		}
		return n;
	}
	@Override
	public void saveOpUpdate(RoleSaveModel rsm, Visitor visitor) throws ServiceException {
		Calendar cur = Calendar.getInstance();
		Role role = new Role();
		role.setName(rsm.getRoleName());
		role.setRemark(rsm.getDesc());
		role.setUdate(cur);
		User updater = new User();
		updater.setId(visitor.getUid());
		role.setUpdater(updater);
		role.setApps(new HashSet<Application>());
		if (rsm.getRoleId() > 0) {
			role.setId(rsm.getRoleId());
		}
		//更新或插入
		roleDao.saveOrUpdate(role);
		//更新权限
		RoleApplication roleApp = new RoleApplication();
		int roleId = role.getId();
		roleApp.setRoleId(roleId);
		List<Integer> appIds = rsm.getAppIds();
		if (appIds == null || appIds.isEmpty()) {
			return;
		}
		roleAppDao.deleteObject(roleApp);
		roleAppDao.flush();
		//增加一个隐藏的application
		appIds.add(1);
		for (Integer appId : appIds) {
			RoleApplication ra = new RoleApplication();
			ra.setAppId(appId);
			ra.setRoleId(roleId);
			ra.setUdate(cur);
			ra.setUuid(visitor.getUid());
			roleAppDao.save(ra);
		}
	}
	@Override
	public void deleteRoleById(int roleId) throws ServiceException {
		if (roleId <= 0) {
			throw new ServiceException("参数不正确");
		}
		//先判断是否有账户依赖这个角色
		User userModel = new User();
		userModel.setRole(new Role(roleId));
		if (this.userDao.find(userModel) != null) {
			throw new ServiceException("该角色不能被删除，请先解除与该角色管理的账户绑定。");
		}
		roleDao.delete(roleId);
		RoleApplication roleApp = new RoleApplication();
		roleApp.setRoleId(roleId);
		roleAppDao.deleteObject(roleApp);
	}
}
