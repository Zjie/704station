package cn.edu.ustb.sem.kpi.service;

import java.util.Collection;
import java.util.List;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.kpi.web.model.OrderProduceInfo;

public interface KpiService {
	public List<OrderProduceInfo> listCurPlan() throws ServiceException;
	public Collection<OrderProduceInfo> listAYearPlan() throws ServiceException;
}
