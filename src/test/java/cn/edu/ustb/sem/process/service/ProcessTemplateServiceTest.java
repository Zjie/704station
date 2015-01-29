package cn.edu.ustb.sem.process.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;

import cn.edu.ustb.sem.BaseITTest;
import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;

public class ProcessTemplateServiceTest extends BaseITTest {
	@Autowired
	private ProcessTemplateService processTemplateService;
	@Test
	@Rollback(true)
	public void testImportExcel() throws IOException, ServiceException {
		Visitor v = new Visitor("joller", 1, true, "", 10);
		SecurityContextHolder.getContext().setAuthentication(v);
		InputStream input = new FileInputStream("D:\\program\\workspace\\704station\\src\\test\\resources\\process.xlsx");
		Workbook wk = new XSSFWorkbook(input);
//		processTemplateService.saveSheet(wk);
		OutputStream os = new FileOutputStream("D:\\program\\workspace\\704station\\src\\test\\resources\\process_error.xlsx");
		wk.write(os);
	}
}
