package cn.edu.ustb.sem.produce.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.produce.dao.ProduceDianshiDao;
import cn.edu.ustb.sem.produce.entity.ProduceDianshi;

@Repository("produceDianshiDao")
public class ProduceDianshiDaoImpl extends BaseDaoImpl<ProduceDianshi, Integer> implements ProduceDianshiDao {

}
