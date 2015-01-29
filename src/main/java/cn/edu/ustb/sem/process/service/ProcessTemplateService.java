package cn.edu.ustb.sem.process.service;

import org.apache.poi.ss.usermodel.Sheet;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
import cn.edu.ustb.sem.process.web.model.ProcessTemplateModel;
import cn.edu.ustb.sem.process.web.model.ProcessTemplateSearchForm;

public interface ProcessTemplateService extends BaseService<ProcessTemplate, ProcessTemplateModel, Integer> {
	public boolean saveSheet(Sheet sheet) throws ServiceException;
	public void saveOrUpdate(ProcessTemplateModel model) throws ServiceException;
	public GridModel<ProcessTemplateModel> list(ProcessTemplateSearchForm form, ItemModelHelper<ProcessTemplate, ProcessTemplateModel> helper) throws ServiceException;
}
