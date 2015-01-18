package cn.edu.ustb.sem.account.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.account.dao.UrlDao;
import cn.edu.ustb.sem.account.entity.Url;
import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
@Repository("urlDao")
public class UrlDaoImpl extends BaseDaoImpl<Url, Integer> implements UrlDao {

}
