package cn.edu.ustb.sem.material.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.material.dao.MtmDao;
import cn.edu.ustb.sem.material.entity.Mtm;
@Repository("mtmDao")
public class MtmDaoImpl extends BaseDaoImpl<Mtm, Integer> implements MtmDao {

}
