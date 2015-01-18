package cn.edu.ustb.sem.process.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.process.service.ProcessModuleService;
import cn.edu.ustb.sem.process.service.ProcessService;
import cn.edu.ustb.sem.process.service.ProcessTemplateService;
import cn.edu.ustb.sem.process.service.PtProductCodeService;

@Service("processModuleService")
public class ProcessModuleServiceImpl implements ProcessModuleService {
	@Autowired
	private ProcessService processService;
	@Autowired
	private ProcessTemplateService ptService;
	@Autowired
	private PtProductCodeService ptcService;
	@Override
	public ProcessService getProcessService() {
		return processService;
	}

	@Override
	public ProcessTemplateService getPtService() {
		return ptService;
	}

	@Override
	public PtProductCodeService getPtProductCodeService() {
		return ptcService;
	}

}
