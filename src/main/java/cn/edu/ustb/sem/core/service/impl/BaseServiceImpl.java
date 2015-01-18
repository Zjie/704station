package cn.edu.ustb.sem.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.pagination.Constants;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.pagination.PageUtil;
import cn.edu.ustb.sem.core.service.BaseService;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;
public abstract class BaseServiceImpl<Bo extends java.io.Serializable, Vo, PK extends java.io.Serializable> implements BaseService<Bo, Vo, PK> {
	protected final Log logger = LogFactory.getLog(getClass());
	protected BaseDao<Bo, PK> baseDao;
    public abstract void setBaseDao(BaseDao<Bo, PK> baseDao);

    @Override
    public Bo save(Bo model) throws ServiceException {
        baseDao.save(model);
        return model;
    }
    
    @Override
    public void merge(Bo model) throws ServiceException {
        baseDao.merge(model);
    }

    @Override
    public void saveOrUpdate(Bo model) throws ServiceException {
        baseDao.saveOrUpdate(model);
    }

    @Override
    public void update(Bo model) throws ServiceException {
        baseDao.update(model);
    }
    
    @Override
    public void delete(PK id) throws ServiceException {
        baseDao.delete(id);
    }

    @Override
    public void deleteObject(Bo model) throws ServiceException {
        baseDao.deleteObject(model);
    }

    @Override
    public Bo get(PK id) throws ServiceException {
        return baseDao.get(id);
    }

   
    
    @Override
    public int countAll() throws ServiceException {
        return baseDao.countAll();
    }

    @Override
    public List<Bo> listAll() throws ServiceException {
        return baseDao.listAll();
    }
    @Override
    public Page<Bo> listAll(int pn) throws ServiceException {

        return this.listAll(pn, Constants.DEFAULT_PAGE_SIZE);
    }
    
    public Page<Bo> listAllWithOptimize(int pn) throws ServiceException {
        return this.listAllWithOptimize(pn, Constants.DEFAULT_PAGE_SIZE);
    }
    @Override
    public Page<Bo> listAll(Bo model, int pn, int pageSize) throws ServiceException {
    	Integer count = this.countAll();
    	List<Bo> items = this.baseDao.listAll(model, pn, pageSize);
    	return PageUtil.getPage(count, pn, items, pageSize);
    }
    @Override
    public Page<Bo> listAll(int pn, int pageSize) throws ServiceException {
        Integer count = countAll();
        List<Bo> items = baseDao.listAll(pn, pageSize);
        return PageUtil.getPage(count, pn, items, pageSize);
    }
    public Page<Bo> listAllWithOptimize(int pn, int pageSize) throws ServiceException {
        Integer count = countAll();
        List<Bo> items = baseDao.listAll(pn, pageSize);
        return PageUtil.getPage(count, pn, items, pageSize);
    }
    
    @Override
    public Page<Bo> pre(PK pk, int pn, int pageSize) throws ServiceException {
        Integer count = countAll();
        List<Bo> items = baseDao.pre(pk, pn, pageSize);
        return PageUtil.getPage(count, pn, items, pageSize);
    }
    @Override
    public Page<Bo> next(PK pk, int pn, int pageSize) throws ServiceException {
        Integer count = countAll();
        List<Bo> items = baseDao.next(pk, pn, pageSize);
        return PageUtil.getPage(count, pn, items, pageSize);
    }
    @Override
    public Page<Bo> pre(PK pk, int pn) throws ServiceException {
        return pre(pk, pn, Constants.DEFAULT_PAGE_SIZE);
    }
    @Override
    public Page<Bo> next(PK pk, int pn) throws ServiceException {
        return next(pk, pn, Constants.DEFAULT_PAGE_SIZE);
    }
   @Override
   public GridModel<Vo> list(Bo form, int pn, int pageSize, ItemModelHelper<Bo, Vo> helper) throws ServiceException {
	   GridModel<Vo> grid =  new GridModel<Vo>();
	   if (pn < 0 || pageSize < 0) {
		   throw new ServiceException("查询页数或者页面大小有误");
	   }
	   Page<Bo> pages = this.listAll(form, pn, pageSize);
	   grid.setTotalNum(pages.getContext().getTotal());
	   List<Bo> items = pages.getItems();
	   List<Vo> itModel = new ArrayList<Vo>();
	   for (Bo m : items) {
		   itModel.add(helper.transfer(m));
	   }
	   grid.setItems(itModel);
	   return grid;
   }
   
   @Override
   public Bo find(Bo model) throws ServiceException {
	   return baseDao.find(model);
   }
}
