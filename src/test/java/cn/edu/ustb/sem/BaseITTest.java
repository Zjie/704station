package cn.edu.ustb.sem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.ustb.sem.core.util.ServiceContext;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class BaseITTest {
	private HttpServletRequest request;    
    private HttpServletResponse response;
	@Before
	public void setup() {
		//创建request和response的Mock    
//        request = new MockHttpServletRequest();
//        response = new MockHttpServletResponse();
//        ServiceContext.setRequest(request);
//        ServiceContext.setResponse(response);
	}
}
