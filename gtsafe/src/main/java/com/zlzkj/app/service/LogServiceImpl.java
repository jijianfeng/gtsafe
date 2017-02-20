package com.zlzkj.app.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.LogDao;
import com.zlzkj.app.model.Log;
import com.zlzkj.app.model.Template;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;
import com.zlzkj.core.utils.UploadUtils;

@Service
@Transactional
public class LogServiceImpl implements LogService{
	@Autowired
	private LogDao logDao;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ContactCateService contactCateService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private AttributeService attributeService;

	@Override
	public List<Map<String, Object>> getApiOldLogList(Integer logId) {
		Map<String, Object> configMap =configService.getConfig();
		int showDay = Integer.valueOf(configMap.get("show_day").toString());
		int addTime = CoreUtils.getNowTimestamp();
		if (logId!=null) {
			Log log = logDao.getById(logId);
			addTime=log.getAddTime();
		}
		String configBeginTime =configMap.get("begin_time").toString()+" 00:00:00";
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Log.class);
		String timeTop = CoreUtils.formatTimestamp(addTime-(showDay-1)*(24*3600),"yyyy-MM-dd")+" 00:00:00";
		String timeEnd = CoreUtils.formatTimestamp(addTime,"yyyy-MM-dd")+" 23:59:59";
		if (CoreUtils.formatTimestamp(timeTop)<CoreUtils.formatTimestamp(configBeginTime)) {
			timeTop=configBeginTime;
		}

		sqlBuilder.fields("id,templateId")
		.where("addTime>="+CoreUtils.formatTimestamp(timeTop)+" AND addTime<"+CoreUtils.formatTimestamp(timeEnd)+" AND status=2");
		List<Map<String, Object>> logList = null;
		logList = logDao.findBySQL(sqlBuilder.buildSql());
		if(logList.size()<=0) 
			return null;

