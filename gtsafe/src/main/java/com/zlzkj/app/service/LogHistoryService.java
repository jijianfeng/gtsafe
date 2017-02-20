package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

public interface LogHistoryService {

	public int add(int id);

	public Map<String, Object> getList(int page, int rows);

	public List<Map<String, Object>> contactLogHistory(int logHistoryId)
			throws Exception;

	public List<Map<String, Object>> getContactLogHistory(Map<String, Object> where,
			String group);

}
