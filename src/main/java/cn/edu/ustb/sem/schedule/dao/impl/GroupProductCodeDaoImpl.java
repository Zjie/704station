package cn.edu.ustb.sem.schedule.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.schedule.dao.GroupProductCodeDao;
import cn.edu.ustb.sem.schedule.entity.GroupProductCode;
@Repository("groupProductCodeDao")
public class GroupProductCodeDaoImpl extends BaseDaoImpl<GroupProductCode, Integer> implements GroupProductCodeDao {
	private static final String TABLE = GroupProductCode.class.getSimpleName();
	@Override
	public GroupProductCode findGid(String productCode) {
		String hql = "from " + TABLE + " g where g.productCode like ?";
		List<GroupProductCode> pcs = this.list(hql, -1, -1, "%" + productCode + "%");
		if (pcs != null && pcs.size() > 0) {
			return pcs.get(0);
		}
		return null;
	}

}
