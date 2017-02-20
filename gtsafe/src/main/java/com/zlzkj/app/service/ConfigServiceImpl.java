package com.zlzkj.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zlzkj.app.dao.ConfigDao;
import com.zlzkj.app.model.Config;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;
@Service
@Transactional
public class ConfigServiceImpl implements ConfigService {
	
	@Autowired
	private ConfigDao configDao;

	@Override
	public Map<String, Object> getConfig() {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Config.class);
		sqlBuilder.fields("name","value").where("grouping='base'");
		List<Map<String, Object>> configList = configDao.findBySQL(sqlBuilder.buildSql());
		Map<String, Object> config = new HashMap<String,Object>();
		for(Map<String, Object> map : configList){
			String name = (String) map.get("name");
			if(name.equals("begin_time") || name.equals("end_time")){
				//System.out.println(map.get("value").getClass());
				config.put(name, CoreUtils.formatTimestamp(Integer.parseInt(map.get("value").toString()), "yyyy-MM-dd"));
			}else{
				config.put(name, CoreUtils.javaEscape(map.get("value").toString()));
			}
		}
		return config;
	}

	@Override
	public int updateConfig(Config config) {
		List<Config> configList = configDao.findByProperty("name", config.getName());
		Config configResult = new Config();
		if(configList != null && !configList.isEmpty()){
			//System.out.println(configResult);
			configResult = configList.get(0);
		}
		return configDao.update(configResult.getId(), config);
	}

	@Override
	public Map<String, Object> getApiConfig() {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Config.class);
		sqlBuilder.fields("name","value").where("grouping='base'");
		List<Map<String, Object>> configList = configDao.findBySQL(sqlBuilder.buildSql());
		Map<String, Object> config = new HashMap<String,Object>();
		for(Map<String, Object> map : configList){
			String name = (String) map.get("name");
			if(name.equals("begin_time") || name.equals("end_time")){
				//System.out.println(map.get("value").getClass());
				config.put(name, CoreUtils.formatTimestamp(Integer.parseInt(map.get("value").toString()), "yyyy-MM-dd"));
			}else{
				config.put(name, CoreUtils.javaEscape(map.get("value").toString()));
			}
		}
		return config;
	}


}
