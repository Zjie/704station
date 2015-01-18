package cn.edu.ustb.sem.schedule.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.schedule.dao.UnscheduleProcessGroupDao;
import cn.edu.ustb.sem.schedule.entity.UnscheduleProcessGroup;
@Repository("unscheduleProcessGroupDao")
public class UnscheduleProcessGroupDaoImpl extends BaseDaoImpl<UnscheduleProcessGroup, Integer> implements UnscheduleProcessGroupDao {

}
