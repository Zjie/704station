package cn.edu.ustb.sem.produce.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.produce.dao.ProduceTinggongDao;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;

@Repository("produceTinggongDao")
public class ProduceTinggongDaoImpl extends BaseDaoImpl<ProduceTinggong, Integer> implements ProduceTinggongDao{

	@Override
	public boolean isTinggong(Integer orderId) {
		String hql = "from " + this.tableName + " as tg where tg.tinggongStatus =?  and tg.order.id = ?";
		ProduceTinggong pt = this.find(hql, ProduceTinggong.TINGGONG_COMMIT, orderId);
		if (pt == null)
			return false;
		return true;
	}

}
