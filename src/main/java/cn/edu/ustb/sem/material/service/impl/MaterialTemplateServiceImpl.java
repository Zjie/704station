package cn.edu.ustb.sem.material.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.core.util.ExcelUtil;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.material.dao.MaterialDao;
import cn.edu.ustb.sem.material.dao.MaterialTemplateDao;
import cn.edu.ustb.sem.material.dao.MtmDao;
import cn.edu.ustb.sem.material.dao.ProductCodeDao;
import cn.edu.ustb.sem.material.entity.Material;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.entity.MtProductCode;
import cn.edu.ustb.sem.material.entity.Mtm;
import cn.edu.ustb.sem.material.service.MaterialService;
import cn.edu.ustb.sem.material.service.MaterialTemplateService;
import cn.edu.ustb.sem.material.web.model.MaterialModel;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateModel;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateSearchModel;

@Service("materialTemplateService")
public class MaterialTemplateServiceImpl extends
		BaseServiceImpl<MaterialTemplate, MaterialTemplateModel, Integer> implements
		MaterialTemplateService {
	private MaterialTemplateDao materialTemplateDao;
	@Autowired
	private MaterialDao materialDao;
	@Autowired
	private ProductCodeDao productCodeDao;
	@Autowired
	private MtmDao mtmDao;
	@Autowired
	private MaterialService materialService;
	@Autowired
	@Qualifier("materialTemplateDao")
	@Override
	public void setBaseDao(BaseDao<MaterialTemplate, Integer> baseDao) {
		this.baseDao = baseDao;
		this.materialTemplateDao = (MaterialTemplateDao) baseDao;
	}
	@Override
	public boolean saveSheet(Sheet sheet)
			throws ServiceException {
		MaterialTemplate mt = new MaterialTemplate();
		//解析得到模板（包含数据有效性检验），如果解析并验证成功，则保存到数据库中
		if (parseMt(mt, sheet)) {
			//保存模板
			saveMt(mt);
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 解析得到物料模板基本信息
	 * 如果出现数据不一致，则抛出异常，并修改了上传的excel文件
	 * @param mt
	 * @param sheet
	 * @throws ServiceException 
	 */
	private boolean parseMt(MaterialTemplate mt, Sheet sheet) throws ServiceException {
		boolean flag = true;
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		int rowNum = 1;
		Row row = sheet.getRow(rowNum);
		//族名称
		Cell cell = row.getCell(9);
		mt.setGroupName(cell.getStringCellValue());
		//模板名称，检验名称是否重复
		cell = row.getCell(10);
		String name = cell.getStringCellValue();
		if (name == null || name.isEmpty()) {
			ExcelUtil.createCommentForCell(cell, "模板名称不能为空");
			flag &= false;
		} else {
			mt.setName(name);
			MaterialTemplate exists = materialTemplateDao.exists(name);
			if (exists != null) {
				ExcelUtil.createCommentForCell(cell, "模板名称：" + mt.getName() + " 已经存在");
				flag &= false;
			}
		}
		//产品代号，检验产品代号是否重复
		flag &= parseProductCode(mt, sheet);
		//所有物料
		flag &= parseMaterials(mt, sheet);
		mt.setUdate(Calendar.getInstance());
		User u = new User();
		u.setId(visitor.getUid());
		mt.setUpdater(u);
		return flag;
	}
	/**
	 * 解析得到所有产品代号并验证产品代号没有重复
	 * @param mt
	 * @param sheet
	 * @throws ServiceException 
	 */
	private boolean parseProductCode(MaterialTemplate mt, Sheet sheet) {
		boolean flag = true;
		//产品代号
		int rowNum = 1;
		Row row = sheet.getRow(rowNum);
		Cell cell;
		Set<MtProductCode> allPcs = new HashSet<MtProductCode>();
		StringBuilder sb = new StringBuilder();
		while (true) {
			row = sheet.getRow(rowNum);
			if (row == null) break;
			cell = row.getCell(11);
			if (cell == null) {
				break;
			}
			String productCode = cell.getStringCellValue();
			productCode = productCode.toLowerCase().trim();
			if (productCode == null || productCode.isEmpty()) {
				break;
			}
			sb.append(productCode).append(", ");
			Set<MtProductCode> pcs = parseProductCode(productCode, mt);
			//验证产品代号不能重复
			for (MtProductCode pc : pcs) {
				MtProductCode pcBo = productCodeDao.find(new MtProductCode(pc.getProductCode()));
				if (pcBo != null) {
					ExcelUtil.createCommentForCell(cell, "产品代号和模板" + pcBo.getMt().getName() + "的产品代号重复");
					flag &= false;
				}
			}
			allPcs.addAll(pcs);
			rowNum++;
		}
		mt.setProductCodes(allPcs);
		mt.setProductCodeString(sb.toString());
		return flag;
	}
	/**
	 * 解析得到所有物料品类
	 * @param mt
	 * @param sheet
	 */
	private boolean parseMaterials(MaterialTemplate mt, Sheet sheet) {
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		int rowNum = 1;
		Row row = sheet.getRow(rowNum);
		Cell cell;
		//物料类别
		boolean flag = true;
		String type = "";
		Set<Mtm> allMaterials = new HashSet<Mtm>();
		String lastType = null;
		while (true) {
			//这个用来记录一行记录是否全为空，如果全为空，说明这是行的结束
			int rowNullCount = 0;
			int errorCount = 0;
			row = sheet.getRow(rowNum);
			if (row == null) {
				break;
			}
			Material material = new Material();
			//物料类别
			cell = row.getCell(0);
			if (cell == null) {
				type = lastType;
			} else {
				type = cell.getStringCellValue();
				if (type == null || type.isEmpty()) {
					type = lastType;
				} else {
					lastType = type;
				}
			}
			material.setType(type);
			//序号 不能为空
			cell = row.getCell(1);
			int no = 0;
			if (cell != null) {
				if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
					ExcelUtil.createCommentForCell(cell, "序号只能是数字");
					flag &= false;
					errorCount++;
				} else {
					no = (int) cell.getNumericCellValue();
				}
			} else {
				rowNullCount++;
			}
			material.setNo(no);
			//名称 不能为空
			cell = row.getCell(2);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					material.setName(cell.getStringCellValue());
				} else {
					ExcelUtil.createCommentForCell(cell, "名称不能为空");
					flag &= false;
					errorCount++;
				}
			} else {
				rowNullCount++;
			}
			//型号规格 可以为空
			cell = row.getCell(3);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					material.setSpecification(cell.getStringCellValue());
				} else {
					material.setSpecification("");
				}
			} else {
				material.setSpecification("");
			}
			//质量等级或标准
			cell = row.getCell(4);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					material.setLevel(cell.getStringCellValue());
				} else {
					material.setLevel("");
				}
			} else {
				material.setLevel("");
			}
			//计量单位 可以为空
			cell = row.getCell(5);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					material.setUom(cell.getStringCellValue());
				} else {
					material.setUom("");
				}
			} else {
				material.setUom("");
			}
			//单机个数
			cell = row.getCell(6);
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					material.setSingleNum(cell.getNumericCellValue());
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					String danji = cell.getStringCellValue();
					try {
						Double num = Double.parseDouble(danji);
						material.setSingleNum(num);
					} catch (NumberFormatException e) {
						//如果无法解析则设为0
						material.setSingleNum(0.0);
					}
				} else {
					//如果为空
					material.setSingleNum(0.0);
				}
			} else {
				material.setSingleNum(0.0);
				rowNullCount++;
			}
			//工艺备份数量
			cell = row.getCell(7);
			if (cell != null) {
				if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC) {
					material.setBackupNum(cell.getNumericCellValue());
				} else {
					material.setBackupNum(0.0);
				}
			} else  {
				material.setBackupNum(0.0);
			}
			if (rowNullCount >= 4) {
				break;
			}
			rowNum++;
			if (errorCount >= 1) {
				//只要有一个错误，则全错
				flag &= false;
				continue;
			}
			User u = new User();
			u.setId(visitor.getUid());
			material.setUpdater(u);
			material.setUdate(Calendar.getInstance());
			
			Mtm mtm = new Mtm();
			mtm.setMaterial(material);
			mtm.setMt(mt);
			allMaterials.add(mtm);
			
			
		}
		mt.setMtms(allMaterials);
		return flag;
	}
	/**
	 * 解析产品代号系列得到所有产品代号
	 * 例如：cx-1-2/5={cx-1-2, cx-1-3, cx-1-4, cx-1-5}
	 * @param productCode
	 * @return
	 */
	private Set<MtProductCode> parseProductCode(String productCode, MaterialTemplate mt) {
		productCode = productCode.toLowerCase();
		Set<MtProductCode> pcs = new HashSet<MtProductCode>();
		Pattern pattern = Pattern.compile("\\d+/\\d+");
		Matcher matcher = pattern.matcher(productCode);
		if (matcher.find()) {
			String rex = matcher.group();
			String prefix = productCode.substring(0, productCode.length() - rex.length());
			String[] be = rex.split("/");
			int begin = Integer.parseInt(be[0]);
			int end = Integer.parseInt(be[1]);
			if (begin > end) {
				//do some thing
			} else {
				while (begin <= end) {
					String newProductCode = prefix + begin;
					MtProductCode pc = new MtProductCode();
					pc.setProductCode(newProductCode);
					pc.setMt(mt);
					pcs.add(pc);
					begin++;
				}
			}
		} else {
			MtProductCode pc = new MtProductCode();
			pc.setProductCode(productCode);
			pc.setMt(mt);
			pcs.add(pc);
		}
		return pcs;
	}
	//导入不做更新，只是导入，因为如果导入的同时更新的话，数据都会乱掉的。例如模板更新时，怎么保证模板名称没有错误呢？
	private void saveMt(MaterialTemplate mt) throws ServiceException {
		//保存基本信息
		materialTemplateDao.save(mt);
		
		Set<Mtm> mtms = mt.getMtms();
		if (mtms != null) {
			for (Mtm mtm : mtms) {
				Material material = mtm.getMaterial();
				//保存物料信息
				materialDao.save(material);
				//保存物料和模板的关系
				mtmDao.save(mtm);
			}
		}
		//已经级联保存产品代号
	}

	@Override
	public GridModel<MaterialTemplateModel> list(
			MaterialTemplateSearchModel form,
			ItemModelHelper<MaterialTemplate, MaterialTemplateModel> helper)
			throws ServiceException {
		if (helper == null) {
			logger.warn("lack of itemHelper");
			throw new ServiceException("系统出错，请联系管理员");
		}
		if (form == null) {
			return this.list(null, -1, -1, helper);
		}
		MaterialTemplate model = getSearchModel(form);
		return this.list(model, form.getPage(), form.getLimit(), helper);
	}
	/**
	 * 将前端传过来的条件进行转化
	 * @param condition
	 * @return
	 */
	private MaterialTemplate getSearchModel(MaterialTemplateSearchModel condition) {
		MaterialTemplate model = new MaterialTemplate();
		if (condition.getId() > 0) {
			model.setId(condition.getId());
			return model;
		}
		if (condition.getName() != null && !condition.getName().isEmpty()) {
			model.setName(condition.getName());
		}
		if (condition.getGroupName() != null && !condition.getGroupName().isEmpty()) {
			model.setGroupName(condition.getGroupName());
		}
		if (condition.getProductCode() != null && !condition.getProductCode().isEmpty()) {
			MtProductCode pc = new MtProductCode();
			pc.setProductCode(condition.getProductCode());
			HashSet<MtProductCode> pcs = new HashSet<MtProductCode>();
			pcs.add(pc);
			model.setProductCodes(pcs);
		}
		return model;
	}

	@Override
	public void saveOrUpdate(MaterialTemplateModel mtl)
			throws ServiceException {
		if (mtl == null) {
			throw new ServiceException("缺失参数");
		}
		MaterialTemplate mt = new MaterialTemplate();
		mt.setGroupName(mtl.getGroupName());
		mt.setName(mtl.getName());
		mt.setProductCodeString(mtl.getProductCode().toLowerCase().trim());
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = new User(visitor.getUid());
		Calendar udate = Calendar.getInstance();
		mt.setUpdater(u);
		mt.setUdate(udate);
		if (mtl.getId() <= 0) {
			int mtId = materialTemplateDao.save(mt);
			mtl.setId(mtId);
			saveMaterials(mtl);
		} else {
			mt.setId(mtl.getId());
			materialTemplateDao.update(mt);
			updateMaterials(mtl);
		}
		productCodeDao.updateProductCode4Mt(mt);
	}
	//取消了从原有物料库中选择物料，所以保存模板的时候，直接全部保存即可
	/**
	 * 保存物料
	 * @param mtl
	 * @throws ServiceException
	 */
	private void saveMaterials(MaterialTemplateModel mtl) throws ServiceException {
		List<MaterialModel> materials = mtl.getMaterials();
		int i = 1;
		if (materials != null && !materials.isEmpty()) {
			for (MaterialModel mm : materials) {
				mm.setMtId(mtl.getId());
				mm.setNo(i);
				i++;
				materialService.save(mm);
			}
		}
	}
	/**
	 * 更新物料品类
	 * @param mtl
	 * @throws ServiceException
	 */
	private void updateMaterials(MaterialTemplateModel mtl) throws ServiceException {
		//对比已有的物料
		Mtm model = new Mtm();
		model.setMt(new MaterialTemplate(mtl.getId()));
		List<Mtm> existsMtms = mtmDao.listAll(model, -1, -1);
		Map<Integer, Material> materialMap = new HashMap<Integer, Material>();
		if (existsMtms != null) {
			for (Mtm mtm : existsMtms) {
				Material m = mtm.getMaterial();
				materialMap.put(m.getId(), m);
			}
		}
		//更新物料
		List<MaterialModel> materials = mtl.getMaterials();
		int i = 1;
		if (materials != null && !materials.isEmpty()) {
			for (MaterialModel mm : materials) {
				if (mm.getId() > 0) {
					//移除掉没有变化的物料
					materialMap.remove(mm.getId());
					continue;
				}
				//添加新的物料
				mm.setNo(i);
				mm.setMtId(mtl.getId());
				i++;
				materialService.save(mm);
			}
		}
		//删除掉在前端被删除的物料
		Set<Entry<Integer, Material>> entries = materialMap.entrySet();
		for (Entry<Integer, Material> entry: entries) {
			Material m = entry.getValue();
			//删除模板和物料的关系
			Mtm mtm = m.getMtm();
			if (mtm != null) {
				mtmDao.delete(mtm.getId());
			}
			materialDao.delete(entry.getKey());
		}
	}

	@Override
	public MaterialTemplate findByProductCode(String pc)
			throws ServiceException {
		MtProductCode model = new MtProductCode();
		model.setProductCode(pc);
		MtProductCode mpc = productCodeDao.find(model);
		if (mpc == null) {
			//通用模板
			model.setProductCode("*");
			mpc = productCodeDao.find(model);
			if (mpc == null) {
				return null;
			}
		}
		return mpc.getMt();
	}
	@Override
	public void delete(Integer id) {
		MaterialTemplate mt = this.baseDao.get(id);
		if (mt == null) return;
		//删除模板
		this.materialTemplateDao.deleteObject(new MaterialTemplate(id));
		//删除模板和产品代号
		Set<MtProductCode> mtps = mt.getProductCodes();
		for (MtProductCode mp : mtps) {
			if (mp == null) continue;
			productCodeDao.deleteObject(new MtProductCode(mp.getId()));
		}
		Set<Mtm> mtms = mt.getMtms();
		for (Mtm mtm : mtms) {
			if (mtm == null) continue;
			//删除物料和模板的关系
			this.mtmDao.deleteObject(new Mtm(mtm.getId()));
			Material m = mtm.getMaterial();
			if (m != null) {
				//删除物料
				this.materialDao.deleteObject(new Material(m.getId()));
			}
		}
	}
}
