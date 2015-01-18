package cn.edu.ustb.sem.process.service;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.process.entity.PProcess;
import cn.edu.ustb.sem.process.web.model.ProcessModel;
import cn.edu.ustb.sem.process.web.model.ProcessSearchForm;

public interface ProcessService extends BaseService<PProcess, ProcessModel, Integer> {
	public GridModel<ProcessModel> list(ProcessSearchForm form, ItemModelHelper<PProcess, ProcessModel> helper) throws ServiceException;
	public void saveOrUpdate(ProcessModel model) throws ServiceException;
}
