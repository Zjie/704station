package cn.edu.ustb.sem.account.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.account.dao.ApplicationDao;
import cn.edu.ustb.sem.account.entity.Application;
import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
@Repository("applicationDao")
public class ApplicationDaoImpl extends BaseDaoImpl<Application, Integer> implements ApplicationDao {

}
