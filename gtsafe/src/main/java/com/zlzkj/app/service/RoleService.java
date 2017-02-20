package com.zlzkj.app.service;
import java.util.List;
import java.util.Map;
import com.zlzkj.app.model.Role;

public interface RoleService {
	/**
	 * 添加角色
	 * @param role
	 * @return
	 */	
	public Integer add(Role role);
	
	/**
	 * 删除角色
	 * @param id
	 */
	public void del(int id);
	
	/**
	 * 更新角色信息
	 * @param user
	 */
	public int update(Role role);
	
	/**
	 * 角色列表
	 * @return
	 */
	public Map<String, Object> getRoleAll(int page,int rows);
	
	/**
	 * 选择角色列表
	 * @return
	 */
	public List<Map<String, Object>> getRoleAll();

	public Role findById(int id);

}
