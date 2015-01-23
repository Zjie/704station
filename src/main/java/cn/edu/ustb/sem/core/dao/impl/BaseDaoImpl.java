package cn.edu.ustb.sem.core.dao.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import cn.edu.ustb.sem.core.dao.BaseDao;
import cn.edu.ustb.sem.core.pagination.PageUtil;

public abstract class BaseDaoImpl<M extends java.io.Serializable, PK extends java.io.Serializable> implements BaseDao<M, PK> {
	protected final Log logger = LogFactory.getLog(getClass());
	private final Class<M> entityClass;
    private final String HQL_LIST_ALL;
    private final String HQL_COUNT_ALL;
    private final String HQL_OPTIMIZE_PRE_LIST_ALL;
    private final String HQL_OPTIMIZE_NEXT_LIST_ALL;
    protected String pkName = null;
    protected String tableName = null;
    @SuppressWarnings("unchecked")
	public BaseDaoImpl() {
        this.entityClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Field[] fields = this.entityClass.getDeclaredFields();
        for(Field f : fields) {
            if(f.isAnnotationPresent(Id.class)) {
                this.pkName = f.getName();
            }
        }
        tableName = this.entityClass.getSimpleName();
        Assert.notNull(pkName);
        Assert.notNull(tableName);
        HQL_LIST_ALL = "from " + tableName + " order by " + pkName + " desc";
        HQL_OPTIMIZE_PRE_LIST_ALL = "from " + tableName + " where " + pkName + " > ? order by " + pkName + " desc";
        HQL_OPTIMIZE_NEXT_LIST_ALL = "from " + tableName + " where " + pkName + " < ? order by " + pkName + " desc";
        HQL_COUNT_ALL = " select count(*) from " + tableName;
    }
	@Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
	
    public Session getSession() {
        //事务必须是开启的(Required)，否则获取不到
        return sessionFactory.getCurrentSession();
    }
    @SuppressWarnings("unchecked")
	@Override
    public PK save(M model) {
        return (PK) getSession().save(model);
    }
    @Override
    public void saveOrUpdate(M model) {
        getSession().saveOrUpdate(model);
    }
    @Override
    public void update(M model) {
        getSession().update(model);
    }
    @Override
    public void merge(M model) {
        getSession().merge(model);
    }
    @Override
    public void delete(PK id) {
        getSession().delete(this.get(id));
    }
    @Override
    public void deleteObject(M model) {
        String className = model.getClass().getSimpleName();
    	String asName = className.toLowerCase();
    	
    	MyQuery myquery = new MyQuery();
    	myquery.alias = asName;
    	myquery.delete = "delete " + className + " as " + asName;
    
    	List<Object> params = new ArrayList<Object>();
    	try {
    		//生成where语句
			generateWhereClause(myquery, model, asName, params);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			logger.warn(e + "");
		}
    	//执行删除
    	this.execute(myquery.getDelete(), myquery.params.toArray());
    }
    
