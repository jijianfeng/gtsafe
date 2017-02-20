package com.zlzkj.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Query;
import com.zlzkj.core.utils.IniEditor;

public interface QueryService {
	/**
	 * 获取所有的配置文件名
	 * @return
	 * @throws IOException
	 */
	public List<String> getAllContents() throws IOException;
	/**
	 * 获取全部的字段名
	 * @param content 配置文件名
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getAllFields(String content) throws IOException;
	/**
	 * 获取比较符
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getCompare() throws IOException;
	/**
	 * 获取字段对应的条件
	 * @param content
	 * @param field
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getValue(String content, String field) throws IOException;
	/**
	 * 组装select
	 * @param content
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public String buildSelect(String content, String[] fieldNames) throws IOException;
	/**
	 * 获取查询条件列表
	 * @param content
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getCondition(String content, String[] fieldNames) throws IOException;
	/**
	 * 组装where
	 * @param field
	 * @param compare
	 * @param compareVal
	 * @param content 
	 * @return
	 * @throws IOException
	 */
	public String buildWhere(String field, String compare, String compareVal, String content) throws IOException;
	/**
	 * 运行sql
	 * @param sql
	 * @param content
	 * @param fieldNames
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> runSql(String sql,String content, String[] fieldNames) throws IOException;
	/**
	 * 添加自定义查询
	 * @param select
	 * @param where
	 * @param content
	 * @param title
	 * @param fieldName
	 * @return
	 */
	public int add(String select, String where, String content, String title, String fieldName);
	/**
	 * 获取自定义查询列表
	 * @param whereMap 
	 * @return
	 */
	public List<Map<String, Object>> getQueryList(Map<String, Object> whereMap);
	/**
	 * 获取显示这个查询里所有需要填写的条件
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, Object>> getInputList(int id) throws IOException;
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public int del(int id);
	public Query getQueryById(int id);
	/**
	 * 运行带@@的sql
	 * @param id
	 * @param list
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> runCondition(int id, List<Map<String, Object>> list) throws IOException;
	/**
	 * 运行保存好的sql
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> run(int id) throws IOException;
	/**
	 * 更新查询项
	 * @param select
	 * @param where
	 * @param content
	 * @param title
	 * @param fieldName
	 * @param id
	 * @return
	 */
	public int update(String select, String where, String content, String title, String fieldName, Integer id);
	/**
	 * 导出查询项
	 * @param data
	 * @param title
	 * @return
	 */
	public String exportQuery(String data, String title);
	/**
	 * 查找查询项
	 * @param value
	 * @return
	 */
	public List<Map<String, Object>> findQuery(String value);
	/**
	 * 获取数据表格表头
	 * @param fieldNames
	 * @throws IOException
	 */
	public List<Map<String, Object>> getTitle(String[] fieldNames) throws IOException;
	public IniEditor getIni(String content) throws IOException;
	


}
