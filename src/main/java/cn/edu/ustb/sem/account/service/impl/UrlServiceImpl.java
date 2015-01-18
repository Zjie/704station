package cn.edu.ustb.sem.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.dao.UrlDao;
import cn.edu.ustb.sem.account.entity.Url;
import cn.edu.ustb.sem.account.service.UrlService;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;

@Service("urlService")
public class UrlServiceImpl extends BaseServiceImpl<Url, Object, Integer> implements UrlService {
	private UrlDao urlDao;
    @Autowired
    @Qualifier("urlDao")
	@Override
	public void setBaseDao(BaseDao<Url, Integer> urlDao) {
    	this.baseDao = urlDao;
    	this.urlDao = (UrlDao) urlDao;
	}
	@Override
	public Url getByUrl(String url) throws ServiceException {
		List<Url> urls = urlDao.list("from Url where url = ?", 0, 5, url);
		if (urls == null || url.isEmpty()) {
			return null;
		}
		return urls.get(0);
	}

}
