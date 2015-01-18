package cn.edu.ustb.sem.material.web;

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
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.service.MaterialModuleService;
import cn.edu.ustb.sem.material.service.MaterialTemplateService;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateModel;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateSearchModel;

import com.alibaba.fastjson.JSON;

@Controller
@Scope("prototype")
@RequestMapping("/material/template")
public class MaterialTemplateController extends BaseController {
	private static final String ERR_FILE = MaterialTemplateController.class.getSimpleName() + ".err";
	private static final String FILE_NAME = MaterialTemplateController.class.getSimpleName() + ".fileName";
	public static final String IMPORT_NUM = MaterialTemplateController.class.getSimpleName() + ".num";
	public static final String CUR_NUM = MaterialTemplateController.class.getSimpleName() + ".curNum";
	public static final String IS_UPLOAD_FINISHED = MaterialTemplateController.class.getSimpleName() + ".isUploadFinished";
	public static final String IS_UPLOAD_BEGIN = MaterialTemplateController.class.getSimpleName() + ".isUploadBegin";
	@Autowired
	private MaterialModuleService mats;

	@RequestMapping(value = "/upload")
	@ResponseBody
	public String upLoad(
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		ServiceContext.setRequest(request);
		MaterialTemplateService mts = mats.getMaterialTemplateService();
		Workbook wk = null;
		HttpSession session = request.getSession(true);
		try {
			session.setAttribute(IS_UPLOAD_BEGIN, true);
			session.setAttribute(IS_UPLOAD_FINISHED, false);
			wk = new XSSFWorkbook(file.getInputStream());
			session.setAttribute(IS_UPLOAD_BEGIN, false);
			session.setAttribute(IS_UPLOAD_FINISHED, true);
			mts.saveExcelFile(wk);
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
			return getErrorJsonMsg(e.getMessage() + ", 保存失败", null);
		} catch (IOException e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + ", 保存失败", null);
		} catch (Exception e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + ", 保存失败", null);
		}
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

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(MaterialTemplateSearchModel form) {
		try {
			GridModel<MaterialTemplateModel> mtl = mats
					.getMaterialTemplateService()
					.list(form,
							new ItemModelHelper<MaterialTemplate, MaterialTemplateModel>() {
								@Override
								public MaterialTemplateModel transfer(
										MaterialTemplate bo) {
									return new MaterialTemplateModel(bo);
								}
							});
			result.put("data", mtl);
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
			mats.getMaterialTemplateService().delete(tid);
			return getSuccessJsonMsg("删除成功", null);
		} catch (ServiceException e) {
			logger.warn(e + "" + tid);
			return getErrorJsonMsg("删除失败", null);
		}
	}

	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdate(MaterialTemplateModel mtl) {
		try {
			mats.getMaterialTemplateService().saveOrUpdate(mtl);
		} catch (ServiceException e) {
			logger.warn(e + "");
			return getErrorJsonMsg(e.getMessage() + ", 保存失败", null);
		}
		return getSuccessJsonMsg("保存成功", null);
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
}
