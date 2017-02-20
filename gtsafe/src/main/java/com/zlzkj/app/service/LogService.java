package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Log;


public interface LogService {
	/**
	 * 获取日志列表
	 * @param page
	 * @param rows
	 * @return
	 */
	public	Map<String, Object> getList(int page, int rows, String whereString);
	
	/**
	 * 获取预览用的日志信息
	 * @return
	 */
	public List<Map<String, Object>> getPreviewLog();
	
	/**
	 * 昨日日志信息
	 * @return
	 */
	public Map<String, Object> yesterdayList(int day);

	/**
	 * 添加日志
	 * @param templateId
	 * @param score
	 * @param addTime 
	 * @return
	 */
	public int add(int templateId, int score, String time, Integer addTime);
	/**
	 * 获取未填写日志的日期
	 * @return
	 */
	public List<Map<String, Object>> getTime();
	/**
	 * 通过模板id查找日志
	 * @param templateId
	 * @return
	 */
	public List<Log> findLogByTempId(int templateId);
	/**
	 * 工期开始日期
	 * @return
	 */
	public String getStartDate();
	/**
	 * 计算离工期开始后的第一个日志的日期差
	 * @param timestamp
	 * @return
	 */
	public int dates(int timestamp);
	/**
	 * 更新日志
	 * @param id
	 * @param score
	 * @return
	 */
	public int update(int id, int score);
	public Log getLogById(int id);
	/**
	 * 更新日志状态
	 * @param id
	 * @return
	 */
	public int statusRight(int id);
	/**
	 * 更新日志状态
	 * @param id
	 * @return
	 */
	public int statusReject(int id);
	
	public Integer getApiLogId();
	/**
	 * 导出日志
	 * @param templateId
	 * @param list
	 * @param time 
	 * @return
	 * @throws Exception
	 */
	public String exportLog(List<Map<String, Object>> contactList, Integer logId,
			Integer addTime);
	public List<Map<String, Object>> getApiOldLogList(Integer logId);
	/**
	 * 电视机端获取日志信息
	 * @param logId 
	 * @return
	 */
	public List<Map<String, Object>> getApiLog(int addTime, String where);

	

}
