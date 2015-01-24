package cn.edu.ustb.sem.assign.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.assign.dao.DispatchMaterialDao;
import cn.edu.ustb.sem.assign.entity.DispatchMaterial;
import cn.edu.ustb.sem.assign.service.DispatchMaterialService;
import cn.edu.ustb.sem.assign.web.model.DispatchMaterialModel;
import cn.edu.ustb.sem.assign.web.model.DispatchMaterialSearchForm;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.order.dao.OrderDao;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.schedule.entity.Worker;

@Service("dispatchMaterialService")
public class DispatchMaterialServiceImpl extends BaseServiceImpl<DispatchMaterial, DispatchMaterialModel, Integer> 
	implements DispatchMaterialService {

	@Autowired
	private DispatchMaterialDao baseDao;
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public GridModel<DispatchMaterialModel> list(DispatchMaterialSearchForm form)
			throws ServiceException {
		DispatchMaterial model = new DispatchMaterial();
		if (form.getNo() != null && !form.getNo().isEmpty()) {
			Order om = new Order();
			om.setNo(form.getNo());
			Order o = this.orderDao.find(om);
			if (o != null) {
				model.setOrder(new Order(o.getId()));
			}
		}
		if (form.getWorkerId() != null) {
			model.setWorker(new Worker(form.getWorkerId()));
		}
		GridModel<DispatchMaterialModel> grid = new GridModel<DispatchMaterialModel>();
		Integer count = this.baseDao.count(model);
    	List<DispatchMaterial> items = this.baseDao.listAll(model, form.getPage(), form.getLimit());
		grid.setTotalNum(count);
		List<DispatchMaterialModel> itModel = new ArrayList<DispatchMaterialModel>();
		for (DispatchMaterial m : items) {
			itModel.add(new DispatchMaterialModel(m));
		}
		grid.setItems(itModel);
		return grid;
	}

	@Autowired
	@Qualifier("dispatchMaterialDao")
	@Override
	public void setBaseDao(BaseDao<DispatchMaterial, Integer> baseDao) {
		this.baseDao = (DispatchMaterialDao) baseDao;
	}
	
}
