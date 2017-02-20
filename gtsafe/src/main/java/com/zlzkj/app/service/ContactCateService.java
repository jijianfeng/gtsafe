package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.ContactCate;

public interface ContactCateService {
	
	/**
	 * 触点分类列表
	 * @return
	 */
	public List<Map<String, Object>> getCateList();
	/**
	 * 触点分类目录 
	 * @param level
	 * @return
	 */
	public List<Map<String, Object>> getAllCateList(Integer level);
	/**
	 * 添加触点分类
	 * @param contactCate
	 * @return
	 */
	public Integer addCate(ContactCate contactCate);
	/**
	 * 更新触点分类
	 * @param contactCate
	 * @param upNumber 
	 * @return
	 */
	public Integer update(ContactCate contactCate);
	
	/**
	 * 删除触点分类
	 * @param id
	 * @return
	 */
	public Integer del(int id);
	/**
	 * 添加触点项时增加触点数
	 * @param id
	 * @param count
	 */
	public void addCount(Integer id, Integer count);

	/**
	 * 删除触点项时减少触点数
	 * @param oldId 
	 * @param count
	 */
	public void delCount(Integer id, Integer count);
	
	/**
	 * 获取子节点列表
	 * @param where
	 * @return
	 */
	public List<Map<String, Object>> getCate(String where);
	public ContactCate findById(int id);
	public int topCate(int cateId);
	public int endCate(int cateId);
	public Map<String, Object> getCateByVersion(int cateId, int addTime);
	

}
