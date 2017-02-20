package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Field;
import com.zlzkj.app.model.User;

public interface UserService {
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */	
	public Integer add(User user);
	
	/**
	 * 删除用户
	 * @param id
	 */
	public void del(int id);
	
	/**
	 * 更新用户信息
	 * @param user
	 */
	public int update(User user);
	
	public User findByAccount(String account);
	
	public User findById(Integer id);
	/**
	 * 用户列表
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getUserRoleAll(int page, int rows);
	/**
	 * 包括工地后的用户列表
	 * @param page
	 * @param rows
	 * @return
	 */
	public Map<String, Object> getUserRoleFieldAll(int page, int rows);
	
	/**
	 * 获取该用户下的所有权限节点
	 * @param userName
	 * @return
	 */
	public List<Map<String, Object>> getRoleAll(String userName);
	public int updatePass(User user);
}
