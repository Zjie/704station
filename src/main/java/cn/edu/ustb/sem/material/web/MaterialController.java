package cn.edu.ustb.sem.material.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.edu.ustb.sem.core.web.BaseController;
import cn.edu.ustb.sem.material.service.MaterialModuleService;
import cn.edu.ustb.sem.material.web.model.MaterialModel;

@Controller
@Scope("prototype")
@RequestMapping("/material/material")
public class MaterialController extends BaseController {
	@Autowired
	private MaterialModuleService mats;

	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(MaterialModel msm) {
		return JSON.toJSONString(msm);
	}
}
