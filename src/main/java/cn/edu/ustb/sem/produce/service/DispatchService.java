package cn.edu.ustb.sem.produce.service;

import java.util.List;

import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.produce.web.model.DispatchResultModel;
import cn.edu.ustb.sem.schedule.entity.Worker;

public interface DispatchService {
	public List<DispatchResultModel> getCurrentDispatchResult(Visitor v) throws ServiceException;
	
	public List<DispatchResultModel> getOrderDispatchResult(Integer oid) throws ServiceException;
	
	public DispatchResultModel printDispatchResultModel(Integer srid, Integer uid) throws ServiceException;
	
	public Worker getWorker(Visitor v) throws ServiceException;
}
