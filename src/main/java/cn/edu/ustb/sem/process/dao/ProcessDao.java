package cn.edu.ustb.sem.process.dao;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.process.entity.PProcess;
@Repository("processDao")
public interface ProcessDao extends BaseDao<PProcess, Integer> {

}