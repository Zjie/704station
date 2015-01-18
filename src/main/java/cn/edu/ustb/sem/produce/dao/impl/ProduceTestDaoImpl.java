package cn.edu.ustb.sem.produce.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.produce.dao.ProduceTestDao;
import cn.edu.ustb.sem.produce.entity.ProduceTest;

@Repository("produceTestDao")
public class ProduceTestDaoImpl extends BaseDaoImpl<ProduceTest, Integer> implements ProduceTestDao {

}
