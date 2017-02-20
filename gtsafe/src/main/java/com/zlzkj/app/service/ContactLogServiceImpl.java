package com.zlzkj.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.ContactLogDao;
import com.zlzkj.app.model.Attribute;
import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.app.model.ContactLog;
import com.zlzkj.app.model.Log;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class ContactLogServiceImpl implements ContactLogService{
	@Autowired
	private ContactLogDao contactLogDao;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ContactCateService contactCateService;

	@Override
	public void add(String id, int logId, String attr){
		ContactLog contactLog =new ContactLog();
		contactLog.setContactId(Integer.parseInt(id));
		contactLog.setLogId(logId);
		List<ContactLog> list =contactLogDao.findByExample(contactLog);
		if (list.size()>0) {
			ContactLog old =list.get(0);
			contactLog.setValue(attr);
			contactLog.setAddTime(CoreUtils.getNowTimestamp());
			contactLogDao.update(old.getId(),contactLog);
		}else {
			contactLog.setValue(attr);
			contactLog.setAddTime(CoreUtils.getNowTimestamp());
			contactLogDao.save(contactLog);
		}
	}
	@Override
	public List<Map<String, Object>> getApiContactLog(int logId) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(ContactLog.class);
		sqlBuilder.fields("ContactLog.id as id,Contact.name as contactName,Attribute.name as attributeName,Attribute.important as important,Attribute.measures as attributeMeasures")
		.join(Contact.class,"Contact.id=ContactLog.contactId")
		.join(Attribute.class, "ContactLog.value=Attribute.id")
		.where("ContactLog.logId="+logId+" AND Contact.type=1")
		.where("Attribute.score<>0")
		.page(1, 5)
		.order("important", "DESC");
		return contactLogDao.findBySQL(sqlBuilder.buildSql());
	}
	@Override
	public Map<String, Object> getValue(String where) {
		SQLBuilder sbContactLog = SQLBuilder.getSQLBuilder(ContactLog.class);
		sbContactLog.fields("id,logId,value,contactId").where(where);
		return contactLogDao.getBySQL(sbContactLog.buildSql());

	}
	@Override
	public List<Map<String, Object>> getValueByLogId(int logId) {
		SQLBuilder sbContactLog = SQLBuilder.getSQLBuilder(ContactLog.class);
		sbContactLog.fields("value,contactId").where("logId="+logId);
		return contactLogDao.findBySQL(sbContactLog.buildSql());

	}
	@Override
	public void del(int logId) {
		SQLBuilder sbDel =  SQLBuilder.getSQLBuilder(ContactLog.class);
		sbDel.fields("id,templateId").where("logId="+logId);
		contactLogDao.delete(sbDel.buildDeleteSql());
		return;

	}
	@Override
	public List<Map<String, Object>> getApiOldContactLog(List<Map<String, Object>> logId,String appearNumber) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(ContactLog.class);
		String[] fields ={
				"ContactLog.id as id",
				"Contact.name as contactName",
				"Attribute.name as attributeName",
				"Attribute.important as important",
				"Attribute.measures as attributeMeasures",
				"count(Attribute.id)",
		};
		sqlBuilder.fields(fields)
				.join(Contact.class,"Contact.id=ContactLog.contactId")
				.join(Attribute.class, "ContactLog.value=Attribute.id")
				.where("Contact.type=1 AND "+CoreUtils.whereIN("logId", logId, "id"))
				.where("Attribute.score <> 0")
				.page(1, 5)
				.order("important", "DESC")
				.order("count(Attribute.id)", "DESC")
				.group("Attribute.id having count(Attribute.id) >= "+appearNumber);
		return contactLogDao.findBySQL(sqlBuilder.buildSql());
	}


	@Override
	public List<Map<String, Object>> getContactLog(Map<String , Object > where,String group) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(ContactLog.class);
		String[] fields ={
				"Log.id as logId",
				"ContactCate.id as cateId",
				"Contact.id as contactId",
				"Contact.name as contactName",
				"Contact.type as type",
				"ContactLog.value as val",
				"ContactCate.number as cateNumber",
				"Contact.number as contactNumber",
				"Log.version as addTime"
		};
		sqlBuilder.fields(fields)
				.join(Log.class, "ContactLog.logId=Log.id")
				.join(Contact.class,"ContactLog.contactId = Contact.id")
				.join(ContactCate.class, "Contact.contactCateId = ContactCate.id")
				.where(where)
				.order("Contact.number", "ASC")
				.group(group);
		return contactLogDao.findBySQL(sqlBuilder.buildSql());
	}


	@Override
	public List<Map<String, Object>> contactLog(int logId) throws Exception {

		List<Map<String, Object>> pidCateList = new ArrayList<Map<String,Object>>();
		Map<String, Object> where =new HashMap<String, Object>();
		where.put("ContactLog.logId", logId);
		List<Map<String, Object>> list2=this.getContactLog(where,"ContactCate.id");

		//获取子节点的cate和contact和attr
		List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : list2) {
			Map<String, Object> cateMap = contactCateService.getCateByVersion(
					Integer.valueOf(map2.get("cateId").toString()),
					Integer.valueOf(map2.get("addTime").toString()));
			where.put("ContactCate.id", map2.get("cateId"));
			List<Map<String, Object>> list3=this.getContactLog(where,"Contact.id");
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


}



