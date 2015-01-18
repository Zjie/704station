package cn.edu.ustb.sem.material.service;

import org.apache.poi.ss.usermodel.Workbook;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateModel;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateSearchModel;

public interface MaterialTemplateService extends BaseService<MaterialTemplate, MaterialTemplateModel, Integer>{
	public void saveExcelFile(Workbook hssfWorkbook) throws ServiceException;
	public void saveOrUpdate(MaterialTemplateModel mtl) throws ServiceException;
	public GridModel<MaterialTemplateModel> list(MaterialTemplateSearchModel form, ItemModelHelper<MaterialTemplate, MaterialTemplateModel> helper) throws ServiceException;
	public MaterialTemplate findByProductCode(String pc) throws ServiceException;
}
