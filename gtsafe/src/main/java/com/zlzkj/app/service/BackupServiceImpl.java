package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zlzkj.app.dao.BackupDao;
import com.zlzkj.app.model.Backup;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.DBUtils;
import com.zlzkj.core.utils.SQLBuilder;
import com.zlzkj.core.utils.UploadUtils;

@Service
@Transactional
public class BackupServiceImpl implements BackupService {
	
	@Autowired
	private BackupDao backupDao;
	
	@Override
	public int add(Backup backup) {
		List<Backup> backupList = backupDao.findByProperty("fileName", backup.getFileName());
		if(backupList.size() > 0){
			return -1;
		}
		backup.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) backupDao.save(backup);
	}

	@Override
	public Map<String, Object> getBackupList(int page, int rows) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Backup.class);
		sqlBuilder.fields("id,number,fileName as name,format,size,addTime").page(page, rows)
				  .order("addTime", "DESC");
		List<Map<String, Object>> backupList = backupDao.findBySQL(sqlBuilder.buildSql());
		for(Map<String, Object> map : backupList){
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
			map.put("size", DBUtils.humanReadableByteCount(Long.parseLong(map.get("size").toString()), false));
			map.put("filePath", UploadUtils.getFileServer() + "/backup/" + map.get("name").toString() + ".sql.gz");
			
		}
		
		int count = Integer.parseInt(backupDao.getValueBySQL(sqlBuilder.buildCountSql()));
		return CoreUtils.getUIGridData(count, backupList);
	}

	@Override
	public void del(int id) {
		backupDao.delete(id);
		
	}

}