		return logList;
	}

	@Override
	public Integer getApiLogId() {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Log.class);

		String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy-MM-dd");
		String timeTop = time+" 00:00:00";

		sqlBuilder.fields("id,templateId")
		.where("addTime<="+CoreUtils.getNowTimestamp()+" AND addTime>="+CoreUtils.formatTimestamp(timeTop)+" AND status=2");
		List<Map<String, Object>> logList = null;
		logList = logDao.findBySQL(sqlBuilder.buildSql());
		if(logList.size()<=0){
			time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp()-(24*3600),"yyyy-MM-dd");
			timeTop = time+" 00:00:00";
			String timeEnd = time+" 23:59:59";

			sqlBuilder.fields("id,templateId")
			.where("addTime<="+CoreUtils.formatTimestamp(timeEnd)+" AND addTime>="+CoreUtils.formatTimestamp(timeTop)+" AND status=2");

			logList = logDao.findBySQL(sqlBuilder.buildSql());
		}
		if(logList.size()<=0) 
			return null;
		int logId = Integer.parseInt(logList.get(0).get("id").toString());
		return logId;
	}
	@Override
	public Map<String, Object> getList(int page, int rows ,String where){
		SQLBuilder  sbLog = SQLBuilder .getSQLBuilder(Log.class);
		sbLog.fields("id,templateId,score,score2,score3,summary,day,addTime,status,checkTime")
		.where(where)
		.order("addTime", "DESC")
		.page(page, rows);
		List<Map<String, Object>> list = logDao.findBySQL(sbLog.buildSql());
		for (Map<String, Object> map : list) {
			int addTime = Integer.parseInt(map.get("addTime").toString());
			if (addTime > 0) {
				map.put("addTime",CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
			} else {
				map.put("addTime","/");
			}
			if("0".equals(map.get("status").toString())){
				map.put("statusName", "等待审核");
			}else if ("1".equals(map.get("status").toString())) {
				map.put("statusName", "已被驳回");
			}else if ("2".equals(map.get("status").toString())) {
				map.put("statusName", "已审核通过");
			}else {
				map.put("statusName", "/");
			}
			Template templateName = templateService.getTemplateById(Integer.parseInt(map.get("templateId").toString()));
			if (templateName!=null) {
				map.put("template", templateName.getName());
			}else {
				map.put("template", "模板已删除");
			}
		}
		int count = Integer.parseInt(logDao.getValueBySQL(sbLog.where(where).buildCountSql()));
		return CoreUtils.getUIGridData(count, list);
	}


	@Override
	public Map<String, Object> yesterdayList(int day){
		SQLBuilder sbOldLog =  SQLBuilder.getSQLBuilder(Log.class);
		String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp()-(day*24*3600),"yyyy-MM-dd");
		String timeTop = time+" 00:00:00";
		String timeEnd = time+" 23:59:59";
		sbOldLog.fields("id,templateId").where("addTime<"+CoreUtils.formatTimestamp(timeEnd)+" AND addTime>="+CoreUtils.formatTimestamp(timeTop));
		Map<String, Object> oldList=logDao.getBySQL(sbOldLog.buildSql());
		return oldList;

	}

	@Override
	public List<Map<String, Object>> getApiLog(int addTime,String where) {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		SQLBuilder  sbLog = SQLBuilder .getSQLBuilder(Log.class);
		sbLog.fields("id,summary as point,day")
		.where("addTime <="+addTime)
		.where(where)
		.order("day", "ASC");
		List<Map<String, Object>> list = logDao.findBySQL(sbLog.buildSql());
		int count = this.dates(addTime);
		int j = 0;
		for(int i=1;i<=count;i++){
			if(list.size()>j){
				Map<String, Object> one = list.get(j);
				if(Integer.parseInt(one.get("day").toString())==i){
					j=j+1;
					data.add(one);
				}else{
					Map<String, Object> two = new HashMap<String, Object>();
					two.put("id",0);
					two.put("point",0);
					two.put("day",i);
					data.add(two);
				}
			}else{
				Map<String, Object> two = new HashMap<String, Object>();
				two.put("id", 0);
				two.put("point",0);
				two.put("day",i);
				data.add(two);
			}
		}
		return data;
	}

	@Override
	public List<Map<String, Object>> getTime() {
		SQLBuilder sbDay = SQLBuilder.getSQLBuilder(Log.class);
		Map<String, Object> config = configService.getConfig();
		String configBeginTime =config.get("begin_time").toString()+" 00:00:00";
		String configEndTime =config.get("end_time").toString()+" 23:59:59";
		String startDate = getStartDate();
		String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy-MM-dd");
		if(CoreUtils.formatTimestamp(configEndTime)<CoreUtils.getNowTimestamp()){
			time = config.get("end_time").toString();
		}
		String timeEnd = time + " 23:59:59";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		long time1 = CoreUtils.formatTimestamp(startDate);
		long time2 = CoreUtils.formatTimestamp(timeEnd);
		while (time1<time2){
			int day = dates((int)time2);
			sbDay.fields("id,day,status").where("day="+day+" and addTime>="+CoreUtils.formatTimestamp(configBeginTime)+" and addTime<"+CoreUtils.formatTimestamp(configEndTime));
			Map<String, Object> logDay = logDao.getBySQL(sbDay.buildSql());
			if (logDay==null) {
				Map<String, Object> timeMap = new HashMap<String, Object>();
				timeMap.put("addTime", time);
				timeMap.put("id", time);
				list.add(timeMap);
			}else if (logDay.get("status").toString().equals("1")) {
				Map<String, Object> timeMap = new HashMap<String, Object>();
				timeMap.put("addTime", time+" (驳回)");
				timeMap.put("id", "logId-"+logDay.get("id"));
				list.add(timeMap);
			}
			time2 = time2 - 24*3600;
			time = CoreUtils.formatTimestamp((int)time2, "yyyy-MM-dd");
		} 
		return list;
	}
	@Override
	public int add(int templateId, int score, String time, Integer version) {
		String timeTop =  CoreUtils.formatTimestamp((int)CoreUtils.formatTimestamp(time),"yyyy-MM-dd")+" 00:00:00";
		String timeEnd =  CoreUtils.formatTimestamp((int)CoreUtils.formatTimestamp(time),"yyyy-MM-dd")+" 23:59:59";
		String timeWhere = "addTime >= "+CoreUtils.formatTimestamp(timeTop)+" AND addTime <"+CoreUtils.formatTimestamp(timeEnd);
		SQLBuilder sbTime = SQLBuilder.getSQLBuilder(Log.class);
		sbTime.fields("addTime").where(timeWhere);
		List<Map<String, Object>> logTimeList = logDao.findBySQL(sbTime.buildSql());
		if(logTimeList.size()>0){
			return  -2;
		}
		Map<String, Object> config = configService.getConfig();
		int score2 = Integer.parseInt(config.get("benchmark").toString());
		int score3 = Integer.parseInt(config.get("undulate").toString());
		int summary = score+score2+score3;
		Log addLog = new Log();
		addLog.setDay(dates((int)CoreUtils.formatTimestamp(time)));
		addLog.setTemplateId(templateId);
		addLog.setScore(score);
		addLog.setScore2(score2);
		addLog.setScore3(score3);
		addLog.setSummary(summary);
		addLog.setAddTime((int) CoreUtils.formatTimestamp(time));
		addLog.setCheckTime(0);
		addLog.setStatus((short)0);
		addLog.setVersion(version);
		return (Integer) logDao.save(addLog);
	}

	@Override
	public List<Log> findLogByTempId(int templateId) {
		return logDao.findByProperty("templateId", templateId);
	}


	@Override
	public int dates(int timestamp){
		String time = getStartDate();
		String timeNow = CoreUtils.formatTimestamp(timestamp,"yyyy-MM-dd")+" 00:00:00";
		long day= CoreUtils.DateDays(time,timeNow)+1;
		return (int)day;
	}

	@Override
	public String getStartDate(){
		SQLBuilder sbTime = SQLBuilder.getSQLBuilder(Log.class);
		Map<String, Object> config = configService.getConfig();
		String configBeginTime =config.get("begin_time").toString()+" 00:00:00";
		sbTime.fields("addTime").where("addTime>="+CoreUtils.formatTimestamp(configBeginTime)).order("addTime", "asc");
		List<Map<String, Object>> list = logDao.findBySQL(sbTime.buildSql());
		String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy-MM-dd")+" 00:00:00";
		if(list != null && list.size()!=0){
			time = CoreUtils.formatTimestamp((Integer)list.get(0).get("addTime"), "yyyy-MM-dd")+" 00:00:00";
		}
		return time;
	}


	@Override
	public List<Map<String, Object>> getPreviewLog() {
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		SQLBuilder  sbLog = SQLBuilder.getSQLBuilder(Log.class);
		sbLog.fields("id,summary as point,day")
		.where("addTime <="+CoreUtils.getNowTimestamp())
		.order("day", "ASC");
		List<Map<String, Object>> list = logDao.findBySQL(sbLog.buildSql());
		int count =this.dates(CoreUtils.getNowTimestamp());
		int j = 0;
		for(int i=1;i<count;i++){
			if(list.size()>j){
				Map<String, Object> one = list.get(j);
				if(Integer.parseInt(one.get("day").toString())==i){
					j=j+1;
					data.add(one);
				}else{
					Map<String, Object> two = new HashMap<String, Object>();
					two.put("id",0);
					two.put("point",0);
					two.put("day",i);
					data.add(two);
				}
			}else{
				Map<String, Object> two = new HashMap<String, Object>();
				two.put("id", 0);
				two.put("point",0);
				two.put("day",i);
				data.add(two);
			}
		}
		return data;
	}


	@Override
	public int statusReject(int id) {
		Log status = logDao.getById(id);
		if(status.getStatus()==1){
			return -1;
		}else if (status.getStatus()==2) {
			return -2;
		}
		status.setStatus((short)1);
		return logDao.update(id, status);
	}
	
	@Override
	public int statusRight(int id) {
		Log status = logDao.getById(id);
		if(status.getStatus()==1){
			return -1;
		}else if (status.getStatus()==2) {
			return -2;
		}
		status.setStatus((short)2);
		status.setCheckTime(CoreUtils.getNowTimestamp());
		return logDao.update(id, status);
	}


	@Override
	public int update(int id, int score) {
		Log log = new Log();
		Map<String, Object> config = configService.getConfig();
		int score2 = Integer.parseInt(config.get("benchmark").toString());
		int	score3 = Integer.parseInt(config.get("undulate").toString());
		int summary = score+score2+score3;
		log.setScore(score);
		log.setSummary(summary);
		log.setStatus((short)0);
		return (Integer) logDao.update(id, log);
	}


	@Override
	public Log getLogById(int id) {
		return logDao.getById(id);
	}
	@Override
	public String exportLog(List<Map<String	,Object>> contactList,Integer logId,Integer addTime){
		if (addTime==null) {
			addTime = CoreUtils.getNowTimestamp();
		}
		int CheckTime = addTime;
		if(logId!=null){
			Log log = getLogById(logId);
			addTime = log.getVersion();
			CheckTime = log.getCheckTime();
		}
		List<Map<String	,Object>> list = contactService.getAllContacts(CoreUtils.whereIN("id", contactList, "contactId"));
		List<Map<String	,Object>> allList = getPidCateByLogId(contactList,addTime);
		Map<String, Object> valMap = new HashMap<String, Object>();
		for (Map<String, Object> map : contactList) {
			valMap.put(map.get("contactId").toString(), map.get("value"));
		}
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("log");
		//设置单元格样式
		XSSFCellStyle style = wb.createCellStyle(); 
		style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
		style.setWrapText(true);
		//设置边框
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
		//设置各列的宽度
		sheet.setAutobreaks(true);
		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 8000);
		sheet.setColumnWidth(3, 10000);
		//第一行
		String[] array={
				"第一层次",
				"第二层次",
				"触点项",
				"属性"
		};
		XSSFRow rowHeader = sheet.createRow(0);
		for (int i = 0; i < array.length; i++) {
			XSSFCell cell = rowHeader.createCell(i);
			cell.setCellValue(array[i]);
			cell.setCellStyle(style);
		}
		sheet.createRow(1);
		//记录当前行数
		int i = 1;
		//记录当前父级分类编号
		int number0 = 1;
		for (Map<String, Object> pidMap : allList) {
			XSSFCell cell0 = sheet.getRow(i).createCell(0);
			cell0.setCellValue(number0+" "+pidMap.get("name").toString());
			cell0.setCellStyle(style);

			List<Map<String	,Object>> cate2 = contactCateService.getCate(CoreUtils.whereIN("id", list, "contactCateId")+" and pid="+contactCateService.topCate(Integer.valueOf(pidMap.get("id").toString())));
			//记录当前子级分类编号
			int number1 = 1;
			//记录父级分类之间相差的行数
			int sum0 = 0;
			for (Map<String, Object> map : cate2) {
				Map<String, Object> cateMap = contactCateService.getCateByVersion(
						Integer.valueOf(map.get("id").toString()),
						Integer.valueOf(addTime));
				
				XSSFCell cell1 = sheet.getRow(i).createCell(1);
				cell1.setCellValue(number0+"-"+number1+" "+cateMap.get("name").toString());
				cell1.setCellStyle(style);

				String where = CoreUtils.whereIN("id", list, "contactId")+" and contactCateId="+map.get("id").toString();
				List<Map<String	,Object>> list2 = contactService.getContact(where, null, null);
				//记录触点编号
				int number2 = 1;
				//记录子级分类之间相差的行数
				int sum1 = 0;
				for (Map<String, Object> map2 : list2) {
					XSSFCell cell2 = sheet.getRow(i).createCell(2);
					cell2.setCellValue(number0+"-"+number1+"-"+number2+" "+map2.get("name").toString());
					cell2.setCellStyle(style);
					if("1".equals(map2.get("type").toString())){
						List<Map<String	,Object>> list4 = attributeService.getAttributeByVersion(contactService.topContact(Integer.valueOf(map2.get("id").toString())), addTime);
						String val = "";
						int j =1;
						for (Map<String, Object> map3 : list4) {
							String circle = "○";
							if(valMap.get(map2.get("id").toString())!=null)
								if (valMap.get(map2.get("id").toString()).equals(map3.get("id").toString())) {
									circle = "●";
								}
							String number3 = number0+"-"+number1+"-"+number2+"-"+j;
							val = val+circle+number3+" "+map3.get("name").toString()+" \n";
							j++;
						}
						if (val!="") {
							val = val.substring(0,val.length()-2);
						}
						XSSFCell cell3 = sheet.getRow(i).createCell(3);
						cell3.setCellValue(val);
						cell3.setCellStyle(style);

					}else if ("0".equals(map2.get("type").toString())) {
						//如果是简答,将内容和触点项名单元格合并 填写在一起
						CellRangeAddress region= new CellRangeAddress(i, i, 2, 3);
						sheet.addMergedRegion(region);
						String val="";
						if(valMap.get(map2.get("id").toString())!=null)
							val=valMap.get(map2.get("id").toString()).toString();

						cell2.setCellValue(number0+"-"+number1+"-"+number2+" "+map2.get("name").toString()+" \n"+val);
						cell2.setCellStyle(style);
						setRegionBorder(XSSFCellStyle.BORDER_THIN, region, sheet, wb);
						
						XSSFRow rowNow = sheet.getRow(i);
						rowNow.setHeightInPoints(80);

					}
					//记录当前行数,并创建行
					i++;
					sheet.createRow(i);

					number2++;
					sum0++;
					sum1++;
				}
				number1++;
				//合并当前子级下的空白单元格
//				sheet.addMergedRegion(new CellRangeAddress(i-sum1, i-1, 1, 1));
				CellRangeAddress region3= new CellRangeAddress(i-sum1, i-1, 1, 1);
				sheet.addMergedRegion(region3);
				setRegionBorder(XSSFCellStyle.BORDER_THIN, region3, sheet, wb);
			}
			//没有2级分类时
			if (cate2==null||cate2.size()==0) {
				List<Map<String	,Object>> list2 = contactService.getContact( CoreUtils.whereIN("id", list, "contactId")+" and contactCateId="+contactCateService.topCate(Integer.valueOf(pidMap.get("id").toString())), null, null);
				for (Map<String, Object> map2 : list2) {
					XSSFCell cell2 = sheet.getRow(i).createCell(2);
					cell2.setCellValue(number0+"-"+number1+" "+map2.get("name").toString());
					cell2.setCellStyle(style);
					List<Map<String	,Object>> list4 = attributeService.getAttributeByVersion(Integer.valueOf(map2.get("id").toString()), addTime);
					if("1".equals(map2.get("type").toString())){
						String val = "";
						int j =1;
						for (Map<String, Object> map3 : list4) {
							String circle = "○";
							if(valMap.get(map2.get("id").toString())!=null)
								if (valMap.get(map2.get("id").toString()).equals(map3.get("id").toString())) {
									circle = "●";
								}
							String number3 = number0+"-"+number1+"-"+j;
							val = val+circle+number3+" "+map3.get("name").toString()+" \n";
							j++;
						}

						val = val.substring(0,val.length()-2);

						XSSFCell cell3 = sheet.getRow(i).createCell(3);
						cell3.setCellValue(val);
						cell3.setCellStyle(style);

					}else if ("0".equals(map2.get("type").toString())) {
						CellRangeAddress region= new CellRangeAddress(i, i, 2, 3);
						sheet.addMergedRegion(region);
						
						String val ="" ;
						if(valMap.get(map2.get("id").toString())!=null)
							val=valMap.get(map2.get("id").toString()).toString();

						cell2.setCellValue(number0+"-"+number1+" "+map2.get("name").toString()+" \n"+val);
						cell2.setCellStyle(style);
						setRegionBorder(XSSFCellStyle.BORDER_THIN, region, sheet, wb);
						
						XSSFRow rowNow = sheet.getRow(i);
						rowNow.setHeightInPoints(80);
						
						
					}
					//记录当前行数,并创建行
					i++;
					sheet.createRow(i);
					number1++;
					sum0++;
				}
				//合并当前子级下的空白单元格
//				sheet.addMergedRegion(new CellRangeAddress(i-sum0, i-1, 1, 1));
				CellRangeAddress region= new CellRangeAddress(i-sum0, i-1, 1, 1);
				sheet.addMergedRegion(region);
				setRegionBorder(XSSFCellStyle.BORDER_THIN, region, sheet, wb);
			}
			number0++;
			//合并当前父级下的空白单元格
//			sheet.addMergedRegion(new CellRangeAddress(i-sum0,i-1,0,0));
			CellRangeAddress region= new CellRangeAddress(i-sum0,i-1,0,0);
			sheet.addMergedRegion(region);
			setRegionBorder(XSSFCellStyle.BORDER_THIN, region, sheet, wb);
			
		}
		//填写最后两行
		

		XSSFRow rowEnd = sheet.getRow(i);
