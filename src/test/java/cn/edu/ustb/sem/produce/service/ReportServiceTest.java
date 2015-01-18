package cn.edu.ustb.sem.produce.service;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import cn.edu.ustb.sem.BaseITTest;
import cn.edu.ustb.sem.account.entity.User;
import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.produce.dao.ProduceTinggongDao;
import cn.edu.ustb.sem.produce.entity.ProduceTinggong;

public class ReportServiceTest extends BaseITTest {
	@Autowired
	private ProduceTinggongDao tgDao;
	@Test
	@Rollback(true)
	public void testSaveNewTinggongHistory() {
		ProduceTinggong tg = new ProduceTinggong();
		tg.setDesc("ajdf");
		tg.setEnd(Calendar.getInstance());
		tg.setOrder(new Order(11));
		tg.setReason("adfdsf");
		tg.setStart(Calendar.getInstance());
		tg.setTinggongReporter(new User(2));
		tgDao.save(tg);
	}
}
