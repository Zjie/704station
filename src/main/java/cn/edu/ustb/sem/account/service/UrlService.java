package cn.edu.ustb.sem.account.service;

import cn.edu.ustb.sem.account.entity.Url;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;

public interface UrlService extends BaseService<Url, Object, Integer> {
	public Url getByUrl(String url) throws ServiceException;
}
