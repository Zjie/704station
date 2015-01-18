package cn.edu.ustb.sem.process.dao.impl;

import org.springframework.stereotype.Repository;

import cn.edu.ustb.sem.core.dao.impl.BaseDaoImpl;
import cn.edu.ustb.sem.process.dao.ProcessTemplateDao;
import cn.edu.ustb.sem.process.entity.ProcessTemplate;
@Repository("processTemplateDao")
public class ProcessTemplateDaoImpl extends BaseDaoImpl<ProcessTemplate, Integer> implements ProcessTemplateDao {

}