//		sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 1));
		CellRangeAddress region= new CellRangeAddress(i, i, 0, 1);
		sheet.addMergedRegion(region);
		setRegionBorder(XSSFCellStyle.BORDER_THIN, region, sheet, wb);
		rowEnd.createCell(0).setCellValue("填报人：");
//		sheet.addMergedRegion(new CellRangeAddress(i, i, 2, 3));
		CellRangeAddress region2= new CellRangeAddress(i, i, 2, 3);
		sheet.addMergedRegion(region2);
		setRegionBorder(XSSFCellStyle.BORDER_THIN, region2, sheet, wb);
		rowEnd.createCell(2).setCellValue("审核人：");
		rowEnd.setHeightInPoints(30);

		i++;
		rowEnd = sheet.createRow(i);
		//最后行样式
		XSSFCellStyle styleEnd = wb.createCellStyle(); 
		styleEnd.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styleEnd.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		styleEnd.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		styleEnd.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		styleEnd.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		styleEnd.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
		styleEnd.setWrapText(true);
//		sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 3));
		CellRangeAddress region3= new CellRangeAddress(i, i, 0, 3);
		sheet.addMergedRegion(region3);
		setRegionBorder(XSSFCellStyle.BORDER_THIN, region3, sheet, wb);
		
		XSSFCell cellEnd = rowEnd.createCell(0);
		if (logId!=null) {
			cellEnd.setCellValue("审核时间："+CoreUtils.formatTimestamp(CheckTime,"yyyy年MM月dd日"));
		}else {
			cellEnd.setCellValue("审核时间：      年    月    日");
		}
		
		cellEnd.setCellStyle(styleEnd);
		rowEnd.setHeightInPoints(30);

		String dir = "/log";
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
	
	private static void setRegionBorder(int border, CellRangeAddress region, XSSFSheet sheet,XSSFWorkbook wb){  
        RegionUtil.setBorderBottom(border,region, sheet, wb);  
        RegionUtil.setBorderLeft(border,region, sheet, wb);  
        RegionUtil.setBorderRight(border,region, sheet, wb);  
        RegionUtil.setBorderTop(border,region, sheet, wb);  
    }  
	
	public List<Map<String, Object>> getPidCateByLogId(List<Map<String, Object>> contactList,int addTime) {
		List<Map<String, Object>> pidCateList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> cateList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> list2=contactService.getContactByList(
				CoreUtils.whereIN("Contact.id", contactList, "contactId"),"ContactCate.id");
		//获取子节点的cate
		for (Map<String, Object> map2 : list2) {
			Map<String, Object> cateMap = contactCateService.getCateByVersion
									(Integer.valueOf(map2.get("cateId").toString()),addTime);
			if (cateMap.get("pid").toString().equals("0")) {
				cateMap.put("NoCate",1);
				pidCateList.add(cateMap);
			}else {
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


