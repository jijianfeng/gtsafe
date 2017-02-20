package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.DocsCate;

public interface DocsCateService {
	/**
	 * 获取分类列表
	 * @return
	 */
	public List<Map<String, Object>> getCateList(int level);
	/**
	 * 增加分类
	 * @param docsCate
	 * @return
	 */
	public int add(DocsCate docsCate);
	/**
	 * 获取所有分类列表项
	 * @return
	 */
	public List<Map<String, Object>> getCateAll();
	/**
	 * 更新修改
	 * @param docsCate
	 * @return
	 */
	public int update(DocsCate docsCate);
	/**
	 * 判断是否有子分类
	 * @param id
	 * @return
	 */
	public Boolean isLibEmpty(int id);
	/**
	 * 删除分类
	 * @param id
	 */
	public void del(int id);
	/**
	 * 根据id查找信息
	 * @param id
	 * @return
	 */
	public DocsCate findById(Integer id);
	/**
	 * 分类下文档数++
	 * @param id
	 */
	public void countPlus(int id);
	/**
	 * 分类下文档数--
	 * @param id
	 */
	public void countMinus(int id);
	public List<Map<String, Object>> getCate(Map<String, Object> where);
	public String getCateIds(Integer cateId);

}
