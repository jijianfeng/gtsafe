package com.zlzkj.app.service;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.ContactDao;
import com.zlzkj.app.model.Attribute;
import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;
import com.zlzkj.core.utils.UploadUtils;

@Service
@Transactional
public class ContactServiceImpl implements ContactService{

	@Autowired
	private ContactDao contactDao;
	@Autowired
	private ContactCateService contactCateService;
	@Autowired
	private AttributeService attributeService;
	@Autowired
	private ContactAttributeService contactAttributeService;

	@Override
	public Map<String, Object> getSearchList(int page, int rows, int id){
		if(id!=0){
			String 	where ="status=1 and contactCateId IN ( ";
			List<Map<String, Object>> cateIdList = contactCateService.getCate("pid="+id);
			if(cateIdList.size()!=0&&cateIdList!=null){
				for (Map<String, Object> map : cateIdList) {
					where = where+"'"+map.get("id").toString()+"'"+",";
				}
				where = where.substring(0,where.length()-1);
				where = where + ")";	
				return getContactList(page, rows, where);
			}
			where = where+"'"+id+"'"+")";
			return getContactList(page, rows, where);
		}
		String where ="status=1";
		return getContactList(page, rows, where);
	}


	@Override
	public Map<String, Object> getContactList(int page,int rows,String where) {
		List<Map<String, Object>> contactList = getContact(where, page, rows);
		for(Map<String, Object> map : contactList){
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
			ContactCate cate = contactCateService.findById(contactCateService.endCate(Integer.valueOf(map.get("contactCateId").toString())));
			map.put("cateName", CoreUtils.javaEscape(cate.getName()));
		}

		SQLBuilder sbContact = SQLBuilder.getSQLBuilder(Contact.class);
		int count = Integer.parseInt(contactDao.getValueBySQL(sbContact.where(where).buildCountSql()));
		return CoreUtils.getUIGridData(count, contactList);
	}

	@Override
	public Integer add(Contact contact) {
		ContactCate cate = contactCateService.findById(contact.getContactCateId());
		String number = cate.getNumber()+CoreUtils.frontCompWithZore(contact.getOrderId(), 2);
		contact.setNumber(number);

		Map<String, Object > where = new HashMap<String, Object>();
		where.put("number", number);

		List<Map<String, Object>> list = haveContact(where);
		if(list.size() > 0){
			return -1;
		}

		contact.setStatus((short)1);
		contact.setForward(0);
		contact.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) contactDao.save(contact);
	}

	@Override
	public Integer update(Contact contact) {
		ContactCate cate = contactCateService.findById(contact.getContactCateId());
		String number = cate.getNumber()+CoreUtils.frontCompWithZore(contact.getOrderId(), 2);
		contact.setNumber(number);

		Map<String, Object > where = new HashMap<String, Object>();
		where.put("number", number);
		where.put("id", new String[]{"<>",""+contact.getId()});

		List<Map<String, Object>> list = haveContact(where);
		if (list.size()>0) {
			return -1;
		}
		//改变原来的状态 至为(3)被继承
		int topId = this.topContact(contact.getId());
		
		Contact contactOld = contactDao.getById(contact.getId());
		contactOld.setStatus((short)3);
		contactDao.update(contact.getId(), contactOld);

		//添加新版本
		contact.setId(null);
		contact.setStatus((short)1);
		contact.setForward(topId);
		contact.setAddTime(CoreUtils.getNowTimestamp());

		return (Integer)contactDao.save(contact);
	}

	@Override
	public void del(int id) {
		//删除是将状态至为( 2 )已删除
		int topId = this.topContact(id);
		
		Contact contact = new Contact();
		contact.setStatus((short)3);
		contactDao.update(id, contact);
		//添加新版本
		Contact old = contactDao.getById(id);
		Map<String , Object > map = CoreUtils.convertBean(old);
		
		Contact delContact = (Contact)CoreUtils.convertMap(Contact.class, map);
		delContact.setId(null);
		delContact.setStatus((short)2);
		delContact.setForward(topId);
		delContact.setAddTime(CoreUtils.getNowTimestamp());

		contactDao.save(old);
		
	}

	@Override
	public Contact findById(int id) {
		List<Contact> contacts = contactDao.findByProperty("id", id);
		return( contacts != null && !contacts.isEmpty() )? contacts.get(0):null;
	}
	@Override
	public int topContact(int id) {
		int forward=id;
		int topId = 0;
		do {
			Contact contact = contactDao.getById(forward);
			forward = contact.getForward();
			topId = contact.getId();
		} while (forward!=0);
		return topId;
	}
	
