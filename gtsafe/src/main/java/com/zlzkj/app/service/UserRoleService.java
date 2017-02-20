package com.zlzkj.app.service;

import com.zlzkj.app.model.UserRole;

public interface UserRoleService {
	/**
	 * 新增用户角色关联
	 * @param userRole
	 * @return
	 */
	public Integer add(UserRole userRole);
	/**
	 * 更新用户角色关联
	 * @param userRole
	 * @return
	 */
	public Integer update(UserRole userRole);
	/**
	 * 根据用户ID找到用户角色ID
	 * @param userId
	 * @return
	 */
	public int findIdByUserId(int userId);
	/**
	 * 判断该角色下是否存在用户
	 * @param roleId
	 * @return
	 */
	public Boolean isUserEmpty(int roleId);
	
	public void del(int id);
	public int findRoleIdByUserId(int userId);

}
