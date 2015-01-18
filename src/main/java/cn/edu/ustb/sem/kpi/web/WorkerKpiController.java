package cn.edu.ustb.sem.kpi.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.kpi.service.KpiService;
import cn.edu.ustb.sem.kpi.web.model.OrderProduceInfo;
import cn.edu.ustb.sem.kpi.web.model.WorkerKpiSearchForm;
import cn.edu.ustb.sem.produce.service.ReportService;
import cn.edu.ustb.sem.produce.web.model.ProduceAssembingModel;

@Controller
@Scope("prototype")
@RequestMapping("/kpi")
public class WorkerKpiController extends BaseController {
	@Autowired
	private ReportService reportService;
	@Autowired
	private KpiService kpiService;
	@RequestMapping(value = "/workerKpi", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> workerKpi(WorkerKpiSearchForm form) throws ServiceException{
		try {
			GridModel<ProduceAssembingModel> data = this.reportService.getProduceReportByForm(form);
			result.put("data", data);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/workerReportDetail")
	public ModelAndView produceReport(WorkerKpiSearchForm form){
		try {
			Map<String, Object> result = this.reportService.produceReport(form);
			result.put("workerId", form.getWorkerId());
			return new ModelAndView("/kpi/workerReportDetail", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			result.put("errorMsg", e.getMessage());
			return new ModelAndView("/error", result);
		}
	}
	//输出处于生产阶段的订单，也就是已经被排产的订单。包括：排产、停工、外协、
	@RequestMapping(value = "/outputCurrentPlan")
	public void outputCurrentPlan(HttpServletResponse response) {
		try {
			List<OrderProduceInfo> opis = this.kpiService.listCurPlan();
			// 生成提示信息，
			response.setContentType("application/vnd.ms-excel");
			String codedFileName = null;
			OutputStream fOut = null;
			try {
				fOut = response.getOutputStream();
				Workbook wk = new HSSFWorkbook();
				Sheet sheet = wk.createSheet("车间实时生产情况");
				int i = 0;
				Row row = sheet.createRow(i);
				Cell cell = row.createCell(0);
				cell.setCellValue("生产单元");
				cell = row.createCell(1);
				cell.setCellValue("订单编号");
				cell = row.createCell(2);
				cell.setCellValue("产品代号");
				cell = row.createCell(3);
				cell.setCellValue("投产数量");
				cell = row.createCell(4);
				cell.setCellValue("完工数量");
				cell = row.createCell(5);
				cell.setCellValue("完成率");
				cell = row.createCell(6);
				cell.setCellValue("生产状态");
				cell = row.createCell(7);
				cell.setCellValue("备注");
				for (OrderProduceInfo opi : opis) {
					i++;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue(opi.getUnit());
					cell = row.createCell(1);
					cell.setCellValue(opi.getOrderNo());
					cell = row.createCell(2);
					cell.setCellValue(opi.getCode());
					cell = row.createCell(3);
					cell.setCellValue(opi.getProduceNum());
					cell = row.createCell(4);
					cell.setCellValue(opi.getProcessNum());
					cell = row.createCell(5);
					cell.setCellValue(opi.getFinishedRate());
					cell = row.createCell(6);
					cell.setCellValue(opi.getStatus());
					cell = row.createCell(7);
					cell.setCellValue("");
				}
				// 进行转码，使其支持中文文件名
				codedFileName = "车间实时生产情况.xls";
				codedFileName = new String(codedFileName.getBytes(), "iso8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
				wk.write(fOut);
			} catch (UnsupportedEncodingException e1) {
			} catch (Exception e) {
			} finally {
				try {
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
				}
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/getWorkerCanReportOrder")
	@ResponseBody
	public Map<String, Object> getWorkerCanReportOrder(Integer workerId) {
		try {
			result.put("data", reportService.getCanReportOrder(workerId));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	
	@RequestMapping(value = "/aYearPlan")
	public void aYearPlan(HttpServletResponse response) {
		try {
			Collection<OrderProduceInfo> opis = this.kpiService.listAYearPlan();
			// 生成提示信息，
			response.setContentType("application/vnd.ms-excel");
			String codedFileName = null;
			OutputStream fOut = null;
			try {
				fOut = response.getOutputStream();
				Workbook wk = new HSSFWorkbook();
				Sheet sheet = wk.createSheet("车间全年情况");
				int i = 0;
				Row row = sheet.createRow(i);
				Cell cell = row.createCell(0);
				cell.setCellValue("产品代号");
				cell = row.createCell(1);
				cell.setCellValue("计划产量");
				cell = row.createCell(2);
				cell.setCellValue("实际产出");
				cell = row.createCell(3);
				cell.setCellValue("差额");
				cell = row.createCell(4);
				cell.setCellValue("完成率");
				cell = row.createCell(5);
				cell.setCellValue("备注");
				for (OrderProduceInfo opi : opis) {
					i++;
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue(opi.getCode());
					cell = row.createCell(1);
					cell.setCellValue(opi.getProduceNum());
					cell = row.createCell(2);
					cell.setCellValue(opi.getProcessNum());
					cell = row.createCell(3);
					cell.setCellValue(opi.getDiff());
					cell = row.createCell(4);
					cell.setCellValue(opi.getFinishedRate());
					cell = row.createCell(5);
					cell.setCellValue("");
					cell = row.createCell(6);
				}
				// 进行转码，使其支持中文文件名
				codedFileName = "车间全年情况.xls";
				codedFileName = new String(codedFileName.getBytes(), "iso8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
				wk.write(fOut);
			} catch (UnsupportedEncodingException e1) {
			} catch (Exception e) {
			} finally {
				try {
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
				}
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
