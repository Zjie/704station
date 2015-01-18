package cn.edu.ustb.sem.material.dao;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.entity.MtProductCode;

public interface ProductCodeDao extends BaseDao<MtProductCode, Integer> {
	public void updateProductCode4Mt(MaterialTemplate mt) throws ServiceException;
	public boolean exists(String pc) throws ServiceException;
}
