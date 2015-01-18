package cn.edu.ustb.sem.produce.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.produce.dao.PrintHistoryDao;
import cn.edu.ustb.sem.produce.entity.PrintHistory;
@Repository("printHistoryDao")
public class PrintHistoryDaoImpl extends BaseDaoImpl<PrintHistory, Integer> implements PrintHistoryDao {

}
