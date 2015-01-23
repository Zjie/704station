package cn.edu.ustb.sem.core.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import cn.edu.ustb.sem.core.exception.ServiceException;


public interface BaseDao<M extends java.io.Serializable, PK extends java.io.Serializable> {
	/**
	 * 保存一个对象
	 * @param model
	 * @return
	 * @throws ServiceException TODO
	 */
	public PK save(M model);
	/**
	 * 保存或更新
	 * @param model
	 */
	public void saveOrUpdate(M model);
	/**
	 * 更新
	 * @param model
	 */
    public void update(M model);
    public void merge(M model);
    /**
     * 删除一个对象
     * @param id
     */
    public void delete(PK id);
    public void deleteAll(Collection<PK> id);
    /**
     * 删除一个和Model对象一致的对象
     * @param model
     */
    public void deleteObject(M model);
    /**
     * 基于id查找
     * @param id
     * @return
     */
    public M get(PK id);
    public Collection<M> get(Collection<PK> ids);
    boolean exists(PK id);
    /**
     * 按照主键升序分页查找
     * @param pn 从第0页开始
     * @param pageSize 分页大小
     * @return
     */
    public List<M> listAll(int pn, int pageSize);
    public List<M> listAll(M model, int pn, int pageSize);
    
    public List<M> list(final String hql, final int pn, final int pageSize, final Object... paramlist);
    public List<M> listAll();
    public List<M> pre(PK pk, int pn, int pageSize);
    public List<M> next(PK pk, int pn, int pageSize);
    public int countAll();
    public int count(String hql, final Object... paramlist);
	public void flush();
	public void clear();
	
	public M find(M model);
	public M find(String hql, final Object... params);
	
	public int execute(String hql, final Object... params);
	
	public Session getSession();
	
	public int count(M model);
}