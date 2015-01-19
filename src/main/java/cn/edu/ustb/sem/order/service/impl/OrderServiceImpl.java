package cn.edu.ustb.sem.order.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import cn.edu.ustb.sem.assign.entity.Assign;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.cons.AssignType;
import cn.edu.ustb.sem.core.cons.OrderStatus;
import cn.edu.ustb.sem.core.cons.YesOrNo;
import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.CellParseException;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.service.impl.BaseServiceImpl;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.core.util.ExcelUtil;
import cn.edu.ustb.sem.core.util.ServiceContext;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.material.entity.Material;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.entity.Mtm;
import cn.edu.ustb.sem.material.service.MaterialModuleService;
import cn.edu.ustb.sem.order.dao.OrderDao;
import cn.edu.ustb.sem.order.dao.OrderMaterialDao;
import cn.edu.ustb.sem.order.dao.OrderProcessDao;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.order.entity.OrderMaterial;
import cn.edu.ustb.sem.order.entity.OrderProcess;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.order.web.OrderListController;
import cn.edu.ustb.sem.order.web.model.OrderModel;
import cn.edu.ustb.sem.order.web.model.OrderSearchForm;
import cn.edu.ustb.sem.process.entity.PProcess;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
import cn.edu.ustb.sem.process.entity.PtProductCode;
import cn.edu.ustb.sem.process.entity.Ptp;
import cn.edu.ustb.sem.process.service.ProcessModuleService;
import cn.edu.ustb.sem.produce.entity.ProduceAssembling;

