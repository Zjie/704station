package cn.edu.ustb.sem.process.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.process.dao.ProcessDao;
import cn.edu.ustb.sem.process.entity.PProcess;
@Repository("processDao")
public class ProcessDaoImpl extends BaseDaoImpl<PProcess, Integer> implements ProcessDao {

}
