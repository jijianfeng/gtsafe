package com.zlzkj.app.service;

import java.util.Map;

import com.zlzkj.app.model.Config;

public interface ConfigService {
	/**
	 * 获取配置信息
	 * @return
	 */
	public Map<String, Object> getConfig();
	
	/**
	 * 获取电视端配置信息
	 * @return
	 */
	public Map<String, Object> getApiConfig();
	/**
	 * 提交更新
	 * @param config
	 * @return
	 */
	public int updateConfig(Config config);
	
}
