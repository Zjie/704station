package cn.edu.ustb.sem.schedule.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.schedule.dao.DispatchUnitDao;
import cn.edu.ustb.sem.schedule.entity.DispatchUnit;
@Repository("dispatchUnitDao")
public class DispatchUnitDaoImpl extends BaseDaoImpl<DispatchUnit, Integer> implements DispatchUnitDao {
	
}
