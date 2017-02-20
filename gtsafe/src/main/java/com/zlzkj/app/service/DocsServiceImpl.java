package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.DocsDao;
import com.zlzkj.app.model.Docs;
import com.zlzkj.app.model.DocsCate;
import com.zlzkj.app.model.User;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;
import com.zlzkj.core.utils.UploadUtils;

@Service
@Transactional
public class DocsServiceImpl implements DocsService {
	
	@Autowired
	private DocsDao docsDao;
	@Autowired
	private DocsCateService docsCateService; 
	@Autowired
	private UserService userService;

	@Override
	public int add(Docs docs) {
		List<Docs> docList = docsDao.findByProperty("docsName", docs.getDocsName());
		if(docList.size() > 0){
			return -1;
		}
		docs.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) docsDao.save(docs);
	}

	@Override
	public int update(Docs docs) {
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(Docs.class);
		sbName.fields("name").where("id <> "+docs.getId()+" and name=\'"+docs.getName()+"\'");
		List<Docs> list = docsDao.findBySQLToEntity(sbName.buildSql());
		if (list.size()>0) {
			return -1;
		}
		return docsDao.update(docs.getId(), docs);
	}

	@Override
	public void del(int id) {
		docsDao.delete(id);
	}

	@Override
	public Map<String, Object> getDocsList(Map<String, Object> where,Map<String, String> pageParams) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Docs.class);
		sqlBuilder.fields("*")
				  .where(where)
				  .parseUIPageAndOrder(pageParams)
				  .order("addTime", "DESC");
		List<Map<String, Object>> docsList = docsDao.findBySQL(sqlBuilder.buildSql());
		for(Map<String, Object> map : docsList){
			if (map.get("buildTime")!=null) {
				map.put("buildTime",CoreUtils.formatTimestamp(Integer.valueOf(map.get("buildTime").toString()), "yyyy-MM-dd"));
			}
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
			DocsCate docsCate = docsCateService.findById((Integer)map.get("docsCateId"));
			map.put("docsCateName", docsCate.getName());
			
			User user = userService.findById((Integer)map.get("userId"));
			map.put("userName", user.getAccount());
			map.put("filePath", UploadUtils.parseFileUrl(map.get("name").toString()));
			
		}
		
		int count = Integer.parseInt(docsDao.getValueBySQL(sqlBuilder.buildCountSql()));
		return CoreUtils.getUIGridData(count, docsList);
	}
	
	@Override
	public Docs findById(Integer id) {
		List<Docs> docs = docsDao.findByProperty("id", id);
		return( docs != null && !docs.isEmpty() )? docs.get(0):null;
	}

}
