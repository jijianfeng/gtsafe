package com.zlzkj.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.TemplateContactDao;
import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.app.model.TemplateContact;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class TemplateContactServiceImpl implements TemplateContactService{
	@Autowired
	private TemplateContactDao templateContactDao;
	@Autowired
	private  ContactCateService contactCateService;
	@Autowired
	private  ContactService contactService;
	@Override
	public void add(String[] idStrings,Integer templateId){
		for (String string : idStrings) {
			Contact contact = contactService.getContactByid(Integer.parseInt(string));
			TemplateContact temp =new TemplateContact();
			temp.setContactCateId(contact.getContactCateId());
			temp.setContactId(Integer.parseInt(string));
			temp.setTemplateId(templateId);
			temp.setAddTime(CoreUtils.getNowTimestamp());
			templateContactDao.save(temp);
		}

	}
	public void del(int id){
		List<TemplateContact> delId = templateContactDao.findByProperty("templateId", id);
		for (TemplateContact templateContact : delId) {
			templateContactDao.delete(templateContact.getId());
		}
	}
	@Override
	public List<Map<String, Object>> getTempContactList(Integer templateId) {
		List<Map<String, Object>> pidCate = contactCateService.getCate("pid=0 and isShow=1 and status=1");
		for(Map<String, Object> map : pidCate){
			String where = "pid="+contactCateService.topCate(Integer.valueOf(map.get("id").toString()))+" and isShow = 1 and status=1"; 
			List<Map<String, Object>> cate = contactCateService.getCate(where);
			for (Map<String, Object> map2 :cate){
				String where2 ="status=1 and contactCateId="+contactCateService.topCate(Integer.valueOf(map2.get("id").toString()));
				List<Map<String, Object>> contactList = contactService.getContact(where2, null,null);
				int i=0;
				for(Map<String, Object> map3 : contactList){
					map3.put("isleaf", true);
					if(templateId!=null){
						SQLBuilder sbState = SQLBuilder.getSQLBuilder(TemplateContact.class);
						int state = Integer.parseInt(templateContactDao.getValueBySQL(sbState.where("contactId="+map3.get("id")+" and templateId="+templateId).buildCountSql()));
						if(state>0){
							map3.put("status", true);
							i++;
						}else {
							map3.put("status", false);
						}
					}
					
				}
				if (i==Integer.valueOf(map2.get("counts").toString())) {
					map2.put("status", true);
				}
				map2.put("id","cate"+map2.get("id").toString());
				map2.put("children", contactList);
			}
			if(cate==null||cate.size()==0){
				String where2 ="status=1 and contactCateId="+contactCateService.topCate(Integer.valueOf(map.get("id").toString()));
				List<Map<String, Object>> contactList = contactService.getContact(where2, null,null);
				for(Map<String, Object> map3 : contactList){
					map3.put("isleaf", true);
					if(templateId!=null){
						SQLBuilder sbState = SQLBuilder.getSQLBuilder(TemplateContact.class);
						int state = Integer.parseInt(templateContactDao.getValueBySQL(sbState.where("contactId="+map3.get("id")+" and templateId="+templateId).buildCountSql()));
						if(state>0){
							map3.put("status", true);
						}else {
							map3.put("status", false);
						}
					}
				}
				cate=contactList;
			}
			map.put("id","pidCate"+map.get("id").toString());
			map.put("children",cate);	
		}
		return  pidCate;
	}
	
	
	@Override
	public List<Map<String, Object>> getTemplateContact(Map<String, Object> where,String group) throws IOException{
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(TemplateContact.class);
		String[] fields ={
				"TemplateContact.templateId as templateId",
				"ContactCate.id as cateId",
				"Contact.id as contactId",
				"Contact.name as contactName",
				"Contact.type as type",
				"Contact.number as contactNumber"
		};
		sqlBuilder.fields(fields)
				.join(Contact.class,"TemplateContact.contactId = Contact.id")
				.join(ContactCate.class, "Contact.contactCateId = ContactCate.id")
				.where(where)
				.order("Contact.number", "ASC")
				.group(group);
		return templateContactDao.findBySQL(sqlBuilder.buildSql());
	}
	@Override
	public void update(int oldContactId, int newContactId) {
		List<TemplateContact> list = templateContactDao.findByProperty("contactId", oldContactId);
		for (TemplateContact templateContact : list) {
			templateContact.setContactId(newContactId);
			templateContactDao.update(templateContact.getId(), templateContact);
		}
	}
	
}
