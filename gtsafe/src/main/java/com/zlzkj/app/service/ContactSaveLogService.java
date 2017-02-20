package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

public interface ContactSaveLogService {
	/**
	 * 添加今天的保存内容
	 * @param id
	 * @param flag
	 * @param attrId
	 */
	public void add(String id, int flag, String attrId,int addTime);
	/**
	 * 获取value
	 * @param where
	 * @return
	 */
	public Map<String, Object> getValue(String where);
	/**
	 * 获取是否有保存内容
	 * @return
	 */
	public Map<String, Object> findSaveNow();
	/**
	 * 删除保存内容
	 */
	public void delNow();
	public List<Map<String, Object>> getContactSaveLog(Map<String, Object> where,
			String group);
	public List<Map<String, Object>> contactSaveLog() throws Exception;
	


}
