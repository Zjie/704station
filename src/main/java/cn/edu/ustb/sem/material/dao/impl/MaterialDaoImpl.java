package cn.edu.ustb.sem.material.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.material.dao.MaterialDao;
import cn.edu.ustb.sem.material.entity.Material;
@Repository("materialDao")
public class MaterialDaoImpl extends BaseDaoImpl<Material, Integer> implements MaterialDao {

}
