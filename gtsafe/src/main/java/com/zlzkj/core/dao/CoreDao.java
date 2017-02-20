package com.zlzkj.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 通用dao
 * @author Simon
 *
 * @param <T>
 */
public interface CoreDao<T> {  
	  
	/**
	 * 保存实例
	 * @param entity
	 * @return 新增的流水号
	 */
    public Serializable save(T entity);
  
    /**
     * 更新实例<br/>
     * 注意：实体的属性必须都要使用包装类，这样默认值才会统一为null<br/>
     * 更新时会忽略entity中的null值
     * @param entity
     * @return 数据库执行后影响的行数，正常返回1
     */
    public int update(Serializable id,T entity);
    
    /**
     * 根据id删除
     * @param id
     */
    public void delete(Serializable id);
    /**
     * 批量删除
     * @param sql
     */
    public void delete(String sql);
    
    /**
     * 根据id获取
     * @param id
     * @return 实例 or null
     */
    public T getById(Serializable id);
    
    /**
     * 查询所有的实例记录
     * @return 所有的实例记录
     */
    public List<T> findAll();
    
    /**
     * 根据单个属性值查找
     * @param propertyName
     * @param value
     * @return 实例list
     */
    public List<T> findByProperty(String propertyName, Object value);
    
    /**
     * 根据多个属性查找
     * @param entityExample
     * @return 实例list
     */
    public List<T> findByExample(T entityExample);
    
    
    /**
     * 根据hql查询
     * @param hql
     * @param params 对应hql语句中的问号的值
     * @return 实例list
     */
    public List<T> findByHQL(String hql, Object... params);
    
    /**
     * 根据hql查询
     * @param sql
     * @param params 对应hql语句中的问号的值
     * @return 实例 or null
     */
	public T getByHQL(String hql, Object... params);
    
    /**
     * 根据完整的hql查询,返回一个值
     * @param hql
     * @param params 对应hql语句中的问号的值
     * @return 单个属性值
     */
    public String getValueByHQL(String hql, Object... params);
    
    //-------------SQL-START--------------------
    /**
     * 根据sql查询，返回结果集
     * @param sql
     * @param params 对应hql语句中的问号的值
     * @return
     */
    public List<Map<String, Object>> findBySQL(String sql, Object... params);
    
    /**
     * 根据sql查询，返回结果集
     * @param sql
     * @param params 对应hql语句中的问号的值
     * @return List<Entity>
     */
    public List<T> findBySQLToEntity(String sql, Object... params);
    
    /**
     * 根据sql查询，返回单个实体类信息
     * @param sql
     * @param params 对应hql语句中的问号的值
     * @return map or null
     */
	public Map<String, Object> getBySQL(String sql, Object... params);
    
	/**
     * 根据sql查询，返回结果集
     * @param sql
     * @param params 对应hql语句中的问号的值
     * @return Entity or null
     */
    public T getBySQLToEntity(String sql, Object... params);
	
    /**
     * 根据sql查询，返回一个值
     * @param sql
     * @param params 对应hql语句中的问号的值
     * @return 单个属性值
     */
    public String getValueBySQL(String sql, Object... params);
    
    public int setIncDec(String where, String field, Integer step, String exp);
    //-------------SQL-END--------------------

    
} 
