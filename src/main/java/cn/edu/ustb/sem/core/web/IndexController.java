package cn.edu.ustb.sem.core.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.edu.ustb.sem.core.auth.bo.Visitor;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.service.IndexService;
import cn.edu.ustb.sem.core.web.model.TreeStore;

@Controller
@Scope("prototype")
@RequestMapping("/")
public class IndexController extends BaseController {
	@Autowired
	private IndexService indexService;
	@RequestMapping(value = "/index")
	public ModelAndView index() throws JsonGenerationException, JsonMappingException, IOException {
		Visitor visitor = (Visitor) SecurityContextHolder.getContext().getAuthentication();
		Map<String, String> result = new HashMap<String, String>();
		result.put("username", visitor.getName());
		TreeStore ts;
		try {
			ts = indexService.getTreeStore(visitor.getRoleId());
//			result.put("treeStore", JSON.toJSONString(ts));
			ObjectMapper om = new ObjectMapper();
			String data = om.writeValueAsString(ts);
			result.put("treeStore", data);
		} catch (ServiceException e) {
			logger.warn(e + "");
		}
		return new ModelAndView("/index", result);
	}
}
