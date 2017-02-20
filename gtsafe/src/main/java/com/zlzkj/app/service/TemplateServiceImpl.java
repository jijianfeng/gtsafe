package com.zlzkj.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.TemplateDao;
import com.zlzkj.app.model.Template;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService{
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private TemplateContactService templateContactService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ContactCateService contactCateService;
	@Override
	public List<Map<String, Object>> getTemplateNameList(){
		SQLBuilder  sbTempName = SQLBuilder .getSQLBuilder(Template.class);
		sbTempName.fields("id,name");
		List<Map<String, Object>> tempName = templateDao.findBySQL(sbTempName.buildSql());
		for (Map<String, Object> map : tempName) {
			map.put("name",CoreUtils.javaEscape(map.get("name").toString()));
		}
		if (tempName==null||tempName.size()==0) {
			Map<String, Object> map =new HashMap<String, Object>();
			map.put("name","模板列表为空");
			map.put("id",0);
			tempName.add(map);
		}
		return tempName;

	}
	@Override
	public Map<String, Object> getTemplateList(int page, int rows){
		SQLBuilder sbTemp = SQLBuilder.getSQLBuilder(Template.class);
		sbTemp.fields("*").page(page, rows);
		List<Map<String, Object>> list=templateDao.findBySQL(sbTemp.buildSql());
		for(Map<String, Object> map: list){
			map.put("name",CoreUtils.javaEscape(map.get("name").toString()));
			map.put("remark",CoreUtils.javaEscape(map.get("remark").toString()));
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
		}
		int count = Integer.parseInt(templateDao.getValueBySQL(sbTemp.buildCountSql()));
		return CoreUtils.getUIGridData(count, list);	
	}
	@Override
	public Integer add(Template template){
		List<Template> templateList = templateDao.findByProperty("name", template.getName());
		if(templateList.size() > 0){
			return -1;
		}
		template.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) templateDao.save(template);
	}
	@Override
	public Integer update(Template template){
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(Template.class);
		sbName.fields("name").where("id <> "+template.getId()+" and name=\'"+template.getName()+"\'");
		List<Template> list = templateDao.findBySQLToEntity(sbName.buildSql());
		if (list.size()>0) {
			return -1;
		}
		return templateDao.update(template.getId(), template);

	}

	@Override
	public int del(int id){
		templateDao.delete(id);
		return 1;
	}

	@Override
	public Template getTemplateById(int id) {
		return templateDao.getById(id);
	}

	@Override
	public List<Map<String, Object>> template(int templateId) throws Exception {
		List<Map<String, Object>> pidCateList = new ArrayList<Map<String,Object>>();
		Map<String, Object> where =new HashMap<String, Object>();
		where.put("TemplateContact.templateId", templateId);
		List<Map<String, Object>> list2 = templateContactService.getTemplateContact(where,"ContactCate.id");

		//获取子节点的cate和contact和attr
		List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : list2) {
			Map<String, Object> cateMap = contactCateService.getCateByVersion
										(Integer.valueOf(map2.get("cateId").toString())
										,CoreUtils.getNowTimestamp());
			where.put("ContactCate.id", map2.get("cateId"));
			List<Map<String, Object>> list3 = templateContactService.getTemplateContact(where,"Contact.id");
			where.remove("ContactCate.id");
			//获取子节点的contact和attr
			cateMap.put("contact", contactService.getContactListByList(list3,CoreUtils.getNowTimestamp()));

			if (cateMap.get("pid").toString().equals("0")) {
				cateMap.put("NoCate",1);
				pidCateList.add(cateMap);
			}else{
				cateList.add(cateMap);
			}
		}
		
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
	public List<Map<String, Object>> templateApi(int templateId) throws Exception {
		List<Map<String, Object>> pidCateList = new ArrayList<Map<String,Object>>();
		Map<String, Object> where =new HashMap<String, Object>();
		where.put("TemplateContact.templateId", templateId);
		List<Map<String, Object>> list2 = templateContactService.getTemplateContact(where,"ContactCate.id");

		//获取子节点的cate和contact和attr
		List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map2 : list2) {
			Map<String, Object> cateMap = contactCateService.getCateByVersion
										(Integer.valueOf(map2.get("cateId").toString())
										,CoreUtils.getNowTimestamp());
			where.put("ContactCate.id", map2.get("cateId"));
			List<Map<String, Object>> list3 = templateContactService.getTemplateContact(where,"Contact.id");
			where.remove("ContactCate.id");
			//获取子节点的contact和attr
			cateMap.put("contact", contactService.getApiContactListByList(list3,CoreUtils.getNowTimestamp()));

			if (cateMap.get("pid").toString().equals("0")) {
				cateMap.put("NoCate",1);
				pidCateList.add(cateMap);
			}else{
				cateList.add(cateMap);
			}
		}
		
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
