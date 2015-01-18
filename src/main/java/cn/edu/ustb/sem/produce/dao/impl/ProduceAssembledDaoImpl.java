package cn.edu.ustb.sem.produce.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.produce.dao.ProduceAssembledDao;
import cn.edu.ustb.sem.produce.entity.ProduceAssembled;

@Repository("produceAssembledDao")
public class ProduceAssembledDaoImpl extends BaseDaoImpl<ProduceAssembled, Integer> implements ProduceAssembledDao {

}
