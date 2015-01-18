package cn.edu.ustb.sem.schedule.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import cn.edu.ustb.sem.BaseITTest;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.schedule.dao.ScheduleResultDao;

public class ScheduleServiceTest extends BaseITTest {
	@Autowired
	private ScheduleService ss;
	@Autowired
	private ScheduleResultDao sd;
	@Test
	@Rollback(false)
	public void testAutoSch() throws ServiceException {
		List<Integer> ids =  new ArrayList<Integer>();
		ids.add(18);
		ids.add(22);
		ids.add(17);
		ids.add(19);
		ss.autoSche(10, ids, null);
	}
	@Test
	@Rollback(false)
	public void testDeleteSrByGupidsAndOrderId() {
		Integer orderId = 41;
		Set<Integer> gupids = new HashSet<Integer>();
		gupids.add(9);
		gupids.add(10);
		this.sd.deleteByGupidsAndOrderId(gupids, orderId);
	}
}
