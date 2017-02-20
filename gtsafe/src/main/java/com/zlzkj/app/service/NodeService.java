package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Node;

public interface NodeService {
	/**
	 * 获取权限节点(添加权限节点上级节点)
	 * @return
	 */
	public List<Map<String, Object>> getNodeList();
	/**
	 * 添加权限节点
	 * @param node
	 * @return
	 */
	public int add(Node node);
	/**
	 * 获取角色关联节点
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> getRoleNodeList(int roleId);
	public List<Map<String, Object>> leftNodeList(int roleId);

}
