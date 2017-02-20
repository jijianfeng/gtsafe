package com.zlzkj.app.service;

import java.util.Map;

import com.zlzkj.app.model.RoleNode;

public interface RoleNodeService {
	
	public int add(RoleNode roleNode);
	public int add(int roleId,String[] nodeId);
	
	public Map<String, Object> findBy(int roleId,int nodeId);

}
