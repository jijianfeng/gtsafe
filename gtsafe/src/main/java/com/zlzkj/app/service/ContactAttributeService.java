package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.ContactAttribute;

public interface ContactAttributeService {
	/**
	 * 添加属性关联
	 * @param contactAttribute
	 * @param attribute
	 * @param id
	 */
	public void add(int attributeId, int id);	
	/**
	 * 删除属性关联
	 * @param id
	 */
	
	/**
	 * 获取指定触点下的所有属性
	 * @param contactId 
	 * @return
	 */
	public List<Map<String, Object>> getContactAttribute(Map<String, Object > where, Integer page,Integer rows);
	/**
	 * 获取触点项对应的属性数
	 * @param id
	 * @return
	 */
	public int getAttrCountById(int id);

	public ContactAttribute getByAttrId(int attributeId);



	
	
	

}
