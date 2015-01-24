package cn.edu.ustb.sem.assign.service;

import cn.edu.ustb.sem.assign.entity.DispatchMaterial;
import cn.edu.ustb.sem.assign.web.model.DispatchMaterialModel;
import cn.edu.ustb.sem.assign.web.model.DispatchMaterialSearchForm;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.core.web.model.GridModel;

public interface DispatchMaterialService extends BaseService<DispatchMaterial, DispatchMaterialModel, Integer> {
	GridModel<DispatchMaterialModel> list(DispatchMaterialSearchForm form) throws ServiceException;
}
