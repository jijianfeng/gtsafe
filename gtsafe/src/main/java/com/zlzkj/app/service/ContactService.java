package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Contact;

public interface ContactService {
	
	/**
	 * 获取触点项列表
	 * @param page
	 * @param rows
	 * @param whereMap
	 * @return
	 */
	public Map<String, Object> getContactList(int page, int rows,String whereMap);
	
	
	/**
	 * 搜索触点项列表
	 * @param page
	 * @param rows
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSearchList(int page, int rows, int id);
	
	/**
	 * 添加触点	
	 * @param contact
	 * @param upNumber 
	 * @return
	 */
	public Integer add(Contact contact);
	/**
	 * 更新触点
	 * @param upNumber 
	 * @param contact
	 * @return
	 */
	public Integer update(Contact Contact);
	/**
	 * 删除触点
	 * @param id
	 */
	public void del(int id);
	/**
	 * 获取触点项,通过id
	 * @param id
	 * @return
	 */
	public Contact findById(int id);
	/**
	 * 获取触点项信息
	 * @param where
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<Map<String , Object>> getContact(String where,Integer page, Integer rows);


	public String contactImport(String url) throws Exception;


	public List<Map<String, Object>> getAllContacts(String where);


	public Contact getContactByid(int id);


	public List<Map<String, Object>> getContactListByList(List<Map<String, Object>> list, int time) throws Exception;

	public List<Map<String, Object>> getApiContactListByList(List<Map<String, Object>> list, int time) throws Exception;
	
	public int topContact(int contactId);

//	public int endContact(int contactId);

	public List<Map<String, Object>> haveContact(Map<String, Object> where);


	public List<Map<String, Object>> getContactByList(String where, String group);


	

}
