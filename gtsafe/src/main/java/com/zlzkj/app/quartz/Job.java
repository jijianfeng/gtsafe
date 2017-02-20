package com.zlzkj.app.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zlzkj.app.model.Log;
import com.zlzkj.app.service.ContactLogService;
import com.zlzkj.app.service.LogService;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.UploadUtils;

public class Job {
	@Autowired
	private LogService logService;
	@Autowired
	private ContactLogService contactLogService;
	
	private static Logger log = Logger.getLogger(Job.class);
	private static final String CONFIG_QUARTZ = "quartz.properties";

	public void work() throws IOException{
		if (getConfig("quartz.open").equals("yes")) {
			int flag = this.autoUpdate();
			if(flag>0){
				log.info("日志添加成功");
			}else if(flag==-2){
				log.info("日志添加失败,昨日未填");
			}else if(flag==-1){
				log.info("日志添加失败,今天已添加");
			}else {
				log.info("日志添加失败");
			}
		}
		if (getConfig("quartz.open").equals("no")) {
			log.info("任务已关闭");
		}
	}

	public int autoUpdate() throws IOException {
		int day ;
		long basicTime = 12*60*60;
		long nowTime = CoreUtils.getNowTimestamp() - CoreUtils.formatTimestamp(CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(), "yyyy-MM-dd")+" 00:00:00");
		if (basicTime>nowTime) {
			day = 2;
		}else {
			day = 1;
		}
		Map<String, Object> map =logService.yesterdayList(day);
		if (map==null) {
			return -2;
		}
		int oldLogId = Integer.valueOf(map.get("id").toString());
		Log log = logService.getLogById(oldLogId);
		long addTime = CoreUtils.formatTimestamp(CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp()-((day-1)*24*3600),"yyyy-MM-dd")+" 00:00:00")+nowTime;
		int flag = logService.add(log.getTemplateId(), log.getScore(),CoreUtils.formatTimestamp((int)addTime),CoreUtils.getNowTimestamp());
		if(flag > 0){
			logService.statusRight(flag);
			List<Map<String, Object>> list = contactLogService.getValueByLogId(oldLogId);
			for (Map<String, Object> map2 : list) {
				contactLogService.add(map2.get("contactId").toString(), flag, map2.get("value").toString());
			}
		}
		return flag;
	}
	public static String getConfig(String key){
		ClassLoader loader = UploadUtils.class.getClassLoader();
		InputStream in = loader.getResourceAsStream(CONFIG_QUARTZ);
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			log.error("reading `" + CONFIG_QUARTZ + "` error!");
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
}
