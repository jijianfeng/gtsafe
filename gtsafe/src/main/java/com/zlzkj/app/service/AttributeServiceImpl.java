package com.zlzkj.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.AttributeDao;
import com.zlzkj.app.model.Attribute;
import com.zlzkj.app.model.ContactAttribute;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class AttributeServiceImpl implements AttributeService {

	@Autowired
	private AttributeDao attributeDao;
	@Autowired
	private ContactAttributeService contactAttributeService;
	@Override
	public Map<String, Object> getAttrList(Integer page,Integer rows,int contactId){
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("contactId", contactId);
		where.put("status", 1);
		List<Map<String, Object>> arrtList =contactAttributeService.getContactAttribute(where,page,rows);
		for(Map<String, Object> map : arrtList){
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
		}
		int count = contactAttributeService.getAttrCountById(contactId);
		return CoreUtils.getUIGridData(count, arrtList);
	}

//	@Override
//	public int getAttrCountById(int id) {
//		SQLBuilder sbAttr = SQLBuilder.getSQLBuilder(Attribute.class).where("contactId="+id+" AND status=1");
//		return Integer.parseInt(attributeDao.getValueBySQL(sbAttr.buildCountSql()));
//	}

	@Override
	public Integer addAttr(Attribute attribute) {
		attribute.setStatus((short)1);
		attribute.setForward(0);
//		attribute.setContactId(contactId);
		attribute.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) attributeDao.save(attribute);
	}


	@Override
	public int del(int id) {
		int topId = topAttr(id);
		
		Attribute attribute = new Attribute();
		attribute.setStatus((short)3);
		attributeDao.update(id,attribute);
		
		Attribute old = attributeDao.getById(id);
		Map<String , Object > map = CoreUtils.convertBean(old);
		
		Attribute del = (Attribute)CoreUtils.convertMap(Attribute.class, map);
		//添加新版本
		del.setId(null);
		del.setStatus((short)2);
		del.setForward(topId);
		del.setAddTime(CoreUtils.getNowTimestamp());
		
		int attributeId = (Integer)attributeDao.save(del);
		
		ContactAttribute contactAttribute = contactAttributeService.getByAttrId(topId);
		contactAttributeService.add(attributeId, contactAttribute.getContactId());
		return 1;
	}

	@Override
	public int getScore(Integer attrId){
		Attribute score = attributeDao.getById(attrId);
		if(score!=null){
			return score.getScore();
		}
		return 0;
	}

	@Override
	public Attribute findById(int id) {
		List<Attribute> attributes = attributeDao.findByProperty("id", id);
		return( attributes != null && !attributes.isEmpty() )? attributes.get(0):null;
	}

	public int topAttr(int id) {
		int forward=id;
		int topId = 0;
		do {
			Attribute attribute = attributeDao.getById(forward);
			forward = attribute.getForward();
			topId = attribute.getId();
		} while (forward!=0);
		return topId;
	}
	@Override
	public int update(Attribute attribute) {
		//改变原来的属性的状态 至为(3)被继承
		int topId = topAttr(attribute.getId());

		Attribute attributeOld = attributeDao.getById(attribute.getId());
		attributeOld.setStatus((short)3);
		attributeDao.update(attribute.getId(),attributeOld);

		//添加新版本的属性
		attribute.setId(null);
//		attribute.setContactId(attributeOld.getContactId());
		attribute.setForward(topId); //继承于原来属性的id
		attribute.setAddTime(CoreUtils.getNowTimestamp());
		attribute.setStatus((short)1);
		int attributeId = (Integer)attributeDao.save(attribute);
		
		ContactAttribute contactAttribute = contactAttributeService.getByAttrId(topId);
		contactAttributeService.add(attributeId, contactAttribute.getContactId());
		return 1; 
	}

//	@Override
//	public List<Map<String, Object>> getAttribute(Map<String, Object> where,Integer page,Integer rows) {
//		SQLBuilder sbArrt =SQLBuilder.getSQLBuilder(Attribute.class);
//		sbArrt.fields("*")
//		.where(where)
//		.page(page, rows)
//		.order("score", "ASC");
//		return attributeDao.findBySQL(sbArrt.buildSql());
//	}

	@Override
	public List<Map<String, Object>> getAttributeByVersion(int contactId,int addTime) {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("ContactAttribute.contactId", contactId);
		where.put("Attribute.forward", 0);
		List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list =contactAttributeService.getContactAttribute(where,null,null);
		for (Map<String, Object> map : list) {
			Map<String, Object> where2 = new HashMap<String, Object>();
			where2.put("addTime",new String[]{"<",""+addTime});
			SQLBuilder sqlBuilder =SQLBuilder.getSQLBuilder(Attribute.class);
			sqlBuilder.fields("*")
			.where(where2)
			.where("(id="+map.get("id").toString()+" or forward="+map.get("id").toString()+")")
			.order("addTime", "desc");
			List<Map<String, Object>> list2 = attributeDao.findBySQL(sqlBuilder.buildSql());
			Map<String, Object> map2 = list2.get(0);
			if (!map2.get("status").toString().equals("2")) {
				attr.add(map2);
			}
		}
		return attr;
	}

}
