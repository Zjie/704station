package cn.edu.ustb.sem.schedule.service;

import java.util.List;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.schedule.entity.GroupProductCode;
import cn.edu.ustb.sem.schedule.web.model.GroupUnitProcessModel;
import cn.edu.ustb.sem.schedule.web.model.ProductGroupModel;

public interface ScheduleInfoService {
	public List<ProductGroupModel> list(GroupProductCode model) throws ServiceException;
	public void saveOrUpdateGroup(ProductGroupModel pgm) throws ServiceException;
	public void deleteGroup(int id) throws ServiceException;
	public void deleteGroupProcess(int id) throws ServiceException;
	public List<GroupUnitProcessModel> list(GroupUnitProcessModel form) throws ServiceException;
	public void saveOrUpdateGroupProcess(GroupUnitProcessModel gupm) throws ServiceException;
}
