package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.LogReasonDao;
import com.zlzkj.app.model.LogReason;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class LogReasonServiceImpl implements LogReasonService{
	@Autowired
	private LogReasonDao logReasonDao;

	@Override
	public void add(int logId, String reason, String name) {
		LogReason addReason = new LogReason();
		addReason.setAddTime(CoreUtils.getNowTimestamp());
		addReason.setLogId(logId);
		addReason.setAuditor(name);
		addReason.setReason(reason);
		addReason.setLogHistoryId(0);
		logReasonDao.save(addReason);
	}

	@Override
	public List<Map<String, Object>> getReasonById(int logId) {
		SQLBuilder sbReason = SQLBuilder .getSQLBuilder(LogReason.class);
		sbReason.fields("*").where("logId="+logId).order("addTime", "DESC");
		List<Map<String, Object>> list = logReasonDao.findBySQL(sbReason.buildSql());
		for (Map<String, Object> map : list) {
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
		}
		return list;
	}

	@Override
	public void addLogHistoryId(int logId, int logHistoryId) {
		List<LogReason> list = logReasonDao.findByProperty("logId", logId);
		for (LogReason logReason : list) {
			logReason.setLogHistoryId(logHistoryId);
			logReasonDao.update(logReason.getId(),logReason);
		}
	}

}
