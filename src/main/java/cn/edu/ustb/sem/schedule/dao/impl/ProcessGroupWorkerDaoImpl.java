package cn.edu.ustb.sem.schedule.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.schedule.dao.ProcessGroupWorkerDao;
import cn.edu.ustb.sem.schedule.entity.ProcessGroupWorker;
@Repository("processGroupWorkerDao")
public class ProcessGroupWorkerDaoImpl extends BaseDaoImpl<ProcessGroupWorker, Integer> implements ProcessGroupWorkerDao {

}
