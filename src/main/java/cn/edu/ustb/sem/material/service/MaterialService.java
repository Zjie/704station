package cn.edu.ustb.sem.material.service;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.material.entity.Material;
import cn.edu.ustb.sem.material.web.model.MaterialModel;

public interface MaterialService extends BaseService<Material, MaterialModel, Integer> {
	public void save(MaterialModel mm) throws ServiceException;
}
