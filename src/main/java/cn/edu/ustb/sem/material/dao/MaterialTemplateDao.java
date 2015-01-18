package cn.edu.ustb.sem.material.dao;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;

public interface MaterialTemplateDao extends BaseDao<MaterialTemplate, Integer> {
	public MaterialTemplate exists(String mtName) throws ServiceException;
}