@Service("orderService")
public class OrderServiceImpl extends
		BaseServiceImpl<Order, OrderModel, Integer> implements OrderService {
	@Autowired
	private MaterialModuleService mats;
	@Autowired
	private ProcessModuleService pms;
	@Autowired
	private OrderMaterialDao orderMaterialDao;
	@Autowired
	private OrderProcessDao orderProcessDao;
	private OrderDao orderDao;

	@Autowired
	@Qualifier("orderDao")
	@Override
	public void setBaseDao(BaseDao<Order, Integer> baseDao) {
		this.baseDao = baseDao;
		this.orderDao = (OrderDao) baseDao;
	}

	@Override
	public int importExcel(Workbook hssfWorkbook) throws ServiceException {
		HttpSession session = ServiceContext.getRequest().getSession();
		List<Row> successOrder = new ArrayList<Row>();
		// 取第二张表
		Sheet hssfSheet = hssfWorkbook.getSheetAt(1);
		int totalNum=hssfSheet.getLastRowNum();
		int rowNum = 4;
		session.setAttribute(OrderListController.IMPORT_NUM, totalNum - rowNum);
		int errorNum = 0;
		// 从第四行开始
		// 用来保存excel表中的订单序号，禁止有重复的序号
		Map<String, Order> excelExistOrder = new HashMap<String, Order>();
		while (true) {
			Row row = hssfSheet.getRow(rowNum);
			rowNum++;
			if (row == null) {
				break;
			}
			Cell cell = row.getCell(0);
			if (cell == null || cell.getNumericCellValue() == 0) {
				break;
			}
			Order order = new Order();
			Visitor v = (Visitor) SecurityContextHolder.getContext()
					.getAuthentication();
			order.setTyper(new User(v));
			order.setUdate(Calendar.getInstance());
			// 如果解析成功
			if (parse(row, order, excelExistOrder)) {
				boolean rowFlag = true;
				
				excelExistOrder.put(order.getNo(), order);
				boolean saveOrUpdate;
				//改为只能新增
				Order model = new Order();
				model.setNo(order.getNo());
				Order exist = this.orderDao.find(model);
				if (exist != null) {
					// 更新，只更新基础字段
					exist.setProject(order.getProject());
					exist.setProcess(order.getProcess());
					exist.setModel(order.getModel());
					exist.setProductName(order.getProductName());
					exist.setProductCode(order.getProductCode());
					exist.setPlanToAdjust(order.getPlanToAdjust());
					exist.setKe2produceNum(order.getKe2produceNum());
					exist.setKe2DianshiNum(order.getKe2DianshiNum());
					exist.setDeliveryNum(order.getDeliveryNum());
					exist.setProductBatchId(order.getProductBatchId());
					exist.setMaterialsQitaoTime(order.getMaterialsQitaoTime());
					exist.setComponentCompleteness(order.getComponentCompleteness());
					exist.setCertificateDate(order.getCertificateDate());
					exist.setStockInDate(order.getStockInDate());
					exist.setRemark(order.getRemark());
					orderDao.update(exist);
					order = exist;
					saveOrUpdate = false;
				} else {
				
					// 新增
					order.setStatus(OrderStatus.INITIAL.getIndex());
					order.setIsAssigned(AssignType.UNFINISHED_ASSIGN);
					order.setIsReported(YesOrNo.NO.getIndex());
					order.setIsDispatchMaterial(YesOrNo.NO.getIndex());
					order.setProcessIsCheck(Order.PROCESS_IS_NOT_CHECKED);
					order.setAssignStatus(Order.ASSIGN_STATUS_NO);
					orderDao.save(order);
					saveOrUpdate = true;
				}
				// 绑定物料
				if (!bindMaterials(order, saveOrUpdate)) {
					// 如果绑定不成功
					// 不做绑定的强制限制
					// rowFlag &= false;
				}
				// 绑定工序模板
				if (!bindProcesses(order)) {
					// 不做绑定的强制限制
					// rowFlag &= false;
				}
				// 如果完全没有出现错误，则
				if (rowFlag) {
					successOrder.add(row);
				} else {
					errorNum++;
				}
			} else {
				errorNum++;
			}
			session.setAttribute(OrderListController.CUR_NUM, rowNum - 4);
		}
		// 从excel文件中删除已经保存成功的订单的记录
		Collections.reverse(successOrder);
		for (Row row : successOrder) {
			hssfSheet.removeRow(row);
		}
		return errorNum;
	}

	/**
	 * 绑定工序
	 * 
	 * @param order
	 * @return
	 * @throws ServiceException
	 */
	private boolean bindProcesses(Order order) throws ServiceException {
		boolean flag = true;
		// 先删除已有的
		OrderProcess model = new OrderProcess();
		model.setOrder(new Order(order.getId()));
		this.orderProcessDao.deleteObject(model);
		// 再绑定新的工序
		String pc = order.getProductCode();
		PtProductCode form = new PtProductCode(pc);
		PtProductCode exists = this.pms.getPtProductCodeService().find(form);
		if (exists == null) {
			form.setProductCode("*");
			exists = this.pms.getPtProductCodeService().find(form);
			if (exists == null)
				return false;
		}
		// 获得对应的工序模板
		ProcessTemplate pt = exists.getPt();
		Set<Ptp> ptps = pt.getPtps();
		if (ptps != null) {
			for (Ptp ptp : ptps) {
				PProcess p = ptp.getProcess();
				OrderProcess op = new OrderProcess(p);
				op.setOrder(order);
				op.setUdate(order.getUdate());
				op.setUpdater(order.getTyper());
				this.orderProcessDao.save(op);
			}
		}
		return flag;
	}

	/**
	 * 绑定订单和物料
	 * 
	 * @param order
	 * @param saveOrUpdate
	 *            true表示save, false表示update
	 * @throws ServiceException
	 */
	private boolean bindMaterials(Order order, boolean saveOrUpdate)
			throws ServiceException {
		boolean flag = true;
		if (order == null || order.getProductCode() == null
				|| order.getProductCode().isEmpty()) {
			return false;
		}
		// 先删除
		OrderMaterial model = new OrderMaterial();
		model.setOrder(new Order(order.getId()));
		orderMaterialDao.deleteObject(model);
		// 再绑定
		String pc = order.getProductCode();
		// 先找到对应的物料模板
		MaterialTemplate mt = mats.getMaterialTemplateService()
				.findByProductCode(pc);
		if (mt == null) {
			return false;
		}
		Set<Mtm> mtms = mt.getMtms();
		if (mtms != null && mtms.size() > 0) {
			// 取出模板中每种物料
			for (Mtm mtm : mtms) {
				Material m = mtm.getMaterial();
				OrderMaterial om = new OrderMaterial(m);
				om.setOrder(order);
				om.setUdate(order.getUdate());
				om.setUpdater(order.getTyper());
				orderMaterialDao.save(om);
			}
		}
		return flag;
	}

	/**
	 * 解析一行数据
	 * 
	 * @param row
	 * @return
	 * @throws ServiceException
	 */
	private Boolean parse(Row row, Order o, Map<String, Order> excelExistOrder)
			throws ServiceException {
		boolean flag = true;
		// 订单编号
		Cell cell = row.getCell(17);
		int cellType;
		if (cell == null) {
			ExcelUtil.createCommentForCell(cell, "订单编号不能为空");
			flag &= false;
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING)
				o.setNo(cell.getStringCellValue());
			else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setNo(new Double(cell.getNumericCellValue()).intValue() + "");
			} else {
				ExcelUtil.createCommentForCell(cell, "订单编号必须是由字符或数字组成");
				flag &= false;
			}
		}
		// 需要验证序号是否重复，产品代号是否有对应的BOM和工序模板
