package cn.edu.ustb.sem.material.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.material.service.MaterialModuleService;
import cn.edu.ustb.sem.material.service.MaterialService;
import cn.edu.ustb.sem.material.service.MaterialTemplateService;
import cn.edu.ustb.sem.material.service.MtProductCodeService;

@Service("materialModuleService")
public class MaterialModuleServiceImpl implements MaterialModuleService {
	@Autowired
	private MaterialService ms;
	@Autowired
	private MaterialTemplateService mts;
	@Autowired
	private MtProductCodeService mtpc;
	@Override
	public MaterialService getMaterialService() {
		return ms;
	}

	@Override
	public MaterialTemplateService getMaterialTemplateService() {
		return mts;
	}

	@Override
	public MtProductCodeService getMtpcService() {
		return mtpc;
	}

}
