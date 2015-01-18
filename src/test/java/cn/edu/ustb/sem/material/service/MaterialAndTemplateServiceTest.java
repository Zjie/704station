package cn.edu.ustb.sem.material.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;

import cn.edu.ustb.sem.BaseITTest;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
import cn.edu.ustb.sem.material.dao.MaterialTemplateDao;
import cn.edu.ustb.sem.material.entity.MaterialTemplate;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateModel;
import cn.edu.ustb.sem.material.web.model.MaterialTemplateSearchModel;

public class MaterialAndTemplateServiceTest extends BaseITTest {
	@Autowired
	private MaterialTemplateService materialTemplateService;
	@Autowired
	private MaterialTemplateDao materialTemplateDao;
	@Rollback(true)
	@Test
	public void testTransferExcelAndSave() {
		Visitor v = new Visitor("joller", 1, true, "", 10);
		SecurityContextHolder.getContext().setAuthentication(v);
		InputStream input = null;
		Workbook wk = null;
		OutputStream os = null;
		try {
			input = new FileInputStream("D:\\program\\workspace\\704station\\src\\test\\resources\\material_template.xlsx");
			wk = new XSSFWorkbook(input);
			os = new FileOutputStream("D:\\program\\workspace\\704station\\src\\test\\resources\\material_template_error.xlsx");
			materialTemplateService.saveExcelFile(wk);
			wk.write(os);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Rollback(true)
	@Test
	public void testfindBySearchForm() throws ServiceException {
		MaterialTemplateSearchModel sf = new MaterialTemplateSearchModel();
		sf.setName("m1");
		sf.setProductCode("cw1-11-15");
		GridModel<MaterialTemplateModel> mtl = materialTemplateService.list(sf, new ItemModelHelper<MaterialTemplate, MaterialTemplateModel>() {
			@Override
			public MaterialTemplateModel transfer(
					MaterialTemplate bo) {
				return new MaterialTemplateModel(bo);
			}
		});
		Assert.assertTrue(mtl != null);
	}
	@Test
	public void testGetMtms() throws ServiceException {
		MaterialTemplate mt = materialTemplateDao.get(1);
		Object mtms = mt.getMtms();
		Assert.assertTrue(mtms != null);
	}
}
