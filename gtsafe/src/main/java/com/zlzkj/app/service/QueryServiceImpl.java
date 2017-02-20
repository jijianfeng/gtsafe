package com.zlzkj.app.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.QueryDao;
import com.zlzkj.app.model.Query;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.IniEditor;
import com.zlzkj.core.utils.SQLBuilder;
import com.zlzkj.core.utils.UploadUtils;

@Service
@Transactional
public class QueryServiceImpl implements QueryService{
	@Autowired
	private QueryDao queryDao;
	@Autowired
	private ConfigService configService;
	@Override
	public List<String> getAllContents() throws IOException {
		IniEditor IniEditor =getIni("查询目录");
		List<String> allContents = IniEditor.optionNames("AllContents");
		return allContents;
	}

	@Override
	public List<Map<String, Object>> getAllFields(String content) throws IOException {
		IniEditor IniEditor = getIni(content);
		List<String> AllFields = IniEditor.optionNames("AllFields");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (String field : AllFields) {
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("fieldName", field);
			map.put("field", field);
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getCompare() throws IOException {
		IniEditor IniEditor =getIni("查询目录");
		List<String> allCompare = IniEditor.optionNames("Compare");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (String compare : allCompare) {
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("compare", compare);
			map.put("val", IniEditor.get("Compare", compare));
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getCondition(String content, String[] fieldNames) throws IOException {

		IniEditor IniEditor = getIni(content);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (String fieldName : fieldNames) {
			fieldName =URLDecoder.decode(fieldName,"utf8");
			Map<String ,Object> map = new HashMap<String, Object>();
			map.put("id", IniEditor.get("AllFields", fieldName));
			map.put("text", fieldName);
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getValue(String content, String field) throws IOException {
		IniEditor IniEditor = getIni(content);
		String sql = IniEditor.get("BASESQL", "sql");
		String type = IniEditor.get("条件-"+field, "type");
		String group = IniEditor.get("条件-"+field, "sql");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if("1".equals(type)){
			sql = sql.replace("%select", IniEditor.get("条件-"+field, "Field")+" AS text");
			sql = sql.replace("%where",IniEditor.get("条件-"+field, "sql"));
			list = queryDao.findBySQL(sql+" "+group);
			int i=0;
			for (Map<String, Object> map : list) {
				map.put("text", CoreUtils.javaEscape(map.get("text").toString()));
				map.put("id", i);
				i++;
			}
		}else if ("2".equals(type)) {
			List<String> typeList = IniEditor.optionNames("状态-"+field);
			int i=0;
			for (String typeString : typeList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", i);
				map.put("text", IniEditor.get("状态-"+field, typeString));
				list.add(map);
				i++;
			}
		}else if ("3".equals(type)) {
			sql = sql.replace("%select", IniEditor.get("条件-"+field, "Field")+" AS text");
			sql = sql.replace("%where",IniEditor.get("条件-"+field, "sql"));
			list = queryDao.findBySQL(sql+" "+group);
			int i=0;
			for (Map<String, Object> map : list) {
				int addTime = Integer.parseInt(map.get("text").toString());
				if (addTime > 0) {
					map.put("text",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd"));
				} else {
					map.put("text","/");
				}
				map.put("id", i);
				i++;
			}
		}
		return list;
	}
	@Override
	public IniEditor getIni(String content) throws IOException{
		//配置文件路径
		String dir = "/custom-ini";
		String path = UploadUtils.getFileRepository()+dir;
		IniEditor IniEditor = new IniEditor();
		//获取到的配置文件名称
		IniEditor.load(path+"/"+content+".ini");
		return IniEditor;
	}

	@Override
	public String buildSelect(String content, String[] fields) throws IOException {
		IniEditor IniEditor = getIni(content);
		String sql = IniEditor.get("BASESQL", "sql");
		String select = "";
		for (String field : fields) {
			select = select +IniEditor.get("AllFields", field)+" AS "+field+",";
		}
		select = select.substring(0, select.length()-1);
		sql = sql.replace("%select", select);
		return sql;

	}

	@Override
	public String buildWhere(String content,String field, String compare, String compareVal) throws IOException {
		IniEditor IniEditor = getIni(content);
		String whereString = "";
		String type = IniEditor.get("条件-"+field, "type");
		if(type==null){
			return "-10";
		}
		if (compareVal.contains("@@")) {
			compareVal=compareVal.trim()+field;
		}
		if ("1".equals(type)) {
			compareVal = "\""+compareVal+"\"";
			whereString=field+" "+compare+" "+compareVal;
		}else if ("3".equals(type)) {
			if (compareVal.contains("@@")) {
				return "-2";
			}
			if (compare.equals("=")) {
				whereString=field+" >= "+CoreUtils.formatTimestamp(compareVal+" 00:00:00")+" and "+field+" < "+CoreUtils.formatTimestamp(compareVal+" 23:59:59");
			}
			if (compare.equals("<")||compare.equals("<=")) {
				whereString=field+" "+compare+" "+CoreUtils.formatTimestamp(compareVal+" 00:00:00");
			}
			if (compare.equals(">")||compare.equals(">=")) {
				whereString=field+" "+compare+" "+CoreUtils.formatTimestamp(compareVal+" 23:59:59");
			}
			if (compare.equals("<>")) {
				whereString=field+" < "+CoreUtils.formatTimestamp(compareVal+" 00:00:00")+" or "+field+" >= "+CoreUtils.formatTimestamp(compareVal+" 23:59:59");
			}
			if (compare.equals("Like")) {
				return "-1";
			}
		}else if("2".equals(type)){
			List<String> optionList = IniEditor.optionNames("状态-"+field);
			for (String option : optionList) {
				if(compareVal.equals(IniEditor.get("状态-"+field, option)))
					compareVal=option;
			}
			whereString=field+" "+compare+" "+compareVal;
		}

		return whereString;
	}

	@Override
	public List<Map<String, Object>> runSql(String sql,String content,String[] fieldNames) throws IOException {
		IniEditor IniEditor = getIni(content);
		sql = sql+"";
		List<Map<String, Object>> list = queryDao.findBySQL(sql);
		for (Map<String, Object> map : list) {
			for (String fieldName : fieldNames) {
				fieldName =URLDecoder.decode(fieldName,"utf8");
				if (map.get(fieldName)!=null)
					map.put(fieldName,CoreUtils.javaEscape(map.get(fieldName).toString()));
				String field = IniEditor.get("AllFields", fieldName);
				String type = IniEditor.get("条件-"+field, "type");
				if ("3".equals(type)) {
					int addTime = Integer.parseInt(map.get(fieldName).toString());
					if (addTime > 0) {
						map.put(fieldName,CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd"));
					} else {
						map.put(fieldName,"/");
					}
				}else if ("2".equals(type)) {
					map.put(fieldName, IniEditor.get("状态-"+field, map.get(fieldName).toString()));
				}
			}
		}
		return list;
	}

	@Override
	public int add(String select, String where, String content, String title,String fieldName) {
		List<Query> oldList = queryDao.findByProperty("name", title);
		if (oldList.size()>0) {
			return -1;
		}
		Query query =new Query();
		query.setAddTime(CoreUtils.getNowTimestamp());
		query.setFields(fieldName);
		query.setSqlTop(select);
		query.setSqlEnd(where);
		query.setName(title);
		query.setContent(content);
		return (Integer) queryDao.save(query);
	}

	@Override
	public List<Map<String, Object>> getQueryList(Map<String, Object> whereMap) {
		SQLBuilder sbQuery = SQLBuilder.getSQLBuilder(Query.class);
		sbQuery.fields("id,sqlEnd,name").where(whereMap);
		List<Map<String, Object>> list = queryDao.findBySQL(sbQuery.buildSql());
		for (Map<String, Object> map : list) {
			map.put("name", CoreUtils.htmlEscape(map.get("name").toString()));
			if (map.get("sqlEnd").toString().contains("@@")) {
				map.put("status", 1);
			}else {
				map.put("status", 0);
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getInputList(int id) throws IOException {
		List<Map<String, Object >> list = new ArrayList<Map<String,Object>>();
		Query query = queryDao.getById(id);
		String[] fieldNames = query.getFields().split(",");
		String  content = query.getContent();
		String whereString = query.getSqlEnd();
		IniEditor IniEditor = getIni(content);
		for (String fieldName : fieldNames) {
			String field = IniEditor.get("AllFields", fieldName);
			if (whereString	.contains("\"@@"+field+"\"")) {
				Map<String, Object> map =new HashMap<String, Object>();
				String  string =whereString.substring(0,whereString.indexOf("\"@@"+field+"\""));
				if (string.contains(" OR ")||string.contains(" AND ")) {
					if(string.lastIndexOf(" OR ")>string.lastIndexOf(" AND ")){
						string = string.substring(string.lastIndexOf(" OR ")+4, string.length());
					}else {
						string = string.substring(string.lastIndexOf(" AND ")+5, string.length());
					}
				}else if (string.contains("WHERE ")) {
					string = string.substring(string.lastIndexOf("WHERE ")+6, string.length());
				}
				if (string.contains(field)) {
					fieldName = fieldName+string.substring(field.length(), string.length());
				}
				map.put("fieldName", fieldName);
				List<Map<String, Object>> value = getValue(content, field);
				map.put("value", value);
				map.put("oldValue", "@@"+field);
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public int del(int id) {
		queryDao.delete(id);
		return 1;
	}

	@Override
	public Query getQueryById(int id) {
		Query query = queryDao.getById(id);
		return query;
	}

	@Override
	public Map<String, Object> runCondition(int id, List<Map<String, Object>> list) throws IOException {
		Query query = queryDao.getById(id);
		String[] fieldNames = query.getFields().split(",");
		List<Map<String, Object >> titles = getTitle(fieldNames);
		String where = query.getSqlEnd();
		for (Map<String, Object> map : list) {
			String string = where.substring(where.indexOf(map.get("name").toString())-6,where.indexOf(map.get("name").toString())-2);
			if (string.equals("Like"))
				map.put("val","%"+ map.get("val").toString()+"%");
			where = where.replace(map.get("name").toString(), map.get("val").toString());
		}
		String content = query.getContent();
		String select = query.getSqlTop();
		String sql = select+" "+where;
		List<Map<String, Object>> list2 = runSql(sql,content,fieldNames);
		Map<String, Object > map =new HashMap<String, Object>();
		map.put("title", titles);
		map.put("list", list2);
		return map;
	}

	@Override
	public Map<String, Object > run(int id) throws IOException {
		Query query = queryDao.getById(id);
		String[] fieldNames = query.getFields().toString().split(",");
		List<Map<String, Object >> titles = getTitle(fieldNames);
		String content = query.getContent();
		String select = query.getSqlTop();
		String where = query.getSqlEnd();
		String sql = select+" "+where;
		List<Map<String, Object>> list2 = runSql(sql,content,fieldNames);
		Map<String, Object > map2 =new HashMap<String, Object>();
		map2.put("title", titles);
		map2.put("list", list2);
		return map2;

	}

	@Override
	public int update(String select, String where, String content, String title, String fieldName,Integer id) {
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(Query.class);
		sbName.fields("name").where("id <>"+id+" and name=\'"+title+"\'");
		List<Query> list = queryDao.findBySQLToEntity(sbName.buildSql());
		if (list.size()>0) {
			return -1;
		}
		Query query =new Query();
		query.setAddTime(CoreUtils.getNowTimestamp());
		query.setFields(fieldName);
		query.setSqlTop(select);
		if(where=="")
			where=" ";
		query.setSqlEnd(where);
		query.setName(title);
		query.setContent(content);
		return (Integer) queryDao.update(id, query);
	}

	@Override
	public List<Map<String, Object>> findQuery(String value) {
		SQLBuilder sbQuery = SQLBuilder.getSQLBuilder(Query.class);
		sbQuery.fields("id,sqlEnd,name").where("name Like \"%"+value+"%\"");
		List<Map<String, Object>> list = queryDao.findBySQL(sbQuery.buildSql());
		for (Map<String, Object> map : list) {
			map.put("name", CoreUtils.htmlEscape(map.get("name").toString()));
			if (map.get("sqlEnd").toString().contains("@@")) {
				map.put("status", 1);
			}else {
				map.put("status", 0);
			}
		}
		return list;
	}

	@Override
	public String exportQuery(String data,String title) {
		JSONArray array = JSONArray.fromObject(data);
		JSONArray names = JSONArray.fromObject(title);

		@SuppressWarnings("unchecked")
		List<Map<String	, Object >> list = array;
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("query");
		//设置单元格样式
		XSSFCellStyle style = wb.createCellStyle(); 
		style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
		XSSFDataFormat format = wb.createDataFormat();  
		style.setDataFormat(format.getFormat("@"));  
		style.setWrapText(true);
		//设置各列的宽度
		sheet.setAutobreaks(true);

		//第一行
		int cellTop = 0;
		XSSFRow rowHeader = sheet.createRow(0);
		for (Object name: names) {
			rowHeader.createCell(cellTop).setCellValue(name.toString());
			cellTop++;
		}
		int row = 1;
		for (Map<String, Object > map : list) {
			sheet.createRow(row);
			int cell =0;
			for (Object name: names) {
				XSSFCell cellNow = sheet.getRow(row).createCell(cell);
				cellNow.setCellValue(map.get(name).toString());
				if(map.get(name).toString().getBytes().length>name.toString().getBytes().length){
					if(sheet.getColumnWidth(cell)<map.get(name).toString().getBytes().length*280)
						sheet.setColumnWidth((short)cell,map.get(name).toString().getBytes().length*280);
				}else{
					if(sheet.getColumnWidth(cell)<name.toString().getBytes().length*280)
						sheet.setColumnWidth((short)cell,name.toString().getBytes().length*280);
				}
				cellNow.setCellStyle(style);
				cell++;
			}
			row++;
		}

		String dir = "/query";
		String path = UploadUtils.getFileRepository()+dir;
		//检查目录，没有则创建
		File dirFile = new File(path);
		if (!dirFile.exists()){
			dirFile.mkdirs();
		}
		FileOutputStream fileOut;
		String fileName = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyyMMddHHmmss")+".xlsx";
		try {
			fileOut = new FileOutputStream(path+"/"+fileName);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constants.ERROR_FLAG+e.getLocalizedMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constants.ERROR_FLAG+e.getLocalizedMessage();
		}

		return UploadUtils.getFileServer()+dir+"/"+fileName;
	}
	@Override
	public List<Map<String, Object >> getTitle(String[] fieldNames) throws IOException{
		List<Map<String, Object >> list =new ArrayList<Map<String,Object>>();
		for (String fieldName : fieldNames) {
			Map<String, Object > map =new HashMap<String, Object>();
			//map.put("sortable", true);
			map.put("field", fieldName);
			map.put("title", fieldName);
			list.add(map);
		}
		return list;
	}   



}
