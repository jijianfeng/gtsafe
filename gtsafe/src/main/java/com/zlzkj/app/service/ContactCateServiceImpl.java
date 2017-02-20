package com.zlzkj.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.ContactCateDao;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class ContactCateServiceImpl implements ContactCateService{
	@Autowired
	private ContactCateDao contactCateDao;

	@Override
	public List<Map<String, Object>> getCateList() {
		List<Map<String, Object>> list =getCate("pid=0 and status=1");
		for (Map<String, Object> map : list) {
			String where = "status=1 and pid="+this.topCate(Integer.valueOf(map.get("id").toString()));
			List<Map<String, Object>> list2 = getCate(where);
			map.put("children",list2);	
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getAllCateList(Integer level) {
		SQLBuilder sblist =SQLBuilder.getSQLBuilder(ContactCate.class);
		sblist.fields("id,name as text,pid,number as upNumber").where("pid=0 and status=1").order("number", "ASC");
		List<Map<String, Object>> listContactCate = contactCateDao.findBySQL(sblist.buildSql());
		for(Map<String, Object> map : listContactCate){
			map.put("id", this.topCate(Integer.valueOf(map.get("id").toString())));
			map.put("text", CoreUtils.javaEscape(map.get("text").toString()));
			if(level>1){
				sblist.fields("id,name as text,pid,number as upNumber").where("status=1 and pid="+map.get("id").toString()).order("number", "ASC");
				List<Map<String, Object>> list = contactCateDao.findBySQL(sblist.buildSql());
				for (Map<String, Object> map2 : list) {
					map2.put("id", this.topCate(Integer.valueOf(map2.get("id").toString())));
					map2.put("text", CoreUtils.javaEscape(map2.get("text").toString()));
					Map<String, Object> number = new HashMap<String, Object>();
					map2.put("attributes", number);
				}
				map.put("children",list);
			}
			Map<String, Object> number = new HashMap<String, Object>();
			map.put("attributes", number);
		}
		return  listContactCate;
	}

	@Override
	public int topCate(int cateId) {
		int forward=cateId;
		int topId = 0;
		do {
			ContactCate contactCate = contactCateDao.getById(forward);
			forward = contactCate.getForward();
			topId = contactCate.getId();
		} while (forward!=0);
		return topId;
	}

	public List<Map<String, Object>> haveCate(Map<String, Object > where) {
		where.put("status", 1);
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(ContactCate.class);
		sbName.fields("*")
		.where(where);
		List<Map<String, Object>> list = contactCateDao.findBySQL(sbName.buildSql());
		return list;
	}

	@Override
	public Integer addCate(ContactCate contactCate) {
		String upNumber ="";
		ContactCate pidCate = findById(contactCate.getPid());
		if(pidCate!=null)
			upNumber = pidCate.getNumber();
		String number = upNumber+CoreUtils.frontCompWithZore(contactCate.getOrderId(), 2);
		contactCate.setNumber(number);

		Map<String, Object > where = new HashMap<String, Object>();
		where.put("number", number);

		List<Map<String, Object>> list = haveCate(where);
		if (list.size()>0) {
			return -1;
		}

		contactCate.setStatus((short)1);
		contactCate.setCounts(0);
		contactCate.setForward(0);
		contactCate.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) contactCateDao.save(contactCate);
	}


	@Override
	public Integer update(ContactCate contactCate) {
		String upNumber ="";
		ContactCate pidCate = findById(contactCate.getPid());
		if(pidCate!=null)
			upNumber = pidCate.getNumber();
		String number = upNumber+CoreUtils.frontCompWithZore(contactCate.getOrderId(), 2);
		contactCate.setNumber(number);	

		Map<String, Object > where = new HashMap<String, Object>();
		where.put("number", number);
		where.put("id", new String[]{"<>",""+contactCate.getId()});
		List<Map<String, Object>> list = haveCate(where);
		if (list.size()>0) {
			return -1;
		}
		//改变原来的状态 至为(3)被继承
		int topId = this.topCate(contactCate.getId());
		ContactCate contactOld = contactCateDao.getById(contactCate.getId());
		contactOld.setStatus((short)3);
		contactCateDao.update(contactCate.getId(), contactOld);

		//添加新版本
		contactCate.setId(null);
		contactCate.setStatus((short)1);
		contactCate.setForward(topId);
		contactCate.setAddTime(CoreUtils.getNowTimestamp());

		return (Integer)contactCateDao.save(contactCate);
	}

	@Override
	public Integer del(int id) {

		SQLBuilder sbdel =SQLBuilder.getSQLBuilder(ContactCate.class);
		sbdel.fields("counts").where("id="+id);
		int delCount = Integer.parseInt(contactCateDao.getValueBySQL(sbdel.buildSql()));
		if (delCount > 0) {
			return 0;
		}

		SQLBuilder sql =SQLBuilder.getSQLBuilder(ContactCate.class);
		sql.fields("*").where("pid="+id+" AND status = 1");
		List<Map<String, Object>> list = contactCateDao.findBySQL(sql.buildSql());
		if(list.size() > 0){
			return -1;	
		}

		//删除是将状态至为( 2 )已删除
		int topId = this.topCate(id);
		
		ContactCate contactCate = new ContactCate();
		contactCate.setStatus((short)3);
		contactCateDao.update(id, contactCate);
		
		ContactCate old = contactCateDao.getById(id);
		Map<String , Object > map = CoreUtils.convertBean(old);
		
		ContactCate del = (ContactCate)CoreUtils.convertMap(ContactCate.class, map);
		//添加新版本
		del.setId(null);
		del.setStatus((short)2);
		del.setForward(topId);
		del.setAddTime(CoreUtils.getNowTimestamp());

		return (Integer)contactCateDao.save(del);
	}

	@Override
	public void addCount(Integer oldPidId,Integer count){
		int endId = this.endCate(oldPidId);
		String where1 = "id="+endId;
		contactCateDao.setIncDec(where1, "counts", count, "+");
		ContactCate add=contactCateDao.getById(endId);
		if(add!=null&&add.getPid()!=0){
			String where2 = "id="+this.endCate(add.getPid());
			contactCateDao.setIncDec(where2,"counts", count, "+");
		}
	}

	@Override
	public void delCount(Integer oldPidId,Integer count){
		if (oldPidId!=0) {
			int endId = this.endCate(oldPidId);
			String where1 = "id="+endId;
			contactCateDao.setIncDec(where1, "counts", count, "-");
			ContactCate del=contactCateDao.getById(endId);
			if(del!=null&&del.getPid()!=0){
				String where2 = "id="+this.endCate(del.getPid());
				contactCateDao.setIncDec(where2,"counts", count, "-");
			}
		}
	}
	@Override
	public ContactCate findById(int id) {
		List<ContactCate> contactCates = contactCateDao.findByProperty("id",this.endCate(id));
		return( contactCates != null && !contactCates.isEmpty() )? contactCates.get(0):null;
	}

	@Override
	public int endCate(int cateId) {
		int id = cateId;
		if (id==0) {
			return id;
		}
		SQLBuilder sbCate =SQLBuilder.getSQLBuilder(ContactCate.class);
		sbCate.fields("id")
		.where("forward="+id+" and status=1")
		.order("number", "ASC");
		List<Map<String, Object>> list = contactCateDao.findBySQL(sbCate.buildSql());
		if (list.size()>0) {
			id = Integer.valueOf(list.get(0).get("id").toString());
		}
		return id;
	}

	@Override
	public List<Map<String, Object>> getCate(String where) {
		SQLBuilder sbCate =SQLBuilder.getSQLBuilder(ContactCate.class);
		sbCate.fields("id,name,pid,counts,isShow,orderId,number").where(where).order("number", "ASC");
		List<Map<String, Object>> list = contactCateDao.findBySQL(sbCate.buildSql());
		for (Map<String, Object> map : list) {
			map.put("name", CoreUtils.javaEscape(map.get("name").toString()));
		}
		return  list;
	}

	@Override
	public Map<String, Object> getCateByVersion(int cateId,int addTime) {
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("addTime",new String[]{"<",""+addTime});
		String[] cataName={"id", "pid", "name", "number","status","forward"};
		SQLBuilder sbCate =SQLBuilder.getSQLBuilder(ContactCate.class);
		sbCate.fields(cataName)
		.where(where)
		.where("(id="+cateId+" or forward="+cateId+")")
		.order("addTime", "desc");
		List<Map<String, Object>> list = contactCateDao.findBySQL(sbCate.buildSql());
		Map<String, Object> map = list.get(0);
		if (!map.get("status").toString().equals("2")) {
			if (!map.get("forward").toString().equals("0")) {
				map.put("id", map.get("forward").toString());
			}
			return map;
		}
		return null;
	}
}

