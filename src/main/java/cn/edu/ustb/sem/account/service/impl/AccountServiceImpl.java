package cn.edu.ustb.sem.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.service.AccountService;
import cn.edu.ustb.sem.account.service.RoleService;
import cn.edu.ustb.sem.account.service.UrlService;
import cn.edu.ustb.sem.account.service.UserService;
@Service("accountService")
public class AccountServiceImpl implements AccountService {
	@Autowired
	private UrlService urlService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Override
	public UrlService getUrlService() {
		return urlService;
	}

	@Override
	public UserService getUserService() {
		return userService;
	}

	@Override
	public RoleService getRoleService() {
		return roleService;
	}

}
