package cn.edu.ustb.sem.process.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.util.ServiceContext;
import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
import cn.edu.ustb.sem.process.service.ProcessTemplateService;
import cn.edu.ustb.sem.process.web.model.ProcessTemplateModel;
import cn.edu.ustb.sem.process.web.model.ProcessTemplateSearchForm;

import com.alibaba.fastjson.JSON;

@Controller
@Scope("prototype")
@RequestMapping("/process/template")
public class ProcessTemplateController extends BaseController {
	private static final String ERR_FILE = ProcessTemplateController.class.getSimpleName() + ".err";
	private static final String FILE_NAME = ProcessTemplateController.class.getSimpleName() + ".fileName";
	public static final String IMPORT_NUM = ProcessTemplateController.class.getSimpleName() + ".num";
	public static final String CUR_NUM = ProcessTemplateController.class.getSimpleName() + ".curNum";
	public static final String IS_UPLOAD_FINISHED = ProcessTemplateController.class.getSimpleName() + ".isUploadFinished";
	public static final String IS_UPLOAD_BEGIN = ProcessTemplateController.class.getSimpleName() + ".isUploadBegin";
	@Autowired
	private ProcessTemplateService pts;
	@RequestMapping(value = "/upload")
	@ResponseBody
	public String upLoad(
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		ServiceContext.setRequest(request);
		Workbook wk = null;
		HttpSession session = request.getSession(true);
		try {
			session.setAttribute(IS_UPLOAD_BEGIN, true);
			session.setAttribute(IS_UPLOAD_FINISHED, false);
			wk = new XSSFWorkbook(file.getInputStream());
			pts.importExcelFile(wk);
			session.setAttribute(IS_UPLOAD_BEGIN, false);
			session.setAttribute(IS_UPLOAD_FINISHED, true);
			if (wk.getNumberOfSheets() > 0) {
				//如果有保存失败的模板
				session.setAttribute(ERR_FILE, wk);
				session.setAttribute(FILE_NAME, file.getOriginalFilename());
				Map<String, Object> addInfo = new HashMap<String, Object>();
				addInfo.put("errorUrl", "getErrorExcelFile.do");
				return getErrorJsonMsg("共有" + wk.getNumberOfSheets() + "个模板保存失败，请点击确定下载出错的模板！", addInfo);
			} else {
				return getSuccessJsonMsg("保存成功", null);
			}
		} catch (ServiceException e) {
			logger.warn(e + "");
		} catch (IOException e) {
			logger.warn(e + "");
		} catch (Exception e) {
			logger.warn(e + "");
		}
		return getErrorJsonMsg("保存失败", null);
	}
	/**
	 * 导出出错的excel文件
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getErrorExcelFile")
	public void getErrorExcelFile(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			Workbook wk = (Workbook)session.getAttribute(ERR_FILE);
			if (wk == null) {
				return;
			}
			// 进行转码，使其支持中文文件名
			codedFileName = (String) session.getAttribute(FILE_NAME);
			codedFileName = new String(codedFileName.getBytes(), "iso8859-1");
			response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
			fOut = response.getOutputStream();
			wk.write(fOut);
			//删除掉临时文件
			session.removeAttribute(ERR_FILE);
		} catch (UnsupportedEncodingException e1) {
		} catch (Exception e) {
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
			}
		}
	}
	@RequestMapping(value = "/importProcess")
	@ResponseBody
	public String importProcess(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object process = session.getAttribute(CUR_NUM);
		int cur = 0;
		if (process != null) {
			cur = (int)process;
		}
		Object all =  session.getAttribute(IMPORT_NUM);
		int total = 0;
		if (all != null) {
			total = (int)all;
		}
		Object end = session.getAttribute(IS_UPLOAD_FINISHED);
		boolean endFlag = false;
		if (end != null) {
			endFlag = (boolean)end;
		}
		if (endFlag) {
			//如果上传完成
			result.put("wait", false);
			result.put("totalNum", total);
			result.put("cur", cur);
			session.setAttribute(IMPORT_NUM, 0);
			session.setAttribute(CUR_NUM, 0);
			session.setAttribute(IS_UPLOAD_BEGIN, true);
			session.setAttribute(IS_UPLOAD_FINISHED, false);
		} else {
			result.put("wait", true);
		}
		return JSON.toJSONString(result);
	}
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(ProcessTemplateSearchForm form) {
		try {
			GridModel<ProcessTemplateModel> grid = this.pts.list(form, new ItemModelHelper<ProcessTemplate, ProcessTemplateModel>(){
				@Override
				public ProcessTemplateModel transfer(ProcessTemplate bo) {
					return new ProcessTemplateModel(bo);
				}});
			result.put("data", grid);
			return getSuccessJsonResult("", result);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonResult(e.getMessage(), null);
		}
	}
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam int tid) {
		try {
			pts.delete(tid);
			return getSuccessJsonMsg("删除成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "" + tid);
			return getErrorJsonMsg("删除失败", null);
		}
	}
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdate(ProcessTemplateModel model) {
		try {
			pts.saveOrUpdate(model);
			return getSuccessJsonMsg("保存成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + ", 保存失败", null);
		}
	}
}
