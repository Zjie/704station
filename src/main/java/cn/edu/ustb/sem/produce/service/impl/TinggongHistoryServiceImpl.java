package cn.edu.ustb.sem.produce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;
import cn.edu.ustb.sem.produce.service.TinggongHistoryService;
import cn.edu.ustb.sem.produce.web.model.TinggongModel;
@Service("tinggongService")
public class TinggongHistoryServiceImpl extends BaseServiceImpl<ProduceTinggong, TinggongModel, Integer> implements TinggongHistoryService {

	@Autowired
    @Qualifier("produceTinggongDao")
	@Override
	public void setBaseDao(BaseDao<ProduceTinggong, Integer> baseDao) {
		this.baseDao = baseDao;
	}

}
