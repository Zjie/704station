package cn.edu.ustb.sem.material.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.material.dao.MaterialTemplateDao;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;

@Repository("materialTemplateDao")
public class MaterialTemplateDaoImpl extends BaseDaoImpl<MaterialTemplate, Integer> implements MaterialTemplateDao {

	@Override
	public MaterialTemplate exists(String mtName) throws ServiceException {
		MaterialTemplate mt = new MaterialTemplate();
		mt.setName(mtName);
		return this.find(mt);
	}

}
