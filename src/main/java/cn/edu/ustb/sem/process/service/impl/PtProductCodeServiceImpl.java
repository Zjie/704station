package cn.edu.ustb.sem.process.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.process.dao.PtProductCodeDao;
import cn.edu.ustb.sem.process.entity.PtProductCode;
import cn.edu.ustb.sem.process.service.PtProductCodeService;
@Service("ptProductCodeService")
public class PtProductCodeServiceImpl extends BaseServiceImpl<PtProductCode, Object, Integer> implements PtProductCodeService {
	@SuppressWarnings("unused")
	private PtProductCodeDao ptProductCodeDao;
	
	@Autowired
	@Qualifier("ptProductCodeDao")
	@Override
	public void setBaseDao(BaseDao<PtProductCode, Integer> baseDao) {
		ptProductCodeDao = (PtProductCodeDao)baseDao;
		this.baseDao = baseDao;
	}

}
