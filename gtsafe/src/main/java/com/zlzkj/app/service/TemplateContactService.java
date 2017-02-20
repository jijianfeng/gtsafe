package com.zlzkj.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;



public interface TemplateContactService {
	/**
	 * 添加模板触点项关联
	 * @param idStrings
	 * @param templateId
	 */
	public void add(String[] idStrings, Integer templateId);
	/**
	 * 删除模板触点项关联
	 * @param id
	 */
	public void del(int id);
	/**
	 * 获取全部分类和触点项
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getTempContactList(Integer templateId);
	/**
	 * 获取日志的节点信息
	 * @param id
	 * @param logId
	 * @return
	 * @throws IOException 
	 */
	public List<Map<String, Object>> getTemplateContact(Map<String, Object> where,
			String group) throws IOException;
	public void update(int oldId, int newId);



}
