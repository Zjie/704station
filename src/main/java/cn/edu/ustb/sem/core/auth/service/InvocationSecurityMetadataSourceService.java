package cn.edu.ustb.sem.core.auth.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import cn.edu.ustb.sem.account.entity.Application;
import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.entity.Url;
import cn.edu.ustb.sem.account.service.AccountService;
import cn.edu.ustb.sem.core.auth.bo.AuthAttribute;
import cn.edu.ustb.sem.core.exception.ServiceException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。 此类在初始化时，应该取到所有资源及其对应角色的定义。
 * 
 */
public class InvocationSecurityMetadataSourceService implements
		FilterInvocationSecurityMetadataSource, ApplicationListener<ContextRefreshedEvent> {
	protected final Log logger = LogFactory.getLog(getClass());
	private static PathMatcher urlMatcher = new AntPathMatcher();
	private static LoadingCache<String, Collection<ConfigAttribute>> cache;
	private int maximumSize;
	private int refreshAfterWrite;
	@Autowired
	private AccountService accountService;
	private void initCache() {
		//自动刷新缓存
		cache = CacheBuilder.newBuilder()
				.maximumSize(maximumSize)
				.refreshAfterWrite(refreshAfterWrite, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Collection<ConfigAttribute>>() {
					@Override
					public Collection<ConfigAttribute> load(String key) throws Exception {
						logger.debug("load " + key + " from mysql");
						Url url = accountService.getUrlService().getByUrl(key);
						Set<ConfigAttribute> attrs = new HashSet<ConfigAttribute>();
						Application app = url.getApp();
						Set<Role> roles = app.getRoles();
						for (Role r : roles) {
							attrs.add(new AuthAttribute(r.getId() + ""));
						}
						return attrs;
					}
				});
	}
	public void init() throws ExecutionException, ServiceException {
		initCache();
		// 在Web服务器启动时，提取系统中的所有权限。
		List<Url> urls = accountService.getUrlService().listAll();
		/*
		 * 应当是资源为key， 权限为value。 资源通常为url， 权限就是roleId。 一个url可以由多个role来访问
		 */
		for (Url url : urls) {
			String uStr = url.getUrl();
			Set<ConfigAttribute> attrs = (Set<ConfigAttribute>) cache.get(uStr);
			if (attrs != null) {
				Application app = url.getApp();
				Set<Role> roles = app.getRoles();
				for (Role r : roles) {
					attrs.add(new AuthAttribute(r.getId() + ""));
				}
				cache.put(uStr, attrs);
			} else {
				attrs = new HashSet<ConfigAttribute>();
				Application app = url.getApp();
				Set<Role> roles = app.getRoles();
				for (Role r : roles) {
					attrs.add(new AuthAttribute(r.getId() + ""));
				}
				cache.put(uStr, attrs);
			}
		}
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	// 根据URL，找到相关的权限配置。
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		// object 是一个URL，被用户请求的url。
		String url = ((FilterInvocation) object).getRequestUrl();
		int firstQuestionMarkIndex = url.indexOf("?");
		if (firstQuestionMarkIndex != -1) {
			url = url.substring(0, firstQuestionMarkIndex);
		}
		
		Map<String, Collection<ConfigAttribute>> kvs = cache.asMap();
		Iterator<String> ite = kvs.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			if (urlMatcher.match(resURL, url)) {
				try {
					return cache.get(resURL);
				} catch (ExecutionException e) {
					logger.warn(e + "");
				}
			}
		}
		return new HashSet<ConfigAttribute>();
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			init();
		} catch (ExecutionException e) {
			logger.warn(e + "");
		} catch (ServiceException e) {
			logger.warn(e + "");
		}
	}
	public int getMaximumSize() {
		return maximumSize;
	}
	public void setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
	}
	public int getRefreshAfterWrite() {
		return refreshAfterWrite;
	}
	public void setRefreshAfterWrite(int refreshAfterWrite) {
		this.refreshAfterWrite = refreshAfterWrite;
	}

}
