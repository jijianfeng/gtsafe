package com.zlzkj.app.service;

import java.util.Map;

import com.zlzkj.app.model.Docs;

public interface DocsService {
	/**
	 * 增加文档
	 * @param docs
	 * @return
	 */
	public int add(Docs docs);
	/**
	 * 更新修改
	 * @param docs
	 * @return
	 */
	public int update(Docs docs);
	/**
	 * 删除文档
	 * @param id
	 */
	public void del(int id);
	/**
	 * 获取列表数据
	 * @param where 
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getDocsList(Map<String, Object> where, Map<String, String> pageParams);
	/**
	 * 根据id获取文档信息
	 * @param id
	 * @return
	 */
	public Docs findById(Integer id);

}
