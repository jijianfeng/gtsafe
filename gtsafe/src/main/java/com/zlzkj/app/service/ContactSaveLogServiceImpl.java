package com.zlzkj.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.ContactSaveLogDao;
import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.app.model.ContactSaveLog;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class ContactSaveLogServiceImpl implements ContactSaveLogService{

	@Autowired
	private ContactSaveLogDao contactSaveLogDao;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ContactCateService contactCateService;
	@Override
	public void add(String id, int templateId, String attrId,int addTime){
		ContactSaveLog contactSaveLog =new ContactSaveLog();
		contactSaveLog.setContactId(Integer.parseInt(id));
		contactSaveLog.setTemplateId(templateId);
		contactSaveLog.setValue(attrId);
		contactSaveLog.setAddTime(addTime);
		contactSaveLogDao.save(contactSaveLog);
	}
	@Override
	public Map<String, Object> getValue(String where) {
		SQLBuilder sbContactLog = SQLBuilder.getSQLBuilder(ContactSaveLog.class);
		sbContactLog.fields("id,templateId,value,contactId").where(where);
		Map<String, Object> value = contactSaveLogDao.getBySQL(sbContactLog.buildSql());
		return value;
	}


	@Override
	public Map<String, Object> findSaveNow() {
		SQLBuilder sbSaveNow =  SQLBuilder.getSQLBuilder(ContactSaveLog.class);
		sbSaveNow.fields("addTime");
		Map<String, Object> saveId=contactSaveLogDao.getBySQL(sbSaveNow.buildSql());
		return saveId;
	}
	@Override
	public void delNow() {
		SQLBuilder sbDel =  SQLBuilder.getSQLBuilder(ContactSaveLog.class);
		sbDel.fields("id,templateId");
		contactSaveLogDao.delete(sbDel.buildDeleteSql());
		return;

	}
	@Override
	public List<Map<String, Object>> getContactSaveLog(Map<String, Object> where ,String group) {
		SQLBuilder sqlBuilder =  SQLBuilder.getSQLBuilder(ContactSaveLog.class);
		String[] fields ={
				"ContactCate.id as cateId",
				"Contact.id as contactId",
				"Contact.name as contactName",
				"Contact.type as type",
				"ContactSaveLog.value as val",
				"ContactCate.number as cateNumber",
				"Contact.number as contactNumber",
				"ContactSaveLog.addTime as addTime"
		};
		sqlBuilder.fields(fields)
		.join(Contact.class,"ContactSaveLog.contactId = Contact.id")
		.join(ContactCate.class, "Contact.contactCateId = ContactCate.id")
		.where(where)
		.order("Contact.number", "ASC")
		.group(group);
		return contactSaveLogDao.findBySQL(sqlBuilder.buildSql());

	}
	@Override
	public List<Map<String, Object>> contactSaveLog() throws Exception {

		List<Map<String, Object>> pidCateList = new ArrayList<Map<String,Object>>();
		Map<String, Object> where =new HashMap<String, Object>();
		List<Map<String, Object>> list2=this.getContactSaveLog(where,"ContactCate.id");

		//获取子节点的cate和contact和attr
		List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : list2) {
			Map<String, Object> cateMap = contactCateService.getCateByVersion
					(Integer.valueOf(map2.get("cateId").toString())
							,Integer.valueOf(map2.get("addTime").toString()));
			where.put("ContactCate.id", map2.get("cateId"));
			List<Map<String, Object>> list3=this.getContactSaveLog(where,"Contact.id");
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
