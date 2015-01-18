package cn.edu.ustb.sem.assign.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.assign.dao.AssignDao;
import cn.edu.ustb.sem.assign.entity.Assign;
import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;

@Repository("assignDao")
public class AssignDaoImpl extends BaseDaoImpl<Assign, Integer> implements AssignDao{

}
