package cn.edu.ustb.sem.workerMg.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.dao.UserDao;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.schedule.dao.WorkerDao;
import cn.edu.ustb.sem.schedule.dao.WorkerTimelineDao;
import cn.edu.ustb.sem.schedule.entity.Worker;
import cn.edu.ustb.sem.schedule.entity.WorkerTimeLine;
import cn.edu.ustb.sem.workerMg.service.WorkerService;
import cn.edu.ustb.sem.workerMg.web.model.WorkerModel;

@Service("workerService")
public class WorkerServiceImpl extends BaseServiceImpl<Worker, WorkerModel, Integer> implements WorkerService{

	@Autowired
	private UserDao userDao;
	@Autowired
	private WorkerTimelineDao wtlDao;
	private WorkerDao workerDao;
	@Autowired
    @Qualifier("workerDao")
	@Override
	public void setBaseDao(BaseDao<Worker, Integer> baseDao) {
		this.baseDao = baseDao;
		this.workerDao = (WorkerDao) baseDao;
	}
	@Override
	public void saveOrUpdate(WorkerModel model) throws ServiceException {
		Worker worker = new Worker();
		if(model.getId() != 0){
			worker.setId(model.getId());
		}
		worker.setRealName(model.getRealName());
		worker.setUnit(model.getUnit());
		Integer id = model.getUid();
		if(id != null){
			User user = userDao.get(id);
			user.setIsBanded((byte)1);
			userDao.saveOrUpdate(user);
			worker.setUser(user);
		}
		workerDao.saveOrUpdate(worker);
		//如果该工人是新增的，则
		if (model.getId() == 0) {
			//为其开辟一条新的timeline
			WorkerTimeLine wtl = new WorkerTimeLine();
			wtl.setWorker(worker);
			wtl.setStart(Calendar.getInstance());
			wtl.setDuration(8);
			this.wtlDao.save(wtl);
		}
	}
	@Override
	public void unbind(Integer workerId) throws ServiceException {
		if (workerId == null || workerId <= 0) {
			throw new ServiceException("工人id不能为空");
		}
		Worker w = this.get(workerId);
		w.setUser(null);
		this.saveOrUpdate(w);
	}
	@Override
	public void freeze(Integer workerId) throws ServiceException {
		if (workerId == null) return;
		Worker w = this.workerDao.get(workerId);
		if (w == null) throw new ServiceException("该工人不存在");
		w.setIsFreezed(Worker.IS_FREEZED);
		this.workerDao.update(w);
	}

}
