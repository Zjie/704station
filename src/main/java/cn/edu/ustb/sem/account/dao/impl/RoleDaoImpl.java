package cn.edu.ustb.sem.account.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.account.dao.RoleDao;
import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
@Repository("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role, Integer> implements RoleDao {

}
