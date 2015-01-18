package cn.edu.ustb.sem.process.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.process.dao.PtpDao;
import cn.edu.ustb.sem.process.entity.Ptp;
@Repository("ptpDao")
public class PtpDaoImpl extends BaseDaoImpl<Ptp, Integer> implements PtpDao {

}
