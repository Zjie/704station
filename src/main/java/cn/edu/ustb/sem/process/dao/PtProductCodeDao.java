package cn.edu.ustb.sem.process.dao;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
import cn.edu.ustb.sem.process.entity.PtProductCode;

public interface PtProductCodeDao extends BaseDao<PtProductCode, Integer> {
	public void updateProductCodeForPt(ProcessTemplate pt);
}
