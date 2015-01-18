package cn.edu.ustb.sem.material.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.material.dao.MaterialDao;
import cn.edu.ustb.sem.material.dao.MtmDao;
import cn.edu.ustb.sem.material.entity.Material;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.entity.Mtm;
import cn.edu.ustb.sem.material.service.MaterialService;
import cn.edu.ustb.sem.material.web.model.MaterialModel;
@Service("materialService")
public class MaterialServiceImpl extends BaseServiceImpl<Material, MaterialModel, Integer> implements MaterialService {
	@Autowired
	private MtmDao mtmDao;
	private MaterialDao materialDao;
	@Autowired
    @Qualifier("materialDao")
	@Override
	public void setBaseDao(BaseDao<Material, Integer> baseDao) {
		this.baseDao = baseDao;
		this.materialDao = (MaterialDao) baseDao;
	}
	/**
	 * 物料只有保存和删除，没有更新操作
	 */
	@Override
	public void save(MaterialModel mm) throws ServiceException {
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = new User(visitor.getUid());
		Calendar udate = Calendar.getInstance();
		Material m = new Material();
		m.setBackupNum(mm.getBkNum());
		m.setLevel(mm.getLevel());
		m.setName(mm.getName());
		m.setNo(mm.getNo());
		m.setSingleNum(mm.getSingleNum());
		m.setSpecification(mm.getSpecification());
		m.setType(mm.getType());
		m.setUom(mm.getUom());
		m.setUdate(udate);
		m.setUpdater(u);
		//保存物料信息
		materialDao.save(m);
		//保存物料和模板的关系
		Mtm mtm = new Mtm();
		mtm.setMaterial(m);
		MaterialTemplate mt = new MaterialTemplate();
		mt.setId(mm.getMtId());
		mtm.setMt(mt);
		mtmDao.save(mtm);
	}

}
