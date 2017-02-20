package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Attribute;

public interface AttributeService {
	/**
	 * 获取属性列表
	 * @param page
	 * @param rows
	 * @param id 
	 * @return
	 */
	public Map<String, Object> getAttrList(Integer page, Integer rows, int id);
	/**
	 * 添加属性
	 * @param attribute
	 * @return
	 */
	public Integer addAttr(Attribute attribute);
	/**
	 * 删除属性
	 * @param id
	 * @return
	 */
	public int del(int id);
	
	/**
	 * 获取属性的分值
	 * @param attrId
	 * @return
	 */
	public int getScore(Integer attrId);
	
	
	/**
	 * 通过id查找返回实例
	 * @param id
	 * @return
	 */
	public Attribute findById(int id);
	
	public int update(Attribute attribute);
	
//	public int getAttrCountById(int id);
//	public List<Map<String, Object>> getAttribute(Map<String, Object> where,
//			Integer page, Integer rows);
	public List<Map<String, Object>> getAttributeByVersion(int cateId, int addTime);
	

}
