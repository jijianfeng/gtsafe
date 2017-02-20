package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Template;

public interface TemplateService {
	/**
	 * 获取模板信息
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getTemplateList(int page, int rows);
	/**
	 * 添加模板
	 * @param template
	 * @return
	 */
	public Integer add(Template template);
	/**
	 * 更新模板
	 * @param template
	 * @return
	 */
	public Integer update(Template template);
	/**
	 * 删除模板
	 * @param id
	 * @return 
	 */
	public int del(int id);
	
	/**
	 * 获取模板名称
	 * @return
	 */
	public List<Map<String, Object>> getTemplateNameList();
	
	public Template getTemplateById(int id);
	
	public List<Map<String, Object>> template(int templateId) throws Exception;
	
	public List<Map<String, Object>> templateApi(int templateId) throws Exception;
	
	
	
	

}
