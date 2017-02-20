package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

public interface LogReasonService {
	/**
	 * 添加驳回理由
	 * @param logId
	 * @param reason
	 * @param string 
	 */
	public void add(int logId, String reason, String name);
	/**
	 * 获取驳回理由列表
	 * @param logId
	 * @return
	 */
	public List<Map<String, Object>> getReasonById(int logId);
	public void addLogHistoryId(int logId, int logHistoryId);

}
