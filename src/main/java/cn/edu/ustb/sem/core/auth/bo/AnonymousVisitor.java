package cn.edu.ustb.sem.core.auth.bo;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AnonymousVisitor  implements Authentication {
	private static final long serialVersionUID = 4268198248150271108L;
	private static final AnonymousVisitor v = new AnonymousVisitor();
	public static final AnonymousVisitor getInstance() {
		return v;
	}
	@Override
	public String getName() {
		return "匿名用户";
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
	@Override
	public Object getCredentials() {
		return null;
	}
	@Override
	public Object getDetails() {
		return null;
	}
	@Override
	public Object getPrincipal() {
		return null;
	}
	@Override
	public boolean isAuthenticated() {
		return false;
	}
	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		
	}
}
