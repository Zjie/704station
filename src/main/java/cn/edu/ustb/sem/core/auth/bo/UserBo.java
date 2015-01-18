package cn.edu.ustb.sem.core.auth.bo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.User;

public class UserBo implements UserDetails {
	private static final long serialVersionUID = 3874606480615013761L;
	private String userName;
	private String password;
	//用户和角色是一对一
	private int roleId;
	private Role role;
	private int uid;
	private User user;
	public UserBo(User u, Role r) {
		this.user = u;
		this.uid = u.getId();
		this.userName = u.getUserName();
		this.password = u.getPassword();
		this.role = r;
		this.roleId = r.getId();
	}
	public UserBo(String userName, String password, int roleId, int uid) {
		this.userName = userName;
		this.password = password;
		this.roleId = roleId;
		this.uid = uid;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	
	public int getUid() {
		return uid;
	}
	public int getRoleId() {
		return roleId;
	}
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	public Role getRole() {
		return role;
	}
	public User getUser() {
		return user;
	}
	

}
