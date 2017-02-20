package com.zlzkj.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.LogImageDao;
import com.zlzkj.app.model.LogImage;
import com.zlzkj.app.model.User;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;
import com.zlzkj.core.utils.UploadUtils;

@Service
@Transactional
public class LogImageServiceImpl implements LogImageService {
	
	@Autowired
	private LogImageDao LogImageDao;
	@Autowired
	private UserService userService;

	@Override
	public int add(LogImage logImage) {
		return (Integer) LogImageDao.save(logImage);
	}

	@Override
	public int update(LogImage logImage) {
		return LogImageDao.update(logImage.getId(), logImage);
	}
	
	@Override
	public void del(int id) {
		LogImageDao.delete(id);
	}

	@Override
	public Map<String, Object> getLogImageList(Map<String, Object> where,Map<String, String> pageParams) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(LogImage.class);
		sqlBuilder.fields("path as href")
				  .where(where)
				  .parseUIPageAndOrder(pageParams);
		List<Map<String, Object>> logImageList = LogImageDao.findBySQL(sqlBuilder.buildSql());
		int count = Integer.parseInt(LogImageDao.getValueBySQL(sqlBuilder.buildCountSql()));
		//return CoreUtils.getUIGridData(count, logImageList); 
		//仅返回片path
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("data",logImageList );
		return res;
	}

}
