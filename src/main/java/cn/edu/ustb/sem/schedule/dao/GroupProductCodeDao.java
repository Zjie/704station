package cn.edu.ustb.sem.schedule.dao;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.schedule.entity.GroupProductCode;

public interface GroupProductCodeDao extends BaseDao<GroupProductCode, Integer> {
	public GroupProductCode findGid(String productCode);
}
