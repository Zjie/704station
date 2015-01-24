package cn.edu.ustb.sem.process.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import cn.edu.ustb.sem.core.util.ServiceContext;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.process.dao.ProcessTemplateDao;
import cn.edu.ustb.sem.process.dao.PtProductCodeDao;
import cn.edu.ustb.sem.process.dao.PtpDao;
import cn.edu.ustb.sem.process.entity.PProcess;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
import cn.edu.ustb.sem.process.entity.PtProductCode;
import cn.edu.ustb.sem.process.entity.Ptp;
import cn.edu.ustb.sem.process.service.ProcessService;
import cn.edu.ustb.sem.process.service.ProcessTemplateService;
import cn.edu.ustb.sem.process.web.ProcessTemplateController;
import cn.edu.ustb.sem.process.web.model.ProcessModel;
import cn.edu.ustb.sem.process.web.model.ProcessTemplateModel;
import cn.edu.ustb.sem.process.web.model.ProcessTemplateSearchForm;
@Service("processTemplateService")
public class ProcessTemplateServiceImpl extends BaseServiceImpl<ProcessTemplate, ProcessTemplateModel, Integer> implements ProcessTemplateService {
	private static final int BEGIN_ROW = 1;
	private static final int GROUP_NAME_CELL = 17;
	private static final int NAME_CELL = 18;
	private static final int PRO_CODE_CELL = 19;
	private ProcessTemplateDao processTemplateDao;
	@Autowired
	private PtProductCodeDao ptProductCodeDao;
	@Autowired
	private ProcessService processService;
	@Autowired
	private PtpDao ptpDao;
	@Autowired
    @Qualifier("processTemplateDao")
	@Override
	public void setBaseDao(BaseDao<ProcessTemplate, Integer> baseDao) {
		this.processTemplateDao = (ProcessTemplateDao) baseDao;
		this.baseDao = baseDao;
	}
	@Override
	public void importExcelFile(Workbook hssfWorkbook) throws ServiceException {
		HttpSession session = ServiceContext.getRequest().getSession();
		int totalNum = hssfWorkbook.getNumberOfSheets();
		session.setAttribute(ProcessTemplateController.IMPORT_NUM, totalNum);
		List<Integer> successPt = new ArrayList<Integer>();
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < totalNum; numSheet++) {
			// 对于每个sheet
			Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			ProcessTemplate pt = new ProcessTemplate();
			//解析得到模板（包含数据有效性检验），如果解析并验证成功，则保存到数据库中
			if (parsePt(pt, hssfSheet)) {
				//保存模板
				savePt(pt);
				successPt.add(numSheet);
			}
			session.setAttribute(ProcessTemplateController.CUR_NUM, numSheet + 1);
		}
		//删除已经保存成功的模板
		Collections.reverse(successPt);
		for (Integer s : successPt) {
			hssfWorkbook.removeSheetAt(s);
		}
	}
	/**
	 * 解析模板
	 * @param pt
	 * @param sheet
	 * @return
	 * @throws ServiceException 
	 */
	private boolean parsePt(ProcessTemplate pt, Sheet sheet) throws ServiceException {
		boolean flag = true;
		Row row = sheet.getRow(BEGIN_ROW);
		//族名称
		Cell cell = row.getCell(GROUP_NAME_CELL);
		pt.setGroupName(cell.getStringCellValue());
		//模板名称，检验名称是否重复
		cell = row.getCell(NAME_CELL);
		String name = cell.getStringCellValue();
		if (name == null || name.isEmpty()) {
			ExcelUtil.createCommentForCell(cell, "模板名称不能为空");
			flag &= false;
		} else {
			pt.setName(name);
			ProcessTemplate form = new ProcessTemplate();
			form.setName(name);
			ProcessTemplate exists = this.find(form);
			if (exists != null) {
				ExcelUtil.createCommentForCell(cell, "模板名称：" + name + " 已经存在");
				flag &= false;
			}
		}
		//产品代号，检验产品代号是否重复
		flag &= parseProductCode(pt, sheet);
		flag &= parseProcesses(pt, sheet);
		pt.setUdate(Calendar.getInstance());
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		pt.setUpdater(new User(visitor.getUid()));
		return flag;
	}
	/**
	 * 解析得到工序
	 * @param pt
	 * @param sheet
	 * @throws ServiceException 
	 */
	private boolean parseProcesses(ProcessTemplate pt, Sheet sheet) throws ServiceException {
		boolean flag = true;
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = new User(visitor.getUid());
		Calendar udate = Calendar.getInstance();
		int rowNum = BEGIN_ROW;
		
		Cell cell;
		//基数
		cell = sheet.getRow(BEGIN_ROW).getCell(15);
		int base = 0;
		if (cell == null || (base = (int) cell.getNumericCellValue()) == 0) {
			ExcelUtil.createCommentForCell(cell, "基数不能为空");
			flag &=false;
		}
		Set<Ptp> ptps = new HashSet<Ptp>();
		String lastPhase = null;
		while (true) {
			Row row = sheet.getRow(rowNum);
			rowNum++;
			if (row == null) {
				break;
			}
			boolean hitEnd = false;
			//流程
			cell = row.getCell(9);
			if (cell != null) {
				String part = cell.getStringCellValue();
				if (part.toLowerCase().trim().equals("3s/3e")) {
					hitEnd = true;
				}
			}
			cell = row.getCell(10);
			//生产阶段
			String phase = "";
			if (cell == null) {
				phase = lastPhase;
			} else {
				phase = cell.getStringCellValue();
				if (phase == null || phase.isEmpty()) {
					phase = lastPhase;
				} else {
					lastPhase = phase;
				}
			}
			
			//工作内容
			cell = row.getCell(11);
			if (cell == null) {
				continue;
			}
			String content = cell.getStringCellValue();
			if (content == null || content.isEmpty()) {
				//工作内容不能为空
				continue;
			}
			
			//工序组，可能为空
			cell = row.getCell(12);
			int group = 0;
			if (cell != null) {
				group = (int) cell.getNumericCellValue();
			}
			//周期预估可以为空
			cell = row.getCell(13);
			int measure = 0;
			if (cell != null) {
				measure = (int) cell.getNumericCellValue();
			}
			//备注，可以为空
			cell = row.getCell(14);
			String remark = "";
			if (cell != null) {
				remark = cell.getStringCellValue();
			}
			//以生产阶段+工作内容作为定位，确定是否为重复工序
			PProcess p = new PProcess();
			p.setPhase(phase);
			p.setContent(content);
			PProcess exists = processService.find(p);
			if (exists != null) {
				p = exists;
			} else {
				p.setGroup(group);
				p.setMeasure(measure);
				p.setRemark(remark);
			}
			p.setBase(base);
			p.setUdate(udate);
			p.setUpdater(u);
			Ptp ptp = new Ptp();
			ptp.setProcess(p);
			ptp.setPt(pt);
			p.setPtp(ptp);
			ptps.add(ptp);
			if (hitEnd)
				break;
		}
		pt.setPtps(ptps);
		return flag;
	}
	private boolean parseProductCode(ProcessTemplate pt, Sheet sheet) {
		boolean flag = true;
		//产品代号
		int rowNum = BEGIN_ROW;
		Row row = sheet.getRow(rowNum);
		Cell cell;
		Set<PtProductCode> allPcs = new HashSet<PtProductCode>();
		StringBuilder sb = new StringBuilder();
		while (true) {
			row = sheet.getRow(rowNum);
			cell = row.getCell(PRO_CODE_CELL);
			if (cell == null) {
				break;
			}
			String productCode = cell.getStringCellValue();
			if (productCode == null || productCode.isEmpty()) {
				break;
			}
			sb.append(productCode).append(", ");
			Set<PtProductCode> pcs = parseProductCode(productCode, pt);
			//验证产品代号不能重复
			for (PtProductCode pc : pcs) {
				PtProductCode exists = ptProductCodeDao.find(new PtProductCode(pc.getProductCode()));
				if (exists != null) {
					ExcelUtil.createCommentForCell(cell, "产品代号和模板" + exists.getPt().getName() + "的产品代号重复");
					flag &= false;
				}
			}
			allPcs.addAll(pcs);
			rowNum++;
		}
		pt.setProductCodeString(sb.toString());
		pt.setProductCodes(allPcs);
		return flag;
	}
	/**
	 * 解析产品代号系列得到所有产品代号
	 * 例如：cx-1-2/5={cx-1-2, cx-1-3, cx-1-4, cx-1-5}
	 * @param productCode
	 * @return
	 */
	private Set<PtProductCode> parseProductCode(String productCode, ProcessTemplate pt) {
		productCode = productCode.toLowerCase();
		Set<PtProductCode> pcs = new HashSet<PtProductCode>();
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
					PtProductCode pc = new PtProductCode();
					pc.setProductCode(newProductCode);
					pc.setPt(pt);
					pcs.add(pc);
					begin++;
				}
			}
		} else {
			PtProductCode pc = new PtProductCode();
			pc.setProductCode(productCode);
			pc.setPt(pt);
			pcs.add(pc);
		}
		return pcs;
	}
	private void savePt(ProcessTemplate pt) throws ServiceException {
		processTemplateDao.save(pt);
		Set<Ptp> ptps = pt.getPtps();
		if (ptps != null) {
			for (Ptp ptp : ptps) {
				PProcess p = ptp.getProcess();
				processService.save(p);
				ptpDao.save(ptp);
			}
		}
	}
	@Override
	public GridModel<ProcessTemplateModel> list(ProcessTemplateSearchForm form,
			ItemModelHelper<ProcessTemplate, ProcessTemplateModel> helper)
			throws ServiceException {
		ProcessTemplate model = new ProcessTemplate();
		if (form.getPtid() != null) {
			model.setId(form.getPtid());
		}
		return this.list(model, form.getPage(), form.getLimit(), helper);
	}
	@Override
	public void saveOrUpdate(ProcessTemplateModel model)
			throws ServiceException {
		if (model == null) {
			throw new ServiceException("缺失参数");
		}
		ProcessTemplate pt = new ProcessTemplate();
		pt.setGroupName(model.getGroupName());
		pt.setName(model.getName());
		pt.setProductCodeString(model.getProductCodeString());
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = new User(visitor.getUid());
		Calendar udate = Calendar.getInstance();
		pt.setUpdater(u);
		pt.setUdate(udate);
		if (model.getId() <= 0) {
			int ptid = this.baseDao.save(pt);
			model.setId(ptid);
			saveProcesses(model);
		} else {
			pt.setId(model.getId());
			this.baseDao.update(pt);
			updateProcesses(model);
		}
		this.ptProductCodeDao.updateProductCodeForPt(pt);
	}
	/**
	 * 保存模板的工序
	 * @param pt
	 * @throws ServiceException 
	 */
	private void saveProcesses(ProcessTemplateModel pt) throws ServiceException {
		List<ProcessModel> processes = pt.getProcesses();
		List<Integer> neededToBeAdded =  new ArrayList<Integer>();
		if (processes != null && !processes.isEmpty()) {
			for (ProcessModel pm : processes) {
				neededToBeAdded.add(pm.getId());
			}
		}
		//只需更新ptp表
		for (Integer pid : neededToBeAdded) {
			Ptp ptp = new Ptp();
			ptp.setProcess(new PProcess(pid));
			ptp.setPt(new ProcessTemplate(pt.getId()));
			this.ptpDao.save(ptp);
		}
	}
	/**
	 * 更新模板的工序
	 * @param pt
	 */
	private void updateProcesses(ProcessTemplateModel pt) {
		//对比已有的工序
		Ptp model = new Ptp();
		model.setPt(new ProcessTemplate(pt.getId()));
		List<Ptp> exists = this.ptpDao.listAll(model, -1, -1);
		Map<Integer, PProcess> processMap = new HashMap<Integer, PProcess>();
		if (exists != null) {
			for (Ptp ptp : exists) {
				PProcess p = ptp.getProcess();
				processMap.put(p.getId(), p);
			}
		}
		//更新工序
		List<ProcessModel> processes = pt.getProcesses();
		if (processes != null) {
			for (ProcessModel p : processes) {
				int pid = p.getId();
				if (processMap.containsKey(pid)) {
					//移除没有变化的工序
					processMap.remove(pid);
					continue;
				} else {
					//添加新的工序
					Ptp ptp = new Ptp();
					ptp.setProcess(new PProcess(pid));
					ptp.setPt(new ProcessTemplate(pt.getId()));
					this.ptpDao.save(ptp);
				}
			}
		}
		//需要删除的工序
		Set<Integer> neededToBeDeleted = processMap.keySet();
		for (Integer pid : neededToBeDeleted) {
			Ptp ptp = new Ptp();
			ptp.setProcess(new PProcess(pid));
			ptp.setPt(new ProcessTemplate(pt.getId()));
			this.ptpDao.deleteObject(ptp);
		}
	}
	@Override
	public void delete(Integer ptId) {
		ProcessTemplate pt = this.processTemplateDao.get(ptId);
		if (pt == null) return;
		//删除模板和工序的关联关系
		Ptp ptp = new Ptp();
		ptp.setPt(new ProcessTemplate(ptId));
		this.ptpDao.deleteObject(ptp);
		//删除工序模板和产品代号的关联
		Set<PtProductCode> ppcs = pt.getProductCodes();
		for (PtProductCode pc : ppcs) {
			if (pc == null) continue;
			this.ptProductCodeDao.deleteObject(new PtProductCode(pc.getId()));
		}
		//删除模板
		this.processTemplateDao.deleteObject(new ProcessTemplate(ptId));
	}
}
