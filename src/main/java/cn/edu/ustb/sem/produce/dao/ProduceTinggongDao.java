package cn.edu.ustb.sem.produce.dao;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;

public interface ProduceTinggongDao extends BaseDao<ProduceTinggong, Integer>{
	/**
	 * 判断一个订单是否处于停工状态
	 * @param orderId
	 * @return
	 */
	public boolean isTinggong(Integer orderId);
}