//		Order model = new Order();
//		model.setNo(o.getNo());
//		Order exists = this.find(model);
//		if (exists != null) {
//			ExcelUtil.createCommentForCell(cell, "订单序号不能重复");
//			flag &= false;
//		}
		// 保持序号不能在一个excel文件内不重复
		if (excelExistOrder.containsKey(o.getNo())) {
			ExcelUtil.createCommentForCell(cell, "订单序号不能和已有的重复，请用别的序号");
			flag &= false;
		}
		// 课题编号
		cell = row.getCell(1);
		if (cell != null) {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setProject(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setProject("");
			} else {
				ExcelUtil.createCommentForCell(cell, "课题编号必须是字符串");
				flag &= false;
			}
		} else {
			o.setProject("");
		}

		// 主管工艺
		cell = row.getCell(2);
		if (cell != null) {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setProcess(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setProcess("");
			} else {
				ExcelUtil.createCommentForCell(cell, "主管工艺必须是字符串");
				flag &= false;
			}
		} else {
			o.setProcess("");
		}

		// 使用型号
		cell = row.getCell(3);
		if (cell != null) {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setModel(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setModel("");
			} else {
				ExcelUtil.createCommentForCell(cell, "使用型号必须是字符串");
				flag &= false;
			}
		} else {
			o.setModel("");
		}

		// 产品名称
		cell = row.getCell(4);
		if (cell == null) {
			ExcelUtil.createCommentForCell(cell, "产品名称不能为空");
			flag &= false;
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setProductName(cell.getStringCellValue());
			} else {
				ExcelUtil.createCommentForCell(cell, "产品名称必须是字符串");
				flag &= false;
			}
		}

		// 产品代号
		cell = row.getCell(5);
		if (cell != null) {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setProductCode(cell.getStringCellValue());
				/*
				 * // 验证是否存在该产品代号对应的物料模板和工序模板 if (mats.getMtpcService().find( new
				 * MtProductCode(o.getProductCode().toLowerCase().trim())) == null)
				 * { ExcelUtil.createCommentForCell(cell, "该产品代号没有对应的物料模板"); flag &=
				 * false; } if (this.pms.getPtProductCodeService().find( new
				 * PtProductCode(o.getProductCode().toLowerCase().trim())) == null)
				 * { ExcelUtil.addCommentForCell(cell, "该产品代号没有对应的工序模板"); flag &=
				 * false; }
				 */
			} else {
				ExcelUtil.createCommentForCell(cell, "产品代号必须是字符串");
				flag &= false;
			}
		} else {
			ExcelUtil.createCommentForCell(cell, "产品代号不能为空");
		}

		// 计划调整情况
		cell = row.getCell(6);
		if (cell == null) {
			o.setPlanToAdjust("0");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setPlanToAdjust(cell.getNumericCellValue() + "");
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setPlanToAdjust("0");
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				o.setPlanToAdjust(cell.getStringCellValue());
			}
		}
		// 科二的计划投产数量
		cell = row.getCell(7);
		if (cell == null) {
			o.setKe2produceNum("0");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setKe2produceNum(cell.getNumericCellValue() + "");
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String tmp = cell.getStringCellValue();
				if (tmp.trim().equals("/")) {
					o.setKe2produceNum("0");
				} else {
					o.setKe2produceNum(tmp);
				}
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setKe2produceNum("0");
			} else {
				ExcelUtil.createCommentForCell(cell, "科二投产数量必须是数字");
				flag &= false;
			}
		}
		//车间计划数量
		cell = row.getCell(24);
		if (cell == null) {
			o.setProduceNum(0);
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setProduceNum((int)cell.getNumericCellValue());
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String tmp = cell.getStringCellValue();
				if (tmp.trim().equals("/")) {
					o.setProduceNum(0);
				} else {
					o.setProduceNum(Integer.parseInt(tmp));
				}
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setProduceNum(0);
			} else {
				ExcelUtil.createCommentForCell(cell, "车间计划数量必须是数字");
				flag &= false;
			}
		}
		// 交付数量
		cell = row.getCell(8);
		if (cell == null) {
			o.setDeliveryNum(0);
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setDeliveryNum((int) cell.getNumericCellValue());
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String tmp = cell.getStringCellValue();
				if (tmp.trim().equals("/")) {
					o.setDeliveryNum(0);
				} else {
					o.setDeliveryNum(Integer.parseInt(tmp));
				}
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setDeliveryNum(0);
			} else {
				ExcelUtil.createCommentForCell(cell, "交付数量必须是数字");
				flag &= false;
			}
		}
		// 科二的计划典试数量
		cell = row.getCell(9);
		if (cell == null) {
			o.setKe2DianshiNum("0");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setKe2DianshiNum(cell.getNumericCellValue() + "");
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String tmp = cell.getStringCellValue();
				if (tmp.trim().equals("/")) {
					o.setKe2DianshiNum("0");
				} else {
					o.setKe2DianshiNum(tmp);
				}
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setKe2DianshiNum("0");
			} else {
				ExcelUtil.createCommentForCell(cell, "科二典试数量必须是数字");
				flag &= false;
			}
		}
		
		//典试计划数量
		cell = row.getCell(31);
		if (cell == null) {
			o.setTestNum(0);
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setTestNum((int) cell.getNumericCellValue());
			} else if (cellType == Cell.CELL_TYPE_STRING) {
				String tmp = cell.getStringCellValue();
				if (tmp.trim().equals("/")) {
					o.setTestNum(0);
				} else {
					o.setTestNum(Integer.parseInt(tmp));
				}
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setTestNum(0);
			} else {
				ExcelUtil.createCommentForCell(cell, "典试计划数量必须是数字");
				flag &= false;
			}
		}

		// 产品批次
		cell = row.getCell(10);
		if (cell == null) {
			o.setProductBatchId("");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setProductBatchId(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				
			} else {
				ExcelUtil.createCommentForCell(cell, "产品批次必须是字符串");
				flag &= false;
			}
		}
		// 物资齐套时间
		cell = row.getCell(11);
		if (cell == null) {
			ExcelUtil.createCommentForCell(cell, "物资齐套时间不能为空，物资齐套时间格式必须是yyyyMMdd");
			flag &= false;
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				try {
					o.setMaterialsQitaoTime(DateUtil.parseDate(cell.getStringCellValue(), "yyyyMMdd"));
				} catch (Exception e) {
					ExcelUtil.createCommentForCell(cell, "物资齐套时间格式必须是yyyyMMdd，例如20141212");
					flag &= false;
					logger.error(e);
				}
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				try {
					o.setMaterialsQitaoTime(DateUtil.parseDate(new Double(cell.getNumericCellValue()).intValue() + "", "yyyyMMdd"));
				} catch (Exception e) {
					ExcelUtil.createCommentForCell(cell, "物资齐套时间格式必须是yyyyMMdd，例如20141212");
					flag &= false;
					logger.error(e);
				}
			} else {
				ExcelUtil.createCommentForCell(cell, "物资齐套时间格式必须是yyyyMMdd");
				flag &= false;
			}
		}

		// 零件齐套时间
		cell = row.getCell(12);
		if (cell == null) {
			ExcelUtil.createCommentForCell(cell, "零件齐套时间不能为空，零件齐套时间格式必须是yyyyMMdd，例如20141212");
			flag &= false;
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				try {
					o.setComponentCompleteness(DateUtil.parseDate(cell.getStringCellValue(), "yyyyMMdd"));
				} catch (Exception e) {
					ExcelUtil.createCommentForCell(cell, "零件齐套时间格式必须是yyyyMMdd，例如20141212");
					flag &= false;
					logger.error(e);
				}
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				try {
					o.setComponentCompleteness(DateUtil.parseDate(new Double(cell.getNumericCellValue()).intValue() + "", "yyyyMMdd"));
				} catch (Exception e) {
					ExcelUtil.createCommentForCell(cell, "零件齐套时间格式必须是yyyyMMdd，例如20141212");
					flag &= false;
					logger.error(e);
				}
			} else {
				ExcelUtil.createCommentForCell(cell, "零件齐套时间必须是日期，例如2014-09-08");
				flag &= false;
			}
		}
		// 证书完成时间
		cell = row.getCell(13);
		if (cell == null) {
			o.setCertificateDate("");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setCertificateDate(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setCertificateDate(new Double(cell.getNumericCellValue())
						.intValue() + "");
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setCertificateDate("");
			} else {
				ExcelUtil.createCommentForCell(cell, "证书完成时间必须是由字符或数字组成");
				flag &= false;
			}
		}
		// 入库进度
		cell = row.getCell(14);
		if (cell == null) {
			o.setStockInDate("");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setStockInDate(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				o.setStockInDate(new Double(cell.getNumericCellValue()).intValue()
						+ "");
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setStockInDate("");
			} else {
				ExcelUtil.createCommentForCell(cell, "入库进度必须是由字符或数字组成");
				flag &= false;
			}
		}
		// 科二备注
		cell = row.getCell(15);
		if (cell == null) {
			o.setRemark("");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setRemark(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setRemark("");
			} else {
				ExcelUtil.createCommentForCell(cell, "科二备注必须是由字符组成");
				flag &= false;
			}
		}
		// 班组
		cell = row.getCell(19);
		if (cell == null) {
			o.setBanzu("");
		} else {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				o.setBanzu(cell.getStringCellValue());
			} else if (cellType == Cell.CELL_TYPE_BLANK) {
				o.setBanzu("");
			} else {
				ExcelUtil.createCommentForCell(cell, "科二备注必须是由字符组成");
				flag &= false;
			}
		}
		// 订单编号
		/*
		cell = row.getCell(17);
		cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_STRING) {
			o.setNo(cell.getStringCellValue());
		} else if (cellType == Cell.CELL_TYPE_BLANK) {
			
		} else {
			ExcelUtil.createCommentForCell(cell, "订单编号必须是由字符组成");
			flag &= false;
		}
		*/

		// 计划上线日期 可以为空
		cell = row.getCell(20);
		String dateStr = "";
		try {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				dateStr = cell.getStringCellValue().trim();
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				dateStr = new Double(cell.getNumericCellValue()).intValue() + "";
			}
			Calendar cal = DateUtil.parseDate(dateStr, "yyyyMMdd");
			o.setOnlineDate(cal);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 计划完成日期 可以为空
		cell = row.getCell(21);
		try {
			cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_STRING) {
				dateStr = cell.getStringCellValue().trim();
			} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
				dateStr = new Double(cell.getNumericCellValue()).intValue() + "";
			}
			Calendar cal = DateUtil.parseDate(dateStr, "yyyyMMdd");
			o.setFinishDate(cal);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//车间备注
		try {
			o.setFactoryRemark(ExcelUtil.getCellStringCanNull(row, 63, "车间备注"));
		} catch (CellParseException e) {
			flag &= false;
		}
		//产品类别
		try {
			o.setProductType(ExcelUtil.getCellStringCanNull(row, 62, "产品类别"));
		} catch (CellParseException e) {
			flag &= false;
		}
		//工艺员
		try {
			o.setGongyiyuan(ExcelUtil.getCellStringCanNull(row, 16, "工艺员"));
		} catch (CellParseException e) {
			flag &= false;
		}
		//计划更改次数
		try {
			o.setPlanAdjustNum(ExcelUtil.getCellStringCanNull(row, 23, "计划更改次数"));
		} catch (CellParseException e) {
			flag &= false;
		}
		//最近一次计划更改日期
		try {
			o.setLastAdjustDate(ExcelUtil.getCellStringCanNull(row, 22, "最近一次计划更改日期"));
		} catch (CellParseException e) {
			flag &= false;
		}
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		o.setUdate(Calendar.getInstance());
		o.setTyper(new User(v.getUid()));
		o.setProcessIsCheck(Order.PROCESS_IS_NOT_CHECKED);
		return flag;
	}

	@Override
	public GridModel<OrderModel> list(OrderSearchForm form,
			ItemModelHelper<Order, OrderModel> helper) throws ServiceException {
		if (helper == null) {
			logger.warn("lack of itemHelper");
			throw new ServiceException("系统出错，请联系管理员");
		}
		if (form == null) {
			return this.list(null, -1, -1, helper);
		}
		Order model = getSearchModel(form);
		// 如果是查询一定时间内的订单，需要改变一下策略
		if (model.getReportBeginTime() != null
				&& model.getReportFinishedTime() != null) {
			String hql = "from "
					+ Order.class.getSimpleName()
					+ " as o where o.reportBeginTime >= ? and o.reportFinishedTime <= ?";
			List<Order> items = this.orderDao.list(hql, form.getPage(),
					form.getLimit(), model.getReportBeginTime(),
					model.getReportFinishedTime());
			GridModel<OrderModel> grid = new GridModel<OrderModel>();
			hql = "select count(*) " + hql;
			int sum = this.orderDao.count(hql, model.getReportBeginTime(),
					model.getReportFinishedTime());
			grid.setTotalNum(sum);
			List<OrderModel> itModel = new ArrayList<OrderModel>();
			for (Order m : items) {
				itModel.add(helper.transfer(m));
			}
			grid.setItems(itModel);
			return grid;
		}

		return this.list(model, form.getPage(), form.getLimit(), helper);
	}

	private Order getSearchModel(OrderSearchForm condition) {
		Order model = new Order();
		if (condition.getId() != null) {
			model.setId(condition.getId());
			return model;
		}
		if (condition.getNo() != null && !condition.getNo().isEmpty()) {
			model.setNo(condition.getNo());
		}
		if (condition.getProject() != null && !condition.getProject().isEmpty()) {
			model.setProject(condition.getProject());
		}
		if (condition.getProcess() != null && !condition.getProcess().isEmpty()) {
			model.setProcess(condition.getProcess());
		}
		if (condition.getModel() != null && !condition.getModel().isEmpty()) {
			model.setModel(condition.getModel());
		}
		if (condition.getProductName() != null
				&& !condition.getProductName().isEmpty()) {
			model.setProductName(condition.getProductName());
		}
		if (condition.getProductCode() != null
				&& !condition.getProductCode().isEmpty()) {
			model.setProductCode(condition.getProductCode());
		}
		if (condition.getProductBatchId() != null
				&& !condition.getProductBatchId().isEmpty()) {
			model.setProductBatchId(condition.getProductBatchId());
		}
		if (condition.getBanzu() != null && !condition.getBanzu().isEmpty()) {
			model.setBanzu(condition.getBanzu());
		}
		// 增加是否配套完成的查询条件---cwm
		if (condition.getAssignStatus() != null && condition.getAssignStatus() != -1) {
			model.setAssignStatus(condition.getAssignStatus());
		}
		// 增加是否报工完成的查询条件---cwm
		if (condition.getIsReported() != null && condition.getIsReported() != 2) {
			model.setIsReported(condition.getIsReported());
		}
		// 增加订单状态---cwm
		if (condition.getStatus() != null) {
			model.setStatus(condition.getStatus());
		}
		// 报工开始和报工结束
		if (condition.getReportBeginTime() != null) {
			try {
				model.setReportBeginTime(DateUtil.parseDate(condition
						.getReportBeginTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (condition.getReportFinishedTime() != null) {
			try {
				model.setReportFinishedTime(DateUtil.parseDate(condition
						.getReportFinishedTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (condition.getMaterialIsCheck() != null) {
			model.setMaterialIsChecked(condition.getMaterialIsCheck());
		}
		return model;
	}

	@Override
	public List<Order> findCanAutoScheduleOrder(List<Integer> orderIds) {
		List<Order> res = new ArrayList<Order>();
		if (orderIds.size() == 0)
			return res;
		// 按照id的顺序查找，不能批量查找
		for (Integer oid : orderIds) {
			res.add(orderDao.get(oid));
		}
		return res;
		// 选择物料已齐套的或者用户强制排产的订单
		/**
		 * StringBuilder oid = new StringBuilder(); for (Integer o : orderIds)
		 * oid.append(o).append(","); String tmp = oid.toString(); String hql =
		 * "from Order o where o.id in ("+tmp.substring(0, tmp.length() -
		 * 1)+")"; return this.orderDao.list(hql, -1, -1);
		 **/
	}

	@Override
	public List<Order> getAutoScheduleOrder(int pn, int pageSize)
			throws ServiceException {
		return this.orderDao.getAutoScheduleOrder(pn, pageSize);
	}

	@Override
	public int getAutoScheduleOrderCount() {
		return this.orderDao.getAutoScheduleOrderCount();
	}

	@Override
	public void saveOrUpdate(OrderModel order) throws ServiceException {
		Visitor v = (Visitor) SecurityContextHolder.getContext()
				.getAuthentication();
		int orderId = order.getId();
		Order o = new Order(order);
		o.setTyper(new User(v));
		o.setUdate(Calendar.getInstance());

		if (orderId > 0) {
			Order oldOrder = this.orderDao.get(order.getId());
			oldOrder.setProductName(order.getProductName());
			oldOrder.setProductCode(order.getProductCode());
			oldOrder.setProject(order.getProject());
			oldOrder.setProcess(order.getProcess());
			oldOrder.setModel(order.getModel());
			oldOrder.setProductBatchId(order.getProductBatchId());
			oldOrder.setProduceNum(order.getProduceNum());
			oldOrder.setDeliveryNum(order.getDeliveryNum());
			oldOrder.setTestNum(order.getTestNum());
			try {
				//完成时间
				oldOrder.setFinishDate(DateUtil.parseDate(order.getFinishDate()));
				oldOrder.setOnlineDate(DateUtil.parseDate(order.getOnlineDate()));
				//物料齐套时间
				oldOrder.setMaterialsCompleteness(DateUtil.parseDate(order.getMaterialsCompleteness()));
				oldOrder.setComponentCompleteness(DateUtil.parseDate(order.getComponentCompleteness()));
				oldOrder.setMaterialsQitaoTime(DateUtil.parseDate(order.getMaterialsQitaoTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			oldOrder.setPlanToAdjust(order.getPlanToAdjust());
			oldOrder.setCertificateDate(order.getCertificateDate());
			oldOrder.setStockInDate(order.getStockInDate());
			oldOrder.setRemark(order.getRemark());
			
			
			oldOrder.setKe2DianshiNum(order.getKe2DianshiNum());
			oldOrder.setKe2produceNum(order.getKe2ProduceNum());
			oldOrder.setBanzu(order.getBanzu());
			
			
			oldOrder.setFactoryRemark(order.getFactoryRemark());
			oldOrder.setGongyiyuan(order.getGongyiyuan());
			oldOrder.setPlanAdjustNum(order.getPlanAdjustNum());
			oldOrder.setLastAdjustDate(order.getLastAdjustDate());
			oldOrder.setProductType(order.getProductType());
			
			this.orderDao.update(oldOrder);
		} else {
			Order model = new Order();
			model.setNo(order.getNo());
			Order exists = this.find(model);
			if (exists != null)
				throw new ServiceException("订单编号不能重复，请使用别的订单编号");
			// 一些默认值
			o.setStatus(OrderStatus.INITIAL.getIndex());
			o.setIsAssigned(AssignType.UNFINISHED_ASSIGN);
			o.setIsReported(YesOrNo.NO.getIndex());
			o.setIsDispatchMaterial(YesOrNo.NO.getIndex());
			o.setProcessIsCheck(Order.PROCESS_IS_NOT_CHECKED);
			o.setAssignStatus(Order.ASSIGN_STATUS_FINISHED);
			orderId = this.orderDao.save(o);
		}
		// 前端只需把需要新增的发过来即可
		// 全部是新增
		Set<OrderMaterial> oms = o.getOms();
		for (OrderMaterial om : oms) {
			this.orderMaterialDao.save(om);
		}
		Set<OrderProcess> ops = o.getOps();
		for (OrderProcess op : ops) {
			this.orderProcessDao.save(op);
		}
	}

	/**
	 * 通过订单的条形码查找订单
	 * 
	 * @param code
	 * @return
	 * @throws ServiceException
	 */
	public Order getOrderByCode(String code) throws ServiceException {
		String[] data = code.split("-");
		String no = data[0];
		if (no == null || no.isEmpty())
			throw new ServiceException("参数出错");
		Order orderModel = new Order();
		orderModel.setNo(no);
		Order o = this.find(orderModel);
		if (o == null)
			throw new ServiceException("不存在该订单");
		return o;
	}

	@Override
	public GridModel<OrderModel> listCanAdjustOrder(OrderSearchForm form)
			throws ServiceException {
		GridModel<OrderModel> grid = new GridModel<OrderModel>();
		if (form.getStart() < 0 || form.getLimit() < 0) {
			throw new ServiceException("查询页数或者页面大小有误");
		}
		Page<Order> pages = this.orderDao.listCanAdjustOrder(form.getStart(),
				form.getLimit());
		grid.setTotalNum(pages.getContext().getTotal());
		List<Order> items = pages.getItems();
		List<OrderModel> itModel = new ArrayList<OrderModel>();
		for (Order m : items) {
			itModel.add(new OrderModel(m));
		}
		grid.setItems(itModel);
		return grid;
	}

	@Override
	public List<OrderModel> listScheduledOrders() {
		List<Order> orders = this.orderDao.listScheduledOrder();
		List<OrderModel> oms = new ArrayList<OrderModel>(orders.size());
		for (Order o : orders) {
			// 已经报工完成的不需要重排
			if (o.getIsReported() == 1)
				continue;
			oms.add(new OrderModel(o));
		}
		return oms;
	}

	@Override
	public void confirmOrderProcessInfo(Integer orderId) throws ServiceException {
		if (orderId == null)
			return;
		Order o = this.orderDao.get(orderId);
		Visitor v = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = v.getUser();
		if (o == null) {
			throw new ServiceException("不存在该订单");
		}
		o.setProcessIsCheck(Order.PROCESS_IS_CHECKED);
		o.setProcessCheckTime(Calendar.getInstance());
		o.setProcessChecker(u);
		this.orderDao.update(o);
	}

	@Override
	public void deleteOrderMaterial(Integer omid) throws ServiceException {
		OrderMaterial om = this.orderMaterialDao.get(new Long(omid));
		if (om == null) {
			throw new ServiceException("不存在该物料");
		}
		Set<Assign> as = om.getAs();
		if (as != null && as.size() > 0) {
			throw new ServiceException("该物料已经被配套过，无法删除");
		}
		this.orderMaterialDao.delete(new Long(omid));
	}

	@Override
	public void deleteOrderProcess(Integer opid) throws ServiceException {
		OrderProcess op = this.orderProcessDao.get(new Long(opid));
		if (op == null)
			throw new ServiceException("不存在该工序");
		Set<ProduceAssembling> prs = op.getPrs();
		if (prs != null && prs.size() > 0) {
			throw new ServiceException("该工序已经被工人报工过，无法删除");
		}
		this.orderProcessDao.delete(new Long(opid));
	}

	@Override
	public void confirmOrderMaterialInfo(Integer orderId)
			throws ServiceException {
		if (orderId == null)
			return;
		Order o = this.orderDao.get(orderId);
		Visitor v = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		User u = v.getUser();
		if (o == null) {
			throw new ServiceException("不存在该订单");
		}
		o.setMaterialIsChecked(Order.MATERIAL_IS_CHECKED);
		o.setMaterialCheckTime(Calendar.getInstance());
		o.setMaterialChecker(u);
		this.orderDao.update(o);
	}

	@Override
	public GridModel<OrderModel> listForceScheduleOrder() throws ServiceException {
		GridModel<OrderModel> grid = new GridModel<OrderModel>();
		List<OrderModel> result = new ArrayList<OrderModel>();
		List<Order> orders = this.orderDao.listForceScheduleOrder();
		for (Order o : orders) {
			result.add(new OrderModel(o));
		}
		grid.setItems(result);
		return grid;
	}
}
