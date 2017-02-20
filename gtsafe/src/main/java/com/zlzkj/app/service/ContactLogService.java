package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

public interface ContactLogService {
	/**
	 * 添加触点项和日志的关联
	 * @param id
	 * @param flag
	 * @param attr
	 * @param time 
	 */
	public void add(String id, int flag, String attr);
	/**
	 * 获取日志所关联的触点信息
	 * @param logId
	 * @return
	 */
	public List<Map<String, Object>> getApiContactLog(int logId);
	/**
	 * 根据where获取value
	 * @param where
	 * @return
	 */
	public Map<String, Object> getValue(String where);
	/**
	 * 删除日志和触点项关联
	 * @param logId
	 */
	public void del(int logId);
	/**
	 * 获取value
	 * @param logId
	 * @return
	 */
	public List<Map<String, Object>> getValueByLogId(int logId);
	public List<Map<String, Object>> getApiOldContactLog(List<Map<String, Object>> logList, String appearNumber);
	public List<Map<String, Object>> getContactLog(Map<String, Object> where, String group);
	
	public List<Map<String, Object>> contactLog(int logId) throws Exception;
	
	

}