//	@Override
//	public int endContact(int contactId) {
//		int forward=1;
//		int id = contactId;
//		do {
//			List<Contact> contact = contactDao.findByProperty("forward", id);
//			if (contact.size()>0) {
//				id = contact.get(0).getId();
//			}else {
//				forward = -1;
//			}
//		} while (forward!=-1);
//		return id;
//	}
//	
	@Override
	public List<Map<String, Object>> haveContact(Map<String, Object > where) {
		where.put("status", 1);
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(Contact.class);
		sbName.fields("*")
		.where(where);
		List<Map<String, Object>> list = contactDao.findBySQL(sbName.buildSql());
		return list;
	}

	@Override
	public List<Map<String, Object>> getContact(String where,Integer page,Integer rows) {

		SQLBuilder sbContact = SQLBuilder.getSQLBuilder(Contact.class);
		sbContact.fields("*")
		.where(where)
		.page(page, rows)
		.order("number", "ASC");
		List<Map<String, Object>> contactList = contactDao.findBySQL(sbContact.buildSql());
		for (Map<String, Object> map : contactList) {
			map.put("name", CoreUtils.javaEscape(map.get("name").toString()));
			if("1".equals(map.get("type").toString())){
				map.put("typeName", "选择");
				int attrCount = contactAttributeService.getAttrCountById(this.topContact((Integer)map.get("id")));
				map.put("attr", attrCount);
			}else if("0".equals(map.get("type").toString())){
				map.put("typeName", "填空");
				map.put("attr", "/");
			}else{
				map.put("typeName", "填空(预置)");
				int attrCount = contactAttributeService.getAttrCountById(this.topContact((Integer)map.get("id")));
				map.put("attr", attrCount);
			}
		}
		return contactList;
	}



	@Override
	public String contactImport(String url) throws Exception {
		url = UploadUtils.basisOsChange(url);
		Workbook wb = new XSSFWorkbook(new FileInputStream(url));
		Sheet sheet = wb.getSheetAt(0);
		String pidCateNumber = "";
		int pidCateId = 0;
		String cateNumber = "";
		int cateId = 0;
		String contactNumber = "";
		int contactId = 0;
		String attributeNumber ="";
		String error = "";
		for (Row row : sheet) {
			ContactCate pidCate = new ContactCate();
			ContactCate cate = new ContactCate();
			Contact contact = new Contact();
			Attribute attribute = new Attribute();
			if (row.getRowNum()!=0) {
				for (Cell cell : row) {
					switch (cell.getColumnIndex()) {
					//触点父分类编号
					case 0:
						if (!cell.toString().trim().equals("")) {
							pidCateNumber =CoreUtils.importIsNumber(cell.toString().trim());
							cateNumber=pidCateNumber;
							pidCate.setOrderId(Integer.parseInt(pidCateNumber));
						}
						break;
					case 1:
						if (!cell.toString().trim().equals("")) {
							if (pidCateNumber=="") {
								error+="请正确填写触点父分类"+cell.toString().trim()+"的编号，";
								break;
							}
							pidCate.setName(CoreUtils.htmlEscape(cell.toString().trim()));
							List<Map<String, Object >> list = contactCateService.getCate("number=\'"+pidCateNumber+"\' and status = 1");
							if (list!=null&&list.size()!=0) {
								pidCateId = contactCateService.topCate((Integer)list.get(0).get("id"));
							}else {
								pidCate.setPid(0);
								pidCate.setIsShow((short)1);
								ContactCate pidCateNew = new ContactCate();
								pidCateNew = pidCate;
								pidCateId = contactCateService.addCate(pidCateNew);
								cateId = pidCateId;
								if (pidCateId<=0) {
									error+="触点父分类添加失败，";
								}
							}
						}
						break;
						//触点子分类编号
					case 2:
						if (!cell.toString().trim().equals("")) {
							cateNumber =CoreUtils.importIsNumber(cell.toString().trim());
							cate.setOrderId(Integer.parseInt(cateNumber.substring(pidCateNumber.length(),cateNumber.length())));
						}
						break;
					case 3:
						if (!cell.toString().trim().equals("")) {
							if (cateNumber=="") {
								error+="请正确填写触点子分类"+cell.toString().trim()+"的编号，";
								break;
							}
							if(!pidCateNumber.equals(cateNumber.substring(0, pidCateNumber.length()))){
								cateNumber = "";
								error+="请正确填写触点子分类"+cell.toString().trim()+"的编号，";
								break;
							}
							cate.setName(CoreUtils.htmlEscape(cell.toString().trim()));
							List<Map<String, Object >> list = contactCateService.getCate("number=\'"+cateNumber+"\' and status = 1");
							if (list!=null&&list.size()!=0) {
								cateId = contactCateService.topCate((Integer)list.get(0).get("id"));
							}else {
								cate.setPid(pidCateId);
								cate.setIsShow((short)1);
								cateId = contactCateService.addCate(cate);
								if (cateId<=0) {
									error+="触点子分类添加失败，";
								}
							}
						}
						break;
						//触点项编号
					case 4:
						if (!cell.toString().trim().equals("")) {
							contactNumber=CoreUtils.importIsNumber(cell.toString().trim());
							contact.setOrderId(Integer.parseInt(contactNumber.substring(cateNumber.length(),contactNumber.length())));
						}
						break;
					case 5:
						if (!cell.toString().trim().equals("")) {
							if (contactNumber=="") {
								error+="请正确填写触点项"+cell.toString().trim()+"的编号，";
								break;
							}
							if(!cateNumber.equals(contactNumber.substring(0, cateNumber.length()))){
								contactNumber = "";
								error+="请正确填写触点项"+cell.toString().trim()+"的编号，";
								break;
							}
							contact.setName(CoreUtils.htmlEscape(cell.toString().trim()));
						}
						break;
					case 6:
						if (!cell.toString().trim().equals("")&&contactNumber!="") {
							if (cell.toString().trim().equals("是")) {
								contact.setIsSelect((short)1);
							}else if (cell.toString().trim().equals("不是")) {
								contact.setIsSelect((short)0);
							}else {
								error += "请正确填写触点项"+contact.getName()+"是否必选，";
							}
						}
						break;
					case 7:
						if (!cell.toString().trim().equals("")&&contactNumber!="") {
							if (cell.toString().trim().equals("选择")) {
								contact.setType((short)1);
							}else if (cell.toString().trim().equals("填空")) {
								contact.setType((short)0);
							}else {
								error += "请正确填写触点项"+contact.getName()+"的触点类型(选择或填空)，";
								break;
							}
							//							List<Map<String, Object >> list = getContact("number=\'"+contactNumber+"\' and status = 1",null,null);
							Map<String, Object > where = new HashMap<String, Object>();
							where.put("number", contactNumber);

							List<Map<String, Object>> list = haveContact(where);
							if (list!=null&&list.size()!=0) {
								error += "触点项"+contact.getName()+"已存在，";
								contactId =-2;
							}else {
								contact.setContactCateId(cateId);
								contactId = add(contact);
								if (contactId>0) {
									contactCateService.addCount(contact.getContactCateId(),1);
								}else{
									error+="触点添加失败，";
								}
							}
						}
						break;
						//触点项属性编号
					case 8:
						if (!cell.toString().trim().equals("")) {
							attributeNumber=CoreUtils.importIsNumber(cell.toString().trim());
						}
						break;
					case 9:
						if (!cell.toString().trim().equals("")) {
							if (attributeNumber=="") {
								error+="请正确填写触点项属性"+cell.toString().trim()+"的编号，";
								break;
							}
							if(!contactNumber.equals(attributeNumber.substring(0, contactNumber.length()))){
								attributeNumber = "";
								error+="请正确填写触点项属性"+cell.toString().trim()+"的编号，";
							}
							attribute.setName(cell.toString().trim());
						}
						break;
					case 10:
						if (!cell.toString().trim().equals("")&&attributeNumber!="") {
							attribute.setImportant(Integer.parseInt(CoreUtils.importIsNumber(cell.toString().trim())));
						}
						break;
					case 11:
						if (!cell.toString().trim().equals("")&&attributeNumber!="") {
								attribute.setScore(Integer.parseInt(CoreUtils.importIsNumber(cell.toString().trim())));
							Cell measures = row.getCell(12);
							if (measures!=null) {
								attribute.setMeasures(measures.toString().trim());
							}else {
								attribute.setMeasures("");
							}
							if(contactId==-2||contactId==-1){
								error+= "添加编号为"+attributeNumber+"的属性失败，";
							}else {
								int flag = attributeService.addAttr(attribute);
								if(flag > 0){
									contactAttributeService.add(flag,contactId);
								}else if(flag==-1){
									error+= "触点项属性已存在，";
								}else{
									error+="发生错误，请重试，";
								}
							}
						}
						break;
					case 12:
						break;
					}
				}
				if(error!=""){
					error = error.substring(0, error.length()-1)+"，因为没有完整的导入成功，请先删除导入excel表格中存在触点项，再重新导入excel";
					return error;
				}
			}
		}
		return error;
	}


	@Override
	public List<Map<String, Object>> getAllContacts(String where) {
		SQLBuilder sbContact = SQLBuilder.getSQLBuilder(Contact.class);
		sbContact.fields("id as contactId,name,type,contactCateId,addTime,number,isSelect")
				.where(where)
				.order("number", "ASC");
		List<Map<String, Object>> contactList = contactDao.findBySQL(sbContact.buildSql());
		return contactList;
	}


	@Override
	public Contact getContactByid(int id) {
		return contactDao.getById(id);
	}
	@Override
	public List<Map<String , Object >> getContactListByList(List<Map<String, Object>> list,int addTime) throws Exception {
		String[] contact=    {"contactId","contactName","type","contactNumber","val"};
		String[] contactName={"id",       "name",       "type","number",       "val"};
		
		List<Map<String, Object>> contactList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map3 : list) {
			Map<String, Object> contactMap = new HashMap<String, Object>();
			for (int i = 0; i < contact.length; i++) {
				contactMap.put(contactName[i], map3.get(contact[i]));
			}
			if (contactMap.get("type").toString().equals("1")) {  //||contactMap.get("type").toString().equals("2")
				List<Map<String, Object>> list4 = attributeService.getAttributeByVersion(
						this.topContact(Integer.valueOf(map3.get("contactId").toString())),addTime);
				for (Map<String, Object> encodeMap : list4) {
					encodeMap.put("encodeName",URLEncoder.encode(encodeMap.get("name").toString().trim().replace(" ", ""), "UTF-8"));
					encodeMap.put("encodeMeasures",URLEncoder.encode(encodeMap.get("measures").toString().trim().replace(" ", ""), "UTF-8"));
				}
				contactMap.put("attr", list4);
			}
			contactMap.put("encodeName",URLEncoder.encode(contactMap.get("name").toString().trim().replace(" ", ""), "UTF-8"));
			contactList.add(contactMap);

		}
		return contactList;
	}
	@Override
	public List<Map<String, Object>> getContactByList(String where,String group) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Contact.class);
		String[] fields ={
				"Contact.id as contactId",
				"ContactCate.id as cateId",
				"Contact.name as contactName",
				"Contact.type as type",
				"ContactCate.number as cateNumber",
				"Contact.number as contactNumber",
		};
		sqlBuilder.fields(fields)
		.join(ContactCate.class, "Contact.contactCateId = ContactCate.id")
		.where(where)
		.order("Contact.number", "ASC")
		.group(group);;
		return contactDao.findBySQL(sqlBuilder.buildSql());
	}
	
	@Override
	public List<Map<String, Object>> getApiContactListByList(List<Map<String, Object>> list, int addTime) throws Exception {
		String[] contact=    {"contactId","contactName","type","contactNumber","val"};
		String[] contactName={"id",       "name",       "type","number",       "val"};
		
		List<Map<String, Object>> contactList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map3 : list) {
			Map<String, Object> contactMap = new HashMap<String, Object>();
			for (int i = 0; i < contact.length; i++) {
				contactMap.put(contactName[i], map3.get(contact[i]));
			}
			if (contactMap.get("type").toString().equals("1")) {
				List<Map<String, Object>> list4 = attributeService.getAttributeByVersion(
						this.topContact(Integer.valueOf(map3.get("contactId").toString())),addTime);
				contactMap.put("attr", list4);
			}
			contactMap.put("encodeName",URLEncoder.encode(contactMap.get("name").toString().trim().replace(" ", ""), "UTF-8"));
			contactList.add(contactMap);

		}
		return contactList;
	}
}

