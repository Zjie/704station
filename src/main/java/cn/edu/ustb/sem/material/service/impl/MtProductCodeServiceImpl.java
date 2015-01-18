package cn.edu.ustb.sem.material.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.material.dao.ProductCodeDao;
import cn.edu.ustb.sem.material.entity.MtProductCode;
import cn.edu.ustb.sem.material.service.MtProductCodeService;
@Service("mtProductCodeService")
public class MtProductCodeServiceImpl extends BaseServiceImpl<MtProductCode, Object, Integer> implements MtProductCodeService{
	@SuppressWarnings("unused")
	private ProductCodeDao productCodeDao;
	@Autowired
	@Qualifier("productCodeDao")
	@Override
	public void setBaseDao(BaseDao<MtProductCode, Integer> baseDao) {
		this.baseDao = baseDao;
		this.productCodeDao = (ProductCodeDao) baseDao;
	}

}
