package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.ContactAttributeDao;
import com.zlzkj.app.model.Attribute;
import com.zlzkj.app.model.ContactAttribute;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class ContactAttributeServiceImpl implements ContactAttributeService{
	@Autowired
	private ContactAttributeDao contactAttributeDao;
	
	@Override
	public void add(int attributeId,int id){
		ContactAttribute contactAttribute = new ContactAttribute();
		contactAttribute.setContactId(id);
		contactAttribute.setAttributeId(attributeId);
		contactAttribute.setAddTime(CoreUtils.getNowTimestamp());
		contactAttributeDao.save(contactAttribute);
		return ;
	}
	
	@Override
	public List<Map<String, Object>> getContactAttribute(Map<String , Object > where,Integer page,Integer rows) {
		SQLBuilder sbArrt =SQLBuilder.getSQLBuilder(ContactAttribute.class);
		sbArrt.fields("Attribute.id as id,"+
					"ContactAttribute.contactId as contactId,"+
					"Attribute.name as name,"+
					"Attribute.important as important,"+
					"Attribute.score as score,"+
					"Attribute.forward as forward,"+
					"Attribute.measures as measures,"+
					"Attribute.status as status,"+
					"Attribute.addTime as addTime")
					.join(Attribute.class, "Attribute.id=ContactAttribute.attributeId","RIGHT")
					.where(where).page(page, rows).order("score", "ASC");
		return contactAttributeDao.findBySQL(sbArrt.buildSql());
	}
	
	@Override
	public int getAttrCountById(int id) {
		SQLBuilder sbAttr = SQLBuilder.getSQLBuilder(ContactAttribute.class);
		sbAttr.fields("count(*)").join(Attribute.class, "Attribute.id=ContactAttribute.attributeId")
				.where("ContactAttribute.contactId="+id+" AND Attribute.status=1");
		return Integer.parseInt(contactAttributeDao.getValueBySQL(sbAttr.buildSql()));
	}
//	@Override
//	public void update(Attribute attribute) {
//		Attribute attributeOld = getById(attribute.getId());
//		attributeOld.setStatus((short)3);
//		attributeDao.update(attribute.getId(),attributeOld);
//
//		
//		List<ContactAttribute> contactAttributeList =contactAttributeDao.findByProperty("attributeId", attribute.getId());
//		ContactAttribute contactAttribute = contactAttributeList.get(0);
//		contactAttribute.setId(null);
//		contactAttribute.s
//		contactAttributeDao.save(contactAttribute);
//	}

	@Override
	public ContactAttribute getByAttrId(int attributeId) {
		List<ContactAttribute> contactAttributeList =contactAttributeDao.findByProperty("attributeId", attributeId);
		if (contactAttributeList.size()>0) {
			return contactAttributeList.get(0);
		}
		return null;
	}

}
