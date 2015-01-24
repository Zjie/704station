package cn.edu.ustb.sem.produce.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

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
	//生成条形码
	@RequestMapping(value = "/genCode")
	public void genCode(Integer srid, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		OutputStream output = response
				.getOutputStream();
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);
		try {
			BarCode barcode1 = getChart(request);
			barcode1.setSize(barcode1.width, barcode1.height);
			if (barcode1.autoSize) {
				BufferedImage bufferedimage = new BufferedImage(
						barcode1.getSize().width, barcode1.getSize().height, 13);
				java.awt.Graphics2D graphics2d = bufferedimage.createGraphics();
				barcode1.paint(graphics2d);
				barcode1.invalidate();
				graphics2d.dispose();
			}
			BufferedImage bufferedimage1 = new BufferedImage(
					barcode1.getSize().width, barcode1.getSize().height, 1);
			java.awt.Graphics2D graphics2d1 = bufferedimage1.createGraphics();
			barcode1.paint(graphics2d1);
			JPEGImageEncoder jpegimageencoder = JPEGCodec
					.createJPEGEncoder(output);
			JPEGEncodeParam jpegencodeparam = jpegimageencoder
					.getDefaultJPEGEncodeParam(bufferedimage1);
			jpegencodeparam.setQuality(1.0F, true);
			jpegimageencoder.setJPEGEncodeParam(jpegencodeparam);
			jpegimageencoder.encode(bufferedimage1, jpegencodeparam);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
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
	
	private Font convertFont(String s) {
		StringTokenizer stringtokenizer = new StringTokenizer(s, "|");
		String s1 = stringtokenizer.nextToken();
		String s2 = stringtokenizer.nextToken();
		String s3 = stringtokenizer.nextToken();
		byte byte0 = -1;
		if (s2.trim().toUpperCase().equals("PLAIN"))
			byte0 = 0;
		else if (s2.trim().toUpperCase().equals("BOLD"))
			byte0 = 1;
		else if (s2.trim().toUpperCase().equals("ITALIC"))
			byte0 = 2;
		return new Font(s1, byte0, (new Integer(s3)).intValue());
	}

	private Color convertColor(String s) {
		Color color = null;
		if (s.trim().toUpperCase().equals("RED"))
			color = Color.red;
		else if (s.trim().toUpperCase().equals("BLACK"))
			color = Color.black;
		else if (s.trim().toUpperCase().equals("BLUE"))
			color = Color.blue;
		else if (s.trim().toUpperCase().equals("CYAN"))
			color = Color.cyan;
		else if (s.trim().toUpperCase().equals("DARKGRAY"))
			color = Color.darkGray;
		else if (s.trim().toUpperCase().equals("GRAY"))
			color = Color.gray;
		else if (s.trim().toUpperCase().equals("GREEN"))
			color = Color.green;
		else if (s.trim().toUpperCase().equals("LIGHTGRAY"))
			color = Color.lightGray;
		else if (s.trim().toUpperCase().equals("MAGENTA"))
			color = Color.magenta;
		else if (s.trim().toUpperCase().equals("ORANGE"))
			color = Color.orange;
		else if (s.trim().toUpperCase().equals("PINK"))
			color = Color.pink;
		else if (s.trim().toUpperCase().equals("WHITE"))
			color = Color.white;
		else if (s.trim().toUpperCase().equals("YELLOW"))
			color = Color.yellow;
		return color;
	}

	private BarCode getChart(HttpServletRequest request) {
		if (barcode == null)
			barcode = new BarCode();
		try {
			setParameter("barType", request.getParameter("barType"));
			if (request.getParameter("width") != null
					&& request.getParameter("height") != null) {
				setParameter("width", request.getParameter("width"));
				setParameter("height", request.getParameter("height"));
				setParameter("autoSize", "n");
			}
			setParameter("code", request.getParameter("code"));
			setParameter("st", request.getParameter("st"));
			setParameter("textFont", request.getParameter("textFont"));
			setParameter("fontColor", request.getParameter("fontColor"));
			setParameter("barColor", request.getParameter("barColor"));
			setParameter("backColor", request.getParameter("backColor"));
			setParameter("rotate", request.getParameter("rotate"));
			setParameter("barHeightCM", request.getParameter("barHeightCM"));
			setParameter("x", request.getParameter("x"));
			setParameter("n", request.getParameter("n"));
			setParameter("leftMarginCM", request.getParameter("leftMarginCM"));
			setParameter("topMarginCM", request.getParameter("topMarginCM"));
			setParameter("checkCharacter",
					request.getParameter("checkCharacter"));
			setParameter("checkCharacterInText",
					request.getParameter("checkCharacterInText"));
			setParameter("Code128Set", request.getParameter("Code128Set"));
			setParameter("UPCESytem", request.getParameter("UPCESytem"));
		} catch (Exception exception) {
			exception.printStackTrace();
			barcode.code = "Parameter Error";
		}
		return barcode;
	}

	private void setParameter(String key, String value) {
		if (value != null)
			if (key.equals("code"))
				barcode.code = value;
			else if (key.equals("width"))
				barcode.width = (new Integer(value)).intValue();
			else if (key.equals("height"))
				barcode.height = (new Integer(value)).intValue();
			else if (key.equals("autoSize"))
				barcode.autoSize = value.equalsIgnoreCase("y");
			else if (key.equals("st"))
				barcode.showText = value.equalsIgnoreCase("y");
			else if (key.equals("textFont"))
				barcode.textFont = convertFont(value);
			else if (key.equals("fontColor"))
				barcode.fontColor = convertColor(value);
			else if (key.equals("barColor"))
				barcode.barColor = convertColor(value);
			else if (key.equals("backColor"))
				barcode.backColor = convertColor(value);
			else if (key.equals("rotate"))
				barcode.rotate = (new Integer(value)).intValue();
			else if (key.equals("barHeightCM"))
				barcode.barHeightCM = (new Double(value)).doubleValue();
			else if (key.equals("x"))
				barcode.X = (new Double(value)).doubleValue();
			else if (key.equals("n"))
				barcode.N = (new Double(value)).doubleValue();
			else if (key.equals("leftMarginCM"))
				barcode.leftMarginCM = (new Double(value)).doubleValue();
			else if (key.equals("topMarginCM"))
				barcode.topMarginCM = (new Double(value)).doubleValue();
			else if (key.equals("checkCharacter"))
				barcode.checkCharacter = value.equalsIgnoreCase("y");
			else if (key.equals("checkCharacterInText"))
				barcode.checkCharacterInText = value.equalsIgnoreCase("y");
			else if (key.equals("Code128Set"))
				barcode.Code128Set = value.charAt(0);
			else if (key.equals("UPCESytem"))
				barcode.UPCESytem = value.charAt(0);
			else if (key.equals("barType"))
				if (value.equalsIgnoreCase("CODE39"))
					barcode.barType = 0;
				else if (value.equalsIgnoreCase("CODE39EXT"))
					barcode.barType = 1;
				else if (value.equalsIgnoreCase("INTERLEAVED25"))
					barcode.barType = 2;
				else if (value.equalsIgnoreCase("CODE11"))
					barcode.barType = 3;
				else if (value.equalsIgnoreCase("CODABAR"))
					barcode.barType = 4;
				else if (value.equalsIgnoreCase("MSI"))
					barcode.barType = 5;
				else if (value.equalsIgnoreCase("UPCA"))
					barcode.barType = 6;
				else if (value.equalsIgnoreCase("IND25"))
					barcode.barType = 7;
				else if (value.equalsIgnoreCase("MAT25"))
					barcode.barType = 8;
				else if (value.equalsIgnoreCase("CODE93"))
					barcode.barType = 9;
				else if (value.equalsIgnoreCase("EAN13"))
					barcode.barType = 10;
				else if (value.equalsIgnoreCase("EAN8"))
					barcode.barType = 11;
				else if (value.equalsIgnoreCase("UPCE"))
					barcode.barType = 12;
				else if (value.equalsIgnoreCase("CODE128"))
					barcode.barType = 13;
				else if (value.equalsIgnoreCase("CODE93EXT"))
					barcode.barType = 14;
				else if (value.equalsIgnoreCase("POSTNET"))
					barcode.barType = 15;
				else if (value.equalsIgnoreCase("PLANET"))
					barcode.barType = 16;
				else if (value.equalsIgnoreCase("UCC128"))
					barcode.barType = 17;
	}
}
