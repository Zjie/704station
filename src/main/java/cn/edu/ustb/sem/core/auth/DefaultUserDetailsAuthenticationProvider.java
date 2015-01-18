package cn.edu.ustb.sem.core.auth;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.account.service.AccountService;
import cn.edu.ustb.sem.core.auth.bo.UserBo;
import cn.edu.ustb.sem.core.auth.bo.Visitor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class DefaultUserDetailsAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider {
	protected final Log logger = LogFactory.getLog(getClass());
	@Autowired
	private AccountService accountService;
	private LoadingCache<String, UserBo> cache;
	private int maxCacheNum;
	private int authenticationExpiredTimes;
	@SuppressWarnings("unused")
	private void init() {
		cache = CacheBuilder.newBuilder()
				.maximumSize(maxCacheNum)
				.expireAfterWrite(authenticationExpiredTimes, TimeUnit.MINUTES)
				.build(new CacheLoader<String, UserBo>() {
					@Override
					public UserBo load(String key) throws Exception {
						logger.debug("load " + key + " from mysql");
						User u = accountService.getUserService().getByUsername(key);
						// 得到用户的权限
						Role role = u.getRole();
						if (role == null) {
							return null;
						}
						return new UserBo(u, role);
//						return new UserBo(u.getUserName(), u.getPassword(), role.getId(), u.getId());
					}
				});
	}

	/**
	 * 重新覆盖掉它的验证逻辑
	 */
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
				: authentication.getName();
		UserBo user;
		try {
			//需要定时更新缓存，反正修改用户权限后不生效
			user = this.cache.get(username);
		} catch (ExecutionException e) {
			logger.warn(e + "");
			throw new UsernameNotFoundException("找不到用户" + username);
		}
		String presentedPassword;
		//如果没有验证过，验证密码
		if (!authentication.isAuthenticated()) {
			presentedPassword = authentication.getCredentials().toString();
			if (!user.getPassword().equals(presentedPassword)) {
				logger.debug("密码出错：" + username + ":" + presentedPassword);
				throw new BadCredentialsException("密码出错：" + username + ":"
						+ presentedPassword);
			}
		} else {
			presentedPassword = "";
		}
		// 在验证账户是否被冻结等等
		UsernamePasswordAuthenticationToken result = new Visitor(user, true);
		return result;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub

	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxCacheNum() {
		return maxCacheNum;
	}

	public void setMaxCacheNum(int maxCacheNum) {
		this.maxCacheNum = maxCacheNum;
	}

	public int getAuthenticationExpiredTimes() {
		return authenticationExpiredTimes;
	}

	public void setAuthenticationExpiredTimes(int authenticationExpiredTimes) {
		this.authenticationExpiredTimes = authenticationExpiredTimes;
	}
}
