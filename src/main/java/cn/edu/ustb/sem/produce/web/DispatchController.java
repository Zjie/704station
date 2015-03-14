package cn.edu.ustb.sem.produce.web;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.util.BarCode;
import cn.edu.ustb.sem.core.util.DateUtil;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.order.service.OrderService;
import cn.edu.ustb.sem.produce.service.DispatchService;
import cn.edu.ustb.sem.produce.service.ReportService;
import cn.edu.ustb.sem.produce.web.model.DispatchResultModel;
import cn.edu.ustb.sem.schedule.entity.Worker;

import com.onbarcode.barcode.Code128;
@Controller
@Scope("prototype")
@RequestMapping("/produce")
public class DispatchController extends BaseController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private DispatchService dispatchService;
	@Autowired
	private ReportService reportService;
	
	BarCode barcode;

	// 当前排产计划
	@RequestMapping(value = "/currentPlan")
	@ResponseBody
	public Map<String, Object> currentPlan() {
		try {
			Visitor v = this.getVisitor();
			result.put("data", dispatchService.getCurrentDispatchResult(v));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}

	// 选择工序组
	@RequestMapping(value = "/select")
	public ModelAndView select(Integer oid) {
		try {
			List<DispatchResultModel> drm = dispatchService
					.getOrderDispatchResult(oid);
			result.put("order", drm.get(0).order);
			result.put("drm", drm);
			return new ModelAndView("/produce/select", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
		}
		return null;
	}

	// 打印派工单
	@RequestMapping(value = "/print")
	public ModelAndView print(Integer srid) {
		try {
			Visitor v = getVisitor();
			Worker w = v.getUser().getWorker();
			DispatchResultModel drm = dispatchService.printDispatchResultModel(srid, this.getVisitor().getUid());
			result.put("order", drm.order);
			result.put("drm", drm);
			result.put("printDate", DateUtil.getDate(Calendar.getInstance(), "yyyy-MM-dd"));
			result.put("worker", w);
			return new ModelAndView("/produce/print", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
		}
		return null;
	}
	
	@RequestMapping(value = "/getCanReportOrder")
	@ResponseBody
	public Map<String, Object> getCanReportOrder() {
		try {
			result.put("data", reportService.getCanReportOrder(this.getVisitor().getUser().getWorker().getId()));
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	//生成条形码
	@RequestMapping(value = "/genCode")
	public void genCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String code = request.getParameter("code");
        Code128 barcode = new Code128(); 
        barcode.setData(code); 
        ServletOutputStream servletoutputstream = response.getOutputStream(); 
        response.setContentType("image/jpeg"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setHeader("Cache-Control", "no-cache"); 
        response.setDateHeader("Expires", 0); 
        // Generate Code-128 barcode & output to ServletOutputStream
        try {
			barcode.drawBarcode(servletoutputstream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
