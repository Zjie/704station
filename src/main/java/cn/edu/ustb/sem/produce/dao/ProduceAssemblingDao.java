package cn.edu.ustb.sem.produce.dao;

import java.util.List;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;

public interface ProduceAssemblingDao extends BaseDao<ProduceAssembling, Integer>{
	public List<ProduceAssembling> getPrByOrderId(Integer orderId);
}