    @Override
    public boolean exists(PK id) {
        return get(id) != null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public M get(PK id) {
        return (M) getSession().get(this.entityClass, id);
    }

    @Override
    public int countAll() {
        Long total = aggregate(HQL_COUNT_ALL);
        return total.intValue();
    }
    

    @Override
    public List<M> listAll(int pn, int pageSize) {
    	return list(HQL_LIST_ALL, pn, pageSize);
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<M> list(final String hql, final int pn, final int pageSize, final Object... paramlist) {
        Query query = getSession().createQuery(hql);
        setParameters(query, paramlist);
        if (pn > -1 && pageSize > -1) {
            query.setMaxResults(pageSize);
            int start = PageUtil.getPageStart(pn, pageSize);
            if (start != 0) {
                query.setFirstResult(start);
            }
        }
        if (pn < 0) {
            query.setFirstResult(0);
        }
        List<M> results = query.list();
        if (results == null) return new ArrayList<M>();
        return results;
    }
    @SuppressWarnings("unchecked")
	protected <T> T aggregate(final String hql, final Object... paramlist) {
        Query query = getSession().createQuery(hql);
        setParameters(query, paramlist);
        return (T) query.uniqueResult();
    }
    @Override
    public List<M> listAll() {
        return list(HQL_LIST_ALL, -1, -1);
    }
    @Override
    public List<M> pre(PK pk, int pn, int pageSize) {
        if(pk == null) {
            return list(HQL_LIST_ALL, pn, pageSize);
        }
        //倒序，重排
        List<M> result = list(HQL_OPTIMIZE_PRE_LIST_ALL, 1, pageSize, pk);
        Collections.reverse(result);
        return result;
    }
    @Override
    public List<M> next(PK pk, int pn, int pageSize) {
        if(pk == null) {
            return list(HQL_LIST_ALL, pn, pageSize);
        }
        return list(HQL_OPTIMIZE_NEXT_LIST_ALL, 1, pageSize, pk);
    }
    @Override
    public void flush() {
        getSession().flush();
    }
    @Override
    public List<M> listAll(M model, int pn, int pageSize) {
    	if (model == null) {
    		return this.listAll(pn, pageSize);
    	}
    	try {
	    	String className = model.getClass().getSimpleName();
	    	String asName = className.toLowerCase();
	    	
	    	MyQuery myquery = new MyQuery();
	    	myquery.alias = asName;
	    	myquery.select = "from " + className + " as " + asName;
	    
	    	List<Object> params = new ArrayList<Object>();
	    	generateWhereClause(myquery, model, asName, params);
	    	return this.list(myquery.getQuery(), pn, pageSize, myquery.params.toArray());
    	} catch (IllegalArgumentException e) {
    		logger.warn(e + "");
		} catch (NoSuchFieldException e) {
			logger.warn(e + "");
		} catch (SecurityException e) {
			logger.warn(e + "");
		} catch (IllegalAccessException e) {
			logger.warn(e + "");
		}
    	return null;
    }
    @Override
    public void clear() {
        getSession().clear();
    }
    protected void setParameters(Query query, final Object... paramlist) {
        if (paramlist != null) {
            for (int i = 0; i < paramlist.length; i++) {
            	if (paramlist[i] == null) {
            		continue;
            	}
                if (paramlist[i] instanceof Date) {
//                    query.setTimestamp(i, (Date)paramlist[i]);
                    query.setDate(i, (Date)paramlist[i]);
                } else {
                	query.setParameter(i, paramlist[i]);
                }
            }
        }
    }
    /**
     * 根据一个model生成where语句，反射
     * @param myquery
     * @param model
     * @param asName
     * @param params
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    private void generateWhereClause(MyQuery myquery, Object model, String asName, List<Object> params) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
    	if (model.getClass().getAnnotation(Table.class) == null) {
    		//判断如果不是一个和表映射的实体，抛出异常
    		return;
    	}
    	Field[] fields = model.getClass().getDeclaredFields();
    	for (Field field : fields) {
    		//如果字段为空，跳过该条件
    		field.setAccessible(true);
    		Object pa = field.get(model);
    		if (pa == null) {
    			continue;
    		}
    		
            Annotation[] ans = field.getAnnotations();
            //目前只做一层的连接操作递归，对于多层次递归暂不考虑
            for (Annotation an : ans) {
            	if (an instanceof Column) {
            		String colName = field.getName();
					myquery.where += " and " + asName + "." + colName + " = ? ";
					myquery.params.add(pa);
            	} else if (an instanceof ManyToOne) {
            		//该字段是many端，目前多对一的情况，都是别的表的id作为外键
            		String colName = field.getName();
            		generateWhereClause(myquery, pa, asName + "." + colName, params);
            	} else if (an instanceof OneToMany) {
            		//该字段是one端
            		String colName = field.getName();
            		@SuppressWarnings("rawtypes")
					Set midParams = (Set)pa;
            		for (Object obj : midParams) {
            			String className = obj.getClass().getSimpleName();
            			generateWhereClause(myquery, obj, className.toLowerCase(), params);
            			myquery.join += " inner join fetch " + asName + "." + colName + " as " + className.toLowerCase() + " ";
            			break;
            		}
            	} else if (an instanceof OneToOne) {
            		String colName = field.getName();
            		generateWhereClause(myquery, pa, asName + "." + colName, params);
            	}
            }
    	}
    }
    private static class MyQuery {
    	String count = "";
		@SuppressWarnings("unused")
		String alias = "";
    	String select = "";
    	String join = "";
    	String delete = "";
    	String where = " where 1 = 1";
    	List<Object> params = new ArrayList<Object>();
    	String getQuery() {
    		return select + join + where;
    	}
    	String getDelete() {
    		return delete + where;
    	}
    	String getCount() {
    		return count + where;
    	}
    }
    public M find(M model) {
    	List<M> result = this.listAll(model, -1, -1);
    	if (result == null || result.size() == 0) {
 		   return null;
 	   }
 	   return result.get(0);
    }
    
    public M find(String hql, final Object... params) {
    	List<M> res = this.list(hql, -1, -1, params);
    	if (res == null || res.size() == 0) return null;
    	return res.get(0);
    }
    
    public int execute(String hql, final Object... params) {
    	Query query = this.getSession().createQuery(hql);
    	this.setParameters(query, params);
    	return query.executeUpdate();
    }
    @Override
    public int count(String hql, final Object... params) {
    	Query query = this.getSession().createQuery(hql);
    	this.setParameters(query, params);
    	Long num = (Long)query.uniqueResult();
    	return num.intValue();
    }
    
    public int count(M model) {
    	if (model == null) {
    		return this.countAll();
    	}
    	try {
	    	String className = model.getClass().getSimpleName();
	    	String asName = className.toLowerCase();
	    	
	    	MyQuery myquery = new MyQuery();
	    	myquery.alias = asName;
	    	myquery.count = "select count(*) from " + className + " as " + asName;
	    
	    	List<Object> params = new ArrayList<Object>();
	    	generateWhereClause(myquery, model, asName, params);
	    	return this.count(myquery.getCount(), myquery.params.toArray());
    	} catch (IllegalArgumentException e) {
    		logger.warn(e + "");
		} catch (NoSuchFieldException e) {
			logger.warn(e + "");
		} catch (SecurityException e) {
			logger.warn(e + "");
		} catch (IllegalAccessException e) {
			logger.warn(e + "");
		}
    	return 0;
    }
    @Override
    public Collection<M> get(Collection<PK> ids) {
    	Collection<M> data = new ArrayList<M>();
    	if (ids == null || ids.isEmpty()) return data;
    	Iterator<PK> idIter = ids.iterator();
    	PK id = idIter.next();
    	if (id instanceof Integer) {
    		StringBuilder sb = new StringBuilder();
    		sb.append(id).append(",");
    		while (idIter.hasNext()) {
    			Integer next = (Integer) idIter.next();
    			sb.append(next).append(",");
    		}
    		String idStr = sb.toString();
    		idStr = idStr.substring(0, idStr.length() - 1);
    		String hql = "from " + this.tableName + " as t where t.id in (" + idStr + ")";
    		data.addAll(this.list(hql, -1, -1));
    	} else {
    		data.add(this.get(id));
    		while (idIter.hasNext()) {
    			data.add(this.get(idIter.next()));
    		}
    	}
    	return data;
    }
    @Override
    public void deleteAll(Collection<PK> ids) {
    	if (ids == null || ids.isEmpty()) return;
    	Iterator<PK> idIter = ids.iterator();
    	PK id = idIter.next();
    	if (id instanceof Integer) {
    		StringBuilder sb = new StringBuilder();
    		sb.append(id).append(",");
    		while (idIter.hasNext()) {
    			Integer next = (Integer) idIter.next();
    			sb.append(next).append(",");
    		}
    		String idStr = sb.toString();
    		idStr = idStr.substring(0, idStr.length() - 1);
    		String hql = "delete from " + this.tableName + " as t where t.id in (" + idStr + ")";
    		this.execute(hql);
    	} else {
    		this.delete(id);
    		while (idIter.hasNext()) {
    			this.delete(idIter.next());
    		}
    	}
    }

}