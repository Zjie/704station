package cn.edu.ustb.sem.core.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import cn.edu.ustb.sem.core.auth.bo.AnonymousVisitor;

public class AnonymousProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		return AnonymousVisitor.getInstance();
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AnonymousAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
