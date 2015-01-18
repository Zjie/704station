package cn.edu.ustb.sem.account.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import cn.edu.ustb.sem.BaseITTest;
import cn.edu.ustb.sem.account.entity.Application;
import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.Url;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.account.web.model.RoleSaveModel;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.web.model.TreeStore;

public class AccountServiceITTest extends BaseITTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UrlService urlService;
	@Autowired
	private RoleService roleService;
	@Rollback(true)
	@Test
	public void testSave() throws ServiceException {
		User model = new User();
		model.setUserName("shanji");
		model.setPassword("123456");
		userService.save(model);
		System.out.println("id is " + model.getId());
		Assert.assertTrue(model.getId() > 0);
	}

	@Test
	public void testListAllByPage() throws ServiceException {
		Page<User> users = userService.listAll(2, 2);
		Assert.assertNotNull(users);
	}
	/**
	 * 测试列出所有用户
	 * @throws ServiceException 
	 */
	@Test
	public void testListAll() throws ServiceException {
		List<User> users = userService.listAll();
		Assert.assertNotNull(users);
	}
	/**
	 * 测试多对多关系
	 * @throws ServiceException 
	 */
	@Test
	public void testRoleApp() throws ServiceException {
		List<User> users = userService.listAll();
		User admin = users.get(0);
		Role adRole = admin.getRole();
		Assert.assertNotNull(adRole);
		Set<Application> apps = adRole.getApps();
		for (Application app : apps) {
			System.out.println(app.getName());
		}
		Assert.assertTrue(apps.size() > 0);
		Role planner = users.get(1).getRole();
		apps = planner.getApps();
		for (Application app : apps) {
			System.out.println(app.getName());
		}
		Assert.assertTrue(apps.size() > 0);
	}
	/**
	 * 测试递归菜单树的生成
	 * @throws ServiceException 
	 */
	@Test
	public void testAppRecursion() throws ServiceException {
		List<User> users = userService.listAll();
		Role planner = users.get(1).getRole();
		Set<Application> apps = planner.getApps();
		for (Application app : apps) {
			if (app.getId() == 14) {
				System.out.println("parent : " + app.getParent().getName());
				System.out.println("this : " + app.getName());
				for (Application child : app.getChilds()) {
					System.out.println("child : " + child.getName());
				}
			}
		}
	}
	
	@Test
	public void testUrl() throws ServiceException {
		List<Url> urls = urlService.listAll();
		for (Url u : urls) {
			System.out.println(u.getUrl());
			Application app = u.getApp();
			System.out.println(app.getName());
			for (Role r : app.getRoles()) {
				System.out.println(r.getName());
			}
		}
		Assert.assertTrue(urls.size() > 0);
	}
	
	@Test
	public void testAppUrlsMap() throws ServiceException {
		Set<Application> apps = userService.listAll().get(0).getRole().getApps();
		for (Application app : apps) {
			System.out.println(app.getName());
			for (Url u : app.getUrls()) {
				System.out.println(u.getUrl());
			}
		}
		Assert.assertTrue(apps.size() > 0);
	}
	
	@Test
	public void testGenericModelSelect() throws ServiceException {
		User u = new User();
		u.setUserName("zhoujie");
		Page<User> result = userService.listAll(u, 0, 10);
		Assert.assertTrue(result.getItems().size() == 1);
	}
	/**
	 * 测试递归性hql语句
	 * @throws ServiceException 
	 */
	@Test
	public void testDepthSelect() throws ServiceException {
		User u = new User();
		u.setUserName("zhoujie");
		Role r = new Role();
		r.setName("xba1");
		u.setRole(r);
		Page<User> result = userService.listAll(u, 0, 10);
		Assert.assertTrue(result.getItems().size() == 0);
	}
	@Test
	public void testGetRoleTreeStore() throws ServiceException {
		Map<String, Integer> nameIdMap = new HashMap<String, Integer>();
		List<TreeStore> treeStores = roleService.getRoleTreeStore(1, nameIdMap);
		Assert.assertTrue(treeStores.size() > 0);
	}
	/**
	 * 测试更新角色的权限
	 * @throws ServiceException
	 */
	@Rollback(true)
	@Test
	public void testUpdateRoleApp() throws ServiceException {
		RoleSaveModel rsm = new RoleSaveModel();
		rsm.setRoleId(1);
		rsm.setRoleName("超级管理员");
		rsm.setDesc("管理所有系统权限");
		rsm.setAppIds(Arrays.asList(1, 2, 45, 46, 47, 48));
		Visitor v = new Visitor("", 0, true, "", 1);
		roleService.saveOpUpdate(rsm, v);
	}
	@Rollback(false)
	@Test
	public void testSaveRoleApp() throws ServiceException {
		RoleSaveModel rsm = new RoleSaveModel();
		rsm.setRoleName("测试一个角色");
		rsm.setDesc("管理所有系统权限");
		rsm.setAppIds(Arrays.asList(1, 2, 4));
		Visitor v = new Visitor("", 0, true, "", 1);
		roleService.saveOpUpdate(rsm, v);
	}
	@Rollback(true)
	@Test
	public void testDeleteRole() throws ServiceException {
		int roleId = 2;
		roleService.deleteRoleById(roleId);
		Role role = roleService.get(roleId);
		Assert.assertNull(role);
	}
	@Test
	public void testUrlMatch() {
		String url = "/account/role/roleList.do";
		String pattern = "/account/role/*";
		PathMatcher urlMatcher = new AntPathMatcher();
		Assert.assertTrue(urlMatcher.match(pattern, url));
	}
}
