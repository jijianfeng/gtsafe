package com.zlzkj.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.ContactLogHistoryDao;
import com.zlzkj.app.dao.LogHistoryDao;
import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.app.model.ContactLogHistory;
import com.zlzkj.app.model.Log;
import com.zlzkj.app.model.LogHistory;
import com.zlzkj.app.model.Template;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class LogHistoryServiceImpl implements LogHistoryService{
	@Autowired
	private LogHistoryDao logHistoryDao;
	@Autowired
	private ContactLogHistoryDao contactLogHistoryDao;
	@Autowired
	private LogService logService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ContactLogService contactLogService;
	@Autowired
	private LogReasonService logReasonService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ContactCateService contactCateService;
	@Override
	public int add(int oldLogId) {
		Log log = logService.getLogById(oldLogId);
		
		LogHistory logHistory = new LogHistory();
		logHistory.setAddTime(log.getAddTime());
		logHistory.setCheckTime(log.getCheckTime());
		logHistory.setDay(log.getDay());
		logHistory.setScore(log.getScore());
		logHistory.setScore2(log.getScore2());
		logHistory.setScore3(log.getScore3());
		logHistory.setSummary(log.getSummary());
		logHistory.setVersion(log.getVersion());
		Template name = templateService.getTemplateById(log.getTemplateId());
		logHistory.setTemplateName(name.getName()!=null?name.getName():"无模板名");
		
		int flag = this.addLogHistory(logHistory);
		if(flag > 0){
			logReasonService.addLogHistoryId(log.getId(),flag);
			List<Map<String, Object>> list = contactLogService.getValueByLogId(oldLogId);
			for (Map<String, Object> map2 : list) {
				ContactLogHistory contactLogHistory = new ContactLogHistory();
				contactLogHistory.setLogHistoryId(flag);
				contactLogHistory.setContactId(Integer.valueOf(map2.get("contactId").toString()));
				contactLogHistory.setValue(map2.get("value").toString());
				this.addContactLogHistory(contactLogHistory);
			}
			return 1;
		}else{
			return -1;
		}
	}
	
	private int addLogHistory(LogHistory logHistory) {
		return (Integer)logHistoryDao.save(logHistory);
		
	}
	private void addContactLogHistory(ContactLogHistory contactLogHistory) {
		contactLogHistory.setAddTime(CoreUtils.getNowTimestamp());
		contactLogHistoryDao.save(contactLogHistory);
	}
	
	@Override
	public Map<String, Object> getList(int page, int rows){
		SQLBuilder  sbLog = SQLBuilder.getSQLBuilder(LogHistory.class);
		sbLog.fields("*")
		.order("addTime", "DESC")
		.page(page, rows);
		List<Map<String, Object>> list = logHistoryDao.findBySQL(sbLog.buildSql());
		for (Map<String, Object> map : list) {
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
		}
		int count = Integer.parseInt(logHistoryDao.getValueBySQL(sbLog.buildCountSql()));
		return CoreUtils.getUIGridData(count, list);
	}
	@Override
	public List<Map<String, Object>> contactLogHistory(int logHistoryId) throws Exception {

		List<Map<String, Object>> pidCateList = new ArrayList<Map<String,Object>>();
		Map<String, Object> where =new HashMap<String, Object>();
		where.put("ContactLogHistory.LogHistoryId", logHistoryId);
		List<Map<String, Object>> list2=this.getContactLogHistory(where,"ContactCate.id");

		//获取子节点的cate和contact和attr
		List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : list2) {
			Map<String, Object> cateMap = contactCateService.getCateByVersion(
					Integer.valueOf(map2.get("cateId").toString()),
					Integer.valueOf(map2.get("addTime").toString()));
			where.put("ContactCate.id", map2.get("cateId"));
			List<Map<String, Object>> list3=this.getContactLogHistory(where,"Contact.id");
			where.remove("ContactCate.id");

			cateMap.put("contact", contactService.getContactListByList(
					list3,Integer.valueOf(map2.get("addTime").toString())));

			if (cateMap.get("pid").toString().equals("0")) {
				cateMap.put("NoCate",1);
				pidCateList.add(cateMap);
			}else{
				cateList.add(cateMap);
			}
		}
		//获取父节点的cate
		List<String> pidList = new ArrayList<String>();
		for (Map<String, Object> map : cateList) {
			Map<String, Object> pidCateMap = contactCateService.getCateByVersion
					(Integer.valueOf(map.get("pid").toString())
							,CoreUtils.getNowTimestamp());
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			if (!pidList.contains(pidCateMap.get("id").toString())) {
				pidList.add(pidCateMap.get("id").toString());
			}else {
				continue;
			}
			for (Map<String, Object> map2 : cateList) {
				if (pidCateMap.get("id").toString().equals(map2.get("pid").toString())) {
					list.add(map2);
				}
			}
			pidCateMap.put("cate", list);
			pidCateList.add(pidCateMap);
		}

		return CoreUtils.sortList(pidCateList, "number");
	}

	@Override
	public List<Map<String, Object>> getContactLogHistory(Map<String , Object > where,String group) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(ContactLogHistory.class);
		String[] fields ={
				"LogHistory.id as logHistoryId",
				"ContactCate.id as cateId",
				"Contact.id as contactId",
				"Contact.name as contactName",
				"Contact.type as type",
				"ContactLogHistory.value as val",
				"ContactCate.number as cateNumber",
				"Contact.number as contactNumber",
				"LogHistory.version as addTime"
		};
		sqlBuilder.fields(fields)
				.join(LogHistory.class, "ContactLogHistory.logHistoryId=LogHistory.id")
				.join(Contact.class,"ContactLogHistory.contactId = Contact.id")
				.join(ContactCate.class, "Contact.contactCateId = ContactCate.id")
				.where(where)
				.order("Contact.number", "ASC")
				.group(group);
		return contactLogHistoryDao.findBySQL(sqlBuilder.buildSql());
	}

}
