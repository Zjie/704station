package cn.edu.ustb.sem.core.service;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.model.TreeStore;

public interface IndexService {
	/**
	 * 获取该角色的菜单项
	 * @param roleId 角色id
	 * @return
	 * @throws ServiceException
	 */
	public TreeStore getTreeStore(int roleId) throws ServiceException;
}
