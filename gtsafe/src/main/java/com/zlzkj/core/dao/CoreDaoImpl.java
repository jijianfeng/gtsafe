package com.zlzkj.core.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.zlzkj.core.utils.CoreUtils;

/** 
 * 定义DAO的通用操作的实现 
 *  
 * @author Simon 
 */  
@SuppressWarnings("unchecked")  
public class CoreDaoImpl<T> implements CoreDao<T> {  
  
    private Class<T> clazz;
    private String clazzName;
    
    /** 
     * 通过构造方法指定DAO的具体实现类 
     */  
    public CoreDaoImpl() {  
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();  
        clazz = (Class<T>) type.getActualTypeArguments()[0];
        clazzName = this.clazz.getName();
        clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1);
        //System.out.println("DAO的真实实现类是：" + clazz);
    }
  
    /** 
     * 向DAO层注入SessionFactory 
     */  
    @Autowired  
    private SessionFactory sessionFactory;
  
    /** 
     * 获取当前工作的Session 
     */
    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();  
    }  
  
	@Override
    public Serializable save(T entity) {
       return this.getSession().save(entity);
    }  
  
	@Override
    public int update(Serializable id,T entity) {
		Map<String,Object> newData = CoreUtils.convertBean(entity);
		String hql = "update "+this.clazzName+" set ";
		String setString = "";
		Set<String> keysSet = newData.keySet();
		Iterator<String> iterator = keysSet.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();//key
			Object value = newData.get(key);//value
			if(value!=null && !value.equals("")){
				setString += key + "=\'" + value.toString() + "\'" + ",";
			}
		}
		
		setString = setString.substring(0, setString.length()-1); //删除最后的逗号
		hql += setString + " where id=\'" + id + "\'";
		Query query = this.getSession().createQuery(hql); 
		return query.executeUpdate();
    }
	
	@Override
    public void delete(Serializable id) {  
        this.getSession().delete(this.getById(id));
    }  
	@Override
	public void delete(String sql){
		Query query = this.getSession().createSQLQuery(sql); 
		query.executeUpdate();
	}
  
	@Override
    public T getById(Serializable id) {
		Object obj = this.getSession().get(this.clazz, id);
		if(obj==null){
			return null;
		}
        return (T) obj;
    }
    
	@Override
    public List<T> findByProperty(String propertyName, Object value) {
    	String hql = "from "+this.clazzName+" as model where model."
           						+ propertyName + "= ?";
        Query query = this.getSession().createQuery(hql);
        query.setParameter(0, value);
  		return query.list();
  	}
    
	@Override
    public List<T> findByExample(T entityExample) {
    	Criteria c=this.getSession().createCriteria(this.clazz);
		c.add(Example.create(entityExample));
		return c.list();
	}
	
	@Override
	public List<T> findAll() {
		Query query = this.getSession().createQuery("from "+this.clazzName);
		return query.list();
	}
    
	@Override
    public List<T> findByHQL(String hql, Object... params) {  
        Query query = this.getSession().createQuery(hql);
        if(params.length>0){
	        for (int i = 0; i < params.length; i++) {  
	            query.setParameter(i, params[i]);  
	        }  
        }
        return query.list();
    }

	@Override
	public T getByHQL(String hql, Object... params) {
		Query query = this.getSession().createQuery(hql);
		if(params.length>0){
	        for (int i = 0; i < params.length; i++) {  
	            query.setParameter(i, params[i]);  
	        }  
        }
		List<T> entityList = query.list();
		if(entityList.size()>0){
			return entityList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public String getValueByHQL(String hql, Object... params) {
		Query query = this.getSession().createQuery(hql);
		if(params.length>0){
	        for (int i = 0; i < params.length; i++) {  
	            query.setParameter(i, params[i]);  
	        }  
        }
		return query.uniqueResult().toString();
	}
	
	//---------------SQL-START------------------------
	@Override
	public List<Map<String, Object>> findBySQL(String sql,Object... params) {
		//查出来的结果，映射到一个list map中，跟最普通的二维数据一
		System.out.println("》》》》》》》》》》》》》sql bug"+sql);
		Query query = this.getSession().createSQLQuery(sql);
		if(params.length>0){
        	for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}
	
	@Override
	public List<T> findBySQLToEntity(String sql, Object... params) {
		Query query = this.getSession().createSQLQuery(sql);
		if(params.length>0){
        	for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
		query.setResultTransformer(Transformers.aliasToBean(this.clazz));
		return query.list();
	}

	
	@Override
	public Map<String, Object> getBySQL(String sql, Object... params) {
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if(params.length>0){
        	for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
		List<Map<String, Object>> entityList = query.list();
		if(entityList.size()>0){
			return entityList.get(0);
		}else{
			return null;
		}
	}
	

	@Override
	public T getBySQLToEntity(String sql, Object... params) {
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(this.clazz));
		if(params.length>0){
        	for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
		List<T> entityList = query.list();
		if(entityList.size()>0){
			return entityList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public String getValueBySQL(String sql, Object... params) {
		Query query = this.getSession().createSQLQuery(sql);
		if(params.length>0){
        	for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
		return query.uniqueResult().toString();
	}
	
	@Override
	public int setIncDec(String where, String field, Integer step, String exp) {
		String hql = "update "+this.clazz.getAnnotation(Table.class).name()+" set ";
		hql += field+"="+field+exp+step;
		hql += " where "+where;
		
		Query query = this.getSession().createSQLQuery(hql); 
		return query.executeUpdate();
	}
	//--------------SQL-END-----------------------
	
	

}  