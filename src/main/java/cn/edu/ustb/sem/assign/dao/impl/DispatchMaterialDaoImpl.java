package cn.edu.ustb.sem.assign.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.assign.dao.DispatchMaterialDao;
import cn.edu.ustb.sem.assign.entity.DispatchMaterial;
import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;

@Repository("dispatchMaterialDao")
public class DispatchMaterialDaoImpl extends BaseDaoImpl<DispatchMaterial, Integer> implements DispatchMaterialDao {

}
