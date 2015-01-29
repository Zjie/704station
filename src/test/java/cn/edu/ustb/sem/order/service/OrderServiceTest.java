package cn.edu.ustb.sem.order.service;

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

public class OrderServiceTest extends BaseITTest {
	@Autowired
	private OrderService orderService;
	@Rollback(false)
	@Test
	public void testImportExcel() throws ServiceException, IOException {
		Visitor v = new Visitor("joller", 1, true, "", 10);
		SecurityContextHolder.getContext().setAuthentication(v);
		InputStream input = new FileInputStream("D:\\program\\workspace\\704station\\src\\test\\resources\\order.xlsx");
		Workbook wk = null;
		wk = new XSSFWorkbook(input);
//		orderService.importExcel(wk);
		OutputStream os = new FileOutputStream("D:\\program\\workspace\\704station\\src\\test\\resources\\order_error.xlsx");
		wk.write(os);
	}
}
