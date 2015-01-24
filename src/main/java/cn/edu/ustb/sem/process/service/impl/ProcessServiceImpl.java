package cn.edu.ustb.sem.process.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.process.dao.ProcessDao;
import cn.edu.ustb.sem.process.dao.PtpDao;
import cn.edu.ustb.sem.process.entity.PProcess;
import cn.edu.ustb.sem.process.entity.Ptp;
import cn.edu.ustb.sem.process.service.ProcessService;
import cn.edu.ustb.sem.process.web.model.ProcessModel;
import cn.edu.ustb.sem.process.web.model.ProcessSearchForm;

@Service("processService")
public class ProcessServiceImpl extends
		BaseServiceImpl<PProcess, ProcessModel, Integer> implements
		ProcessService {
	@SuppressWarnings("unused")
	private ProcessDao processDao;
	@Autowired
	private PtpDao ptpDao;

	@Autowired
	@Qualifier("processDao")
	@Override
	public void setBaseDao(BaseDao<PProcess, Integer> baseDao) {
		this.baseDao = baseDao;
		this.processDao = (ProcessDao) baseDao;
	}

	@Override
	public GridModel<ProcessModel> list(ProcessSearchForm form,
			ItemModelHelper<PProcess, ProcessModel> helper)
			throws ServiceException {
		String hql = "from " + PProcess.class.getSimpleName()
				+ " p where 1 = 1";
		String countHql = "select count(*) from "+ PProcess.class.getSimpleName()
				+ " p where 1 = 1";
		List<Object> params = new ArrayList<Object>();
		String content = form.getContent();
		String phase = form.getPhase();
		int groupName = form.getGroupName();
		if (content != null && !content.isEmpty()) {
			hql += " and p.content like ?";
			countHql += " and p.content like ?";
			params.add("%" + content + "%");
		}
		if (phase != null && !phase.isEmpty()) {
			hql += " and p.phase = ?";
			countHql += " and p.phase = ?";
			params.add(phase);
		}
		if (groupName > 0) {
			hql += " and p.group = ?";
			countHql += " and p.group = ?";
			params.add(groupName);
		}
		List<PProcess> processes = this.baseDao.list(hql, form.getPage(),
				form.getLimit(), params.toArray());
		int pageCount = this.baseDao.count(countHql, params.toArray());
		GridModel<ProcessModel> grid = new GridModel<ProcessModel>();
		List<ProcessModel> itModel = new ArrayList<ProcessModel>();
		for (PProcess m : processes) {
			itModel.add(helper.transfer(m));
		}
		grid.setItems(itModel);
		grid.setTotalNum(pageCount);
		return grid;
	}

	@Override
	public void delete(Integer pid) throws ServiceException {
		// 验证没有模板引用该工序
		if (pid == null) {
			throw new ServiceException("工序id不能为空");
		}
		Ptp form = new Ptp();
		form.setProcess(new PProcess(pid));
		List<Ptp> result = this.ptpDao.listAll(form, -1, -1);
		if (result != null && result.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Ptp ptp : result) {
				sb.append(ptp.getPt().getName()).append(", ");
			}
			sb.delete(sb.length() - 2, sb.length());
			throw new ServiceException("该工序被" + sb.toString()
					+ "等工序模板引用，请先删除引用的工序模板。");
		}
		this.baseDao.delete(pid);
	}

	@Override
	public void saveOrUpdate(ProcessModel model) throws ServiceException {
		if (model == null) {
			throw new ServiceException("参数出错");
		}
		if (model.getId() > 0) {
			save(model);
		} else {
			update(model);
		}
	}
	private void save(ProcessModel model) throws ServiceException {
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = new User(visitor.getUid());
		Calendar udate = Calendar.getInstance();
		PProcess process = new PProcess();
		process.setContent(model.getContent());
		process.setGroup(model.getGroupName());
		process.setPhase(model.getPhase());
		//验证不能重复
		Page<PProcess> exists = this.listAll(process, -1, -1);
		List<PProcess> items = exists.getItems();
		if (items != null) {
			for (PProcess p : items) {
				if (p.getId() != model.getId()) {
					throw new ServiceException("工序重复，请修改其生产阶段、工作内容、工序组");
				}
			}
		}
		process.setId(model.getId());
		process.setBase(model.getBase());
		process.setMeasure(model.getMeasure());
		process.setRemark(model.getRemark());
		process.setUdate(udate);
		process.setUpdater(u);
		this.baseDao.merge(process);
	}
	private void update(ProcessModel model) throws ServiceException {
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = new User(visitor.getUid());
		Calendar udate = Calendar.getInstance();
		PProcess process = new PProcess();
		process.setContent(model.getContent());
		process.setGroup(model.getGroupName());
		process.setPhase(model.getPhase());
		//验证不能重复
		PProcess exists = this.baseDao.find(process);
		if (exists != null) {
			throw new ServiceException("工序重复，请修改其生产阶段、工作内容、工序组");
		}
		process.setBase(model.getBase());
		process.setMeasure(model.getMeasure());
		process.setRemark(model.getRemark());
		process.setUdate(udate);
		process.setUpdater(u);
		this.baseDao.save(process);
	}
}
