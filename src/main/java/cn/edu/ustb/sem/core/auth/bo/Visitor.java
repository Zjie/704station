package cn.edu.ustb.sem.core.auth.bo;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.User;

import com.alibaba.fastjson.JSON;
/**
 * 当前访问用户的包装类
 * @author zhoujie04
 *
 */
public class Visitor extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = -7039412812624811906L;
	private String userName;
	private int roleId;
	private int uid;
	private User user;
	private Role role;
	private boolean isAuthenticated = false;
	public Visitor(UserBo ub, boolean ia) {
		super(ub.getUser().getUserName(), ub.getUser().getPassword());
		this.user = ub.getUser();
		this.role = ub.getRole();
		this.uid = this.user.getId();
		this.roleId = this.role.getId();
		this.userName = this.user.getUserName();
		this.isAuthenticated = ia;
	}
	public Visitor(String username, int roleId, boolean ia, String password, int uid) {
		super(username, password);
		userName = username;
		this.roleId = roleId;
		isAuthenticated = ia;
		this.uid = uid;
	}
	public int getUid() {
		return uid;
	}

	public int getRoleId() {
		return roleId;
	}
	@Override
	public String getName() {
		return userName;
	}

	@Override
	public boolean isAuthenticated() {
		return isAuthenticated;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	public User getUser() {
		return user;
	}
	public Role getRole() {
		return role;
	}
	
}
