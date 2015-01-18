package cn.edu.ustb.sem.produce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;
import cn.edu.ustb.sem.produce.service.ProduceAssemblingService;
import cn.edu.ustb.sem.produce.web.model.ProduceAssembingModel;
@Service("produceAssemblingService")
public class ProduceAssemblingServiceImpl extends BaseServiceImpl<ProduceAssembling, ProduceAssembingModel, Integer> 
	implements ProduceAssemblingService{
	@Autowired
    @Qualifier("produceAssemblingDao")
	@Override
	public void setBaseDao(BaseDao<ProduceAssembling, Integer> baseDao) {
		this.baseDao = baseDao;
	}

}
