package com.zlzkj.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.FieldDao;
import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.app.model.Field;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class FieldServiceImpl implements FieldService{
	@Autowired
	private FieldDao fieldDao;
	
	@Override
	public List<Map<String, Object>> getFieldList() {
		List<Map<String, Object>> list =getField("pid=0 and status=1");
		for (Map<String, Object> map : list) {
			String where = "status=1 and pid="+this.topField(Integer.valueOf(map.get("id").toString()));
			List<Map<String, Object>> list2 = getField(where);
			map.put("children",list2);	
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getField(String where) {
		// TODO Auto-generated method stub
		SQLBuilder field =SQLBuilder.getSQLBuilder(Field.class);
		field.fields("id,number,fieldName,pid,orderId,forward").where(where).order("number", "ASC");
		List<Map<String, Object>> list = fieldDao.findBySQL(field.buildSql());
		for (Map<String, Object> map : list) {
			map.put("fieldName", CoreUtils.javaEscape(map.get("fieldName").toString()));
		}
		return  list;
	}

	@Override
	public int topField(int fieldId) {
			int forward=fieldId;
			int topId = 0;
			do {
				Field field = fieldDao.getById(forward);
				forward = field.getForward();
				topId = field.getId();
			} while (forward!=0);
			return topId;
	}

	@Override
	public List<Map<String, Object>> haveField(Map<String, Object> where) {
		where.put("status", 1);
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(Field.class);
		sbName.fields("*")
		.where(where);
		List<Map<String, Object>> list = fieldDao.findBySQL(sbName.buildSql());
		return list;
	}

	@Override
	public Integer addField(Field field) {
		// TODO Auto-generated method stub
		String upNumber ="";
		Field pidField = findById(field.getPid());
		if(pidField!=null)
			upNumber = pidField.getNumber();
		String number = upNumber + CoreUtils.frontCompWithZore(field.getOrderId(), 2);
		field.setNumber(number);

		Map<String, Object > where = new HashMap<String, Object>();
		where.put("number", number);

		List<Map<String, Object>> list = haveField(where);
		if (list.size()>0) {
			return -1;
		}

		field.setStatus((short)1);
		field.setForward(0);
		field.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) fieldDao.save(field);
	}

	@Override
	public Field findById(int id) {
		List<Field> fields = fieldDao.findByProperty("id",this.endField(id));
		return( fields != null && !fields.isEmpty() )? fields.get(0):null;
	}

	@Override
	public int endField(int fieldId) {
		// TODO Auto-generated method stub
		int id = fieldId;
		if (id==0) {
			return id;
		}
		SQLBuilder sbCate =SQLBuilder.getSQLBuilder(Field.class);
		sbCate.fields("id")
		.where("forward="+id+" and status=1")
		.order("number", "ASC");
		List<Map<String, Object>> list = fieldDao.findBySQL(sbCate.buildSql());
		if (list.size()>0) {
			id = Integer.valueOf(list.get(0).get("id").toString());
		}
		return id;
	}

	@Override
	public List<Map<String, Object>> getAllFieldList(Integer level) {
		SQLBuilder sblist =SQLBuilder.getSQLBuilder(Field.class);
		sblist.fields("id,fieldName as text,pid,number as upNumber").where("pid=0 and status=1").order("number", "ASC");
		List<Map<String, Object>> listField = fieldDao.findBySQL(sblist.buildSql());
		for(Map<String, Object> map : listField){
			
			map.put("id", this.topField(Integer.valueOf(map.get("id").toString())));
			map.put("text", CoreUtils.javaEscape(map.get("text").toString()));
			
			if(level>1){
				sblist.fields("id,fieldName as text,pid,number as upNumber").where("status=1 and pid="+map.get("id").toString()).order("number", "ASC");
				List<Map<String, Object>> list = fieldDao.findBySQL(sblist.buildSql());
				for (Map<String, Object> map2 : list) {
					map2.put("id", this.topField(Integer.valueOf(map2.get("id").toString())));
					map2.put("text", CoreUtils.javaEscape(map2.get("text").toString()));
					Map<String, Object> number = new HashMap<String, Object>();
					map2.put("attributes", number);
				}
				map.put("children",list);
			}
			
			Map<String, Object> number = new HashMap<String, Object>();
			map.put("attributes", number);
			
		}
		return  listField;
	}

	@Override
	public Integer del(int id) {
		// TODO Auto-generated method stub
		SQLBuilder sql =SQLBuilder.getSQLBuilder(Field.class);
		sql.fields("*").where("pid="+id+" AND status = 1");
		List<Map<String, Object>> list = fieldDao.findBySQL(sql.buildSql());
		if(list.size() > 0){
			return -1;	
		}

		//删除是将状态至为( 2 )已删除
		int topId = this.topField(id);
		
		Field field = new Field();
		field.setStatus((short)3);
		fieldDao.update(id, field);
		
		Field old = fieldDao.getById(id);
		Map<String , Object > map = CoreUtils.convertBean(old);
		
		Field del = (Field)CoreUtils.convertMap(Field.class, map);
		//添加新版本
		del.setId(null);
		del.setStatus((short)2);
		del.setForward(topId);
		del.setAddTime(CoreUtils.getNowTimestamp());

		return (Integer)fieldDao.save(del);
	}

	@Override
	public Integer update(Field field) {
		// TODO Auto-generated method stub
		Field f = this.findById(field.getId());
		Map<String, Object > where = new HashMap<String, Object>();
		where.put("number", f.getNumber());
		where.put("id", new String[]{"<>",""+field.getId()});

		List<Map<String, Object>> list = haveField(where);
		if (list.size()>0) {
			return -1;
		}
		//改变原来的状态 至为(3)被继承
		int topId = this.topField(field.getId());
		
		Field fieldOld = fieldDao.getById(field.getId());
		fieldOld.setStatus((short)3);
		fieldDao.update(field.getId(), fieldOld);

		//添加新版本		
		field.setNumber(f.getNumber());
		field.setId(null);
		field.setStatus((short)1);
		field.setForward(topId);
		field.setAddTime(CoreUtils.getNowTimestamp());
		
		return (Integer)fieldDao.save(field);
	}
}
