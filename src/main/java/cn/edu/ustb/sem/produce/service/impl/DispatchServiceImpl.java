package cn.edu.ustb.sem.produce.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.produce.dao.PrintHistoryDao;
import cn.edu.ustb.sem.produce.entity.PrintHistory;
import cn.edu.ustb.sem.produce.service.DispatchService;
import cn.edu.ustb.sem.produce.web.model.DispatchResultModel;
import cn.edu.ustb.sem.schedule.dao.DispatchUnitDao;
import cn.edu.ustb.sem.schedule.dao.ScheduleResultDao;
import cn.edu.ustb.sem.schedule.dao.WorkerDao;
import cn.edu.ustb.sem.schedule.entity.DispatchUnit;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;
import cn.edu.ustb.sem.schedule.entity.ScheduleResult;
import cn.edu.ustb.sem.schedule.entity.Worker;
@Service("dispatchService")
public class DispatchServiceImpl implements DispatchService {
	@Autowired
	private WorkerDao workerDao;
	@Autowired
	private PrintHistoryDao phDao;
	@Autowired
	private ScheduleResultDao srDao;
	@Autowired
	private DispatchUnitDao duDao;
	@Override
	public List<DispatchResultModel> getCurrentDispatchResult(Visitor v)
			throws ServiceException {
		Worker model = new Worker();
		User u = new User();
		u.setId(v.getUid());
		model.setUser(u);
		Worker w = workerDao.find(model);
		//查找属于这个单元下的排产计划，并且排产结果是调度员确认过的
		ScheduleResult sr = new ScheduleResult();
		//只选出用户确认的排产结果
		sr.setStatus(ScheduleResult.COMMIT);
		Worker wo = new Worker();
		//只选出该工人所属的生产单元下的排产结果
		wo.setUnit(w.getUnit());
		sr.setWorker(wo);
		List<ScheduleResult> results = srDao.listAll(sr, -1, -1);
		List<DispatchResultModel> dispatchResult = new ArrayList<DispatchResultModel>(results.size());
		//保存已经选出来过的订单
		Set<Integer> exists = new HashSet<Integer>();
		for (ScheduleResult s : results) {
			Order order = s.getOrder();
			//如果报工完成，则不再显示出来
			if (order.getIsReported() == 1) continue;
			if (exists.contains(order.getId())) continue;
			exists.add(order.getId());
			dispatchResult.add(new DispatchResultModel(s));
		}
		//加上生产单元调整的订单
		DispatchUnit duModel = new DispatchUnit();
		model.setUnit(w.getUnit());
		List<DispatchUnit> dus = this.duDao.listAll(duModel, -1, -1);
		for (DispatchUnit du : dus) {
			Order order = du.getOrder();
			//如果报工完成，则不再显示出来
			if (order.getIsReported() == 1) continue;
			if (exists.contains(order.getId())) continue;
			exists.add(order.getId());
			DispatchResultModel drm = new DispatchResultModel();
			drm.setId(-1);
			drm.setOid(order.getId());
			drm.setOrder(new OrderModel(order));
			dispatchResult.add(drm);
		}
		return dispatchResult;
	}
	@Override
	public List<DispatchResultModel> getOrderDispatchResult(Integer oid)
			throws ServiceException {
		List<ScheduleResult> results = srDao.findOrderSchedultResult(oid);
		List<DispatchResultModel> dispatchResult = new ArrayList<DispatchResultModel>(results.size());
		//保存已经选出来过的工序组
		Set<Integer> exist = new HashSet<Integer>();
		for (ScheduleResult s : results) {
			GroupUnitProcess gup = s.getGup();
			if (exist.contains(gup.getId())) continue;
			exist.add(gup.getId());
			dispatchResult.add(new DispatchResultModel(s));
		}
		return dispatchResult;
	}
	@Override
	public DispatchResultModel printDispatchResultModel(Integer srid, Integer uid)
			throws ServiceException {
		//更新这个工序组的状态
		ScheduleResult exist = srDao.get(srid);
		srDao.updateSchedultResultStatus(exist);
		PrintHistory ph = new PrintHistory();
		ph.setPrintDate(Calendar.getInstance());
		ph.setOrder(exist.getOrder());
		ph.setGup(exist.getGup());
		ph.setPrinter(exist.getWorker());
		phDao.save(ph);
		return new DispatchResultModel(exist);
	}
	@Override
	public Worker getWorker(Visitor v) throws ServiceException {
		Worker model = new Worker();
		User u = new User();
		u.setId(v.getUid());
		model.setUser(u);
		Worker w = workerDao.find(model);
		return w;
	}

}
