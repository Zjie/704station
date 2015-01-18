package cn.edu.ustb.sem.account.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.account.dao.UserDao;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {

}