package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.ContactCate;
import com.zlzkj.app.model.Field;

public interface FieldService {
	
	/**
	 * 工地列表
	 * @return
	 */
	public List<Map<String, Object>> getFieldList();
	/**
	 * 添加工地
	 * @param contactCate
	 * @return
	 */
	public Integer addField(Field field);
	/**
	 * 删除项目或者工地
	 * @param id
	 * @return
	 */
	public Integer del(int id);
	/**
	 * 获取项目和工地目录 
	 * @param level
	 * @return
	 */
	public List<Map<String, Object>> getAllFieldList(Integer level);
	/**
	 * 更新触点分类
	 * @param contactCate
	 * @param upNumber 
	 * @return
	 */
	public Integer update(Field Field);
	
	public List<Map<String, Object>> haveField(Map<String, Object> where);
	
	/**
	 * 获取子节点列表
	 * @param where
	 * @return
	 */
	public List<Map<String, Object>> getField(String where);
	public Field findById(int id);
	public int topField(int fieldId);
	public int endField(int fieldId);
}
