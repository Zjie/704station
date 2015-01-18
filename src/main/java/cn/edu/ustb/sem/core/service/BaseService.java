package cn.edu.ustb.sem.core.service;

import java.util.List;

import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.web.model.GridModel;
import cn.edu.ustb.sem.core.web.model.ItemModelHelper;

public interface BaseService<Bo extends java.io.Serializable, Vo, PK extends java.io.Serializable> {
	public Bo save(Bo model) throws ServiceException;

    public void saveOrUpdate(Bo model) throws ServiceException;
    
    public void update(Bo model) throws ServiceException;
    
    public void merge(Bo model) throws ServiceException;

    public void delete(PK id) throws ServiceException;

    public void deleteObject(Bo model) throws ServiceException;

    public Bo get(PK id) throws ServiceException;
    
    public int countAll() throws ServiceException;
    
    public List<Bo> listAll() throws ServiceException;
    
    public Page<Bo> listAll(Bo model, int pn, int pageSize) throws ServiceException;
    
    public Bo find(Bo model) throws ServiceException;
    
    public Page<Bo> listAll(int pn) throws ServiceException;
    /**
     * 分页查询
     * @param pn 页数 从0开始
     * @param pageSize 分页大小
     * @return
     */
    public Page<Bo> listAll(int pn, int pageSize) throws ServiceException;
    
    public Page<Bo> pre(PK pk, int pn, int pageSize) throws ServiceException;
    
    public Page<Bo> next(PK pk, int pn, int pageSize) throws ServiceException;
    
    public Page<Bo> pre(PK pk, int pn) throws ServiceException;
    
    public Page<Bo> next(PK pk, int pn) throws ServiceException;
    //分页查询，返回前端可用的model
    public GridModel<Vo> list(Bo form, int pn, int pageSize, ItemModelHelper<Bo, Vo> helper) throws ServiceException;
}