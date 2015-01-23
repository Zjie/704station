package cn.edu.ustb.sem.schedule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.schedule.dao.GroupProductCodeDao;
import cn.edu.ustb.sem.schedule.dao.GroupUnitProcessDao;
import cn.edu.ustb.sem.schedule.dao.ProcessGroupWorkerDao;
import cn.edu.ustb.sem.schedule.entity.GroupProductCode;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.ProcessGroupWorker;
import cn.edu.ustb.sem.schedule.service.ScheduleInfoService;
import cn.edu.ustb.sem.schedule.web.model.GroupUnitProcessModel;
import cn.edu.ustb.sem.schedule.web.model.ProcessWorkerModel;
import cn.edu.ustb.sem.schedule.web.model.ProductGroupModel;
@Service("scheduleInfoService")
public class ScheduleInfoServiceImpl implements ScheduleInfoService {
	@Autowired
	private GroupProductCodeDao gpcDao;
	@Autowired
	private GroupUnitProcessDao gupDao;
	@Autowired
	private ProcessGroupWorkerDao pgwDao;
	@Override
	public List<ProductGroupModel> list(GroupProductCode model)
			throws ServiceException {
		List<GroupProductCode> data = this.gpcDao.listAll(model, -1, -1);
		List<ProductGroupModel> result = new ArrayList<ProductGroupModel>();
		for (GroupProductCode gpc : data) {
			result.add(new ProductGroupModel(gpc));
		}
		return result;
	}
	@Override
	public void saveOrUpdateGroup(ProductGroupModel pgm)
			throws ServiceException {
		if (pgm.getGroupId() == 0) {
			throw new ServiceException("产品族编号不能为空");
		}
		if (pgm.getProductCode() == null || pgm.getProductCode().isEmpty()) {
			throw new ServiceException("产品族绑定的产品代号不能为空");
		}
		if (pgm.getId() > 0) {
			this.gpcDao.update(new GroupProductCode(pgm));
		} else {
			GroupProductCode model = new GroupProductCode();
			model.setGroupId(pgm.getGroupId());
			if (this.gpcDao.find(model) != null) {
				throw new ServiceException("产品族编号" + pgm.getGroupId() + "已存在，请使用别的编号");
			}
			this.gpcDao.save(new GroupProductCode(pgm));
		}
		
	}
	@Override
	public void deleteGroup(int id) throws ServiceException {
		GroupProductCode gpc = this.gpcDao.get(id);
		GroupUnitProcess model = new GroupUnitProcess();
		model.setGroupId(gpc.getGroupId());
		List<GroupUnitProcess> gups = this.gupDao.listAll(model, -1, -1);
		if (gups != null && gups.size() > 0) {
			throw new ServiceException("改产品族被若干工序组绑定，请到\"排产元数据管理-工序组信息\"中删除管理的工序组");
		}
		this.gpcDao.delete(id);
	}
	@Override
	public void deleteGroupProcess(int id) throws ServiceException { 
		//先查看是否有后序工序组，如果有则不能删除
		GroupUnitProcess gup = this.gupDao.get(id);
		Integer after = gup.getAfterProcessGroup();
		if (after != null) {
			GroupUnitProcess afterModel = new GroupUnitProcess();
			afterModel.setProcessGroup(after);
			if (this.gupDao.find(afterModel) != null) {
				throw new ServiceException("该工序组被后序工序组所依赖，请先删除其后序工序组");
			}
		}
		//要删除与其相关的工人对应关系
		ProcessGroupWorker pgw = new ProcessGroupWorker();
		pgw.setGup(new GroupUnitProcess(id));
		this.pgwDao.deleteObject(pgw);
		this.gupDao.delete(id);
	}
	@Override
	public List<GroupUnitProcessModel> list(GroupUnitProcessModel form) throws ServiceException{
		GroupUnitProcess model = new GroupUnitProcess(form);
		List<GroupUnitProcess> data = this.gupDao.listAll(model, -1, -1);
		List<GroupProductCode> gpcs = this.gpcDao.listAll();
		Map<Integer, String> idNameMap = new HashMap<Integer, String>();
		for (GroupProductCode gpc : gpcs) {
			idNameMap.put(gpc.getGroupId(), gpc.getGroupName());
		}
		
		List<GroupUnitProcessModel> result = new ArrayList<GroupUnitProcessModel>();
		for (GroupUnitProcess gup : data) {
			GroupUnitProcessModel gpm = new GroupUnitProcessModel(gup);
			String gupName = idNameMap.get(gpm.getGroupId());
			if (gupName != null) gpm.setGroupName(gupName);
			result.add(gpm);
		}
		return result;
	}
	@Override
	public void saveOrUpdateGroupProcess(GroupUnitProcessModel gupm)
			throws ServiceException {
		GroupUnitProcess gup = new GroupUnitProcess(gupm);
		
		if (gup.getId() != null && gup.getId() >= 0) {
			//删除原有的工人-工序组关联信息
			ProcessGroupWorker model = new ProcessGroupWorker();
			model.setGup(new GroupUnitProcess(gup.getId()));
			this.pgwDao.deleteObject(model);
			this.gupDao.merge(gup);
//			this.gupDao.saveOrUpdate(gup);
		} else {
			this.gupDao.save(gup);
		}
		this.gupDao.flush();
		updateProcessGroupWorker(gup.getId(), gupm);
	}
	private void updateProcessGroupWorker(Integer gupId, GroupUnitProcessModel gupm) {
		//增加新的工人-工序组信息
		List<ProcessWorkerModel> pwms = gupm.getPgws();
		for (ProcessWorkerModel pwm : pwms) {
			pwm.setGupId(gupId);
			ProcessGroupWorker pgw = new ProcessGroupWorker(pwm);
			this.pgwDao.save(pgw);
		}
	}
}
