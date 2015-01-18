package cn.edu.ustb.sem.workerMg.service;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.schedule.entity.Worker;
import cn.edu.ustb.sem.workerMg.web.model.WorkerModel;

public interface WorkerService extends BaseService<Worker, WorkerModel, Integer>{

	public void saveOrUpdate(WorkerModel model) throws ServiceException;
	public void unbind(Integer workerId) throws ServiceException;
	public void freeze(Integer workerId) throws ServiceException;
}
