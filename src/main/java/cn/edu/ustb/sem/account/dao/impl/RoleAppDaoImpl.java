package cn.edu.ustb.sem.account.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.account.dao.RoleAppDao;
import cn.edu.ustb.sem.account.entity.RoleApplication;
import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
@Repository("roleAppDao")
public class RoleAppDaoImpl extends BaseDaoImpl<RoleApplication, Integer> implements RoleAppDao {

}
