package com.zlzkj.app.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zlzkj.app.model.Imgurl;
import com.zlzkj.app.model.Log;
import com.zlzkj.app.model.LogImage;
import com.zlzkj.app.model.User;
import com.zlzkj.app.service.AttributeService;
import com.zlzkj.app.service.ConfigService;
import com.zlzkj.app.service.ContactLogService;
import com.zlzkj.app.service.ContactSaveLogService;
import com.zlzkj.app.service.ContactService;
import com.zlzkj.app.service.LogImageService;
import com.zlzkj.app.dao.LogImageDao;
import com.zlzkj.app.service.LogService;
import com.zlzkj.app.service.NoticeBoardService;
import com.zlzkj.app.service.TemplateService;
import com.zlzkj.app.service.UpimgService;
import com.zlzkj.app.service.UserService;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.UIUtils;
import com.zlzkj.core.utils.UploadUtils;
import com.zlzkj.core.utils.Utils;

/**
 * 移动端服务
 * @author jianfeng
 *
 */
@Controller
@RequestMapping(value="/api")
public class ApiController extends CoreController{
//	@Autowired
//	private ConfigService configService;
//	@Autowired
//	private NoticeBoardService noticeBoardService;
//	@Autowired
//	private LogService logService;
//	@Autowired
//	private ContactService contactService;
//	@Autowired
//	private ContactLogService contactLogService;

//	@RequestMapping(value="/getGTSafeInfo",method=RequestMethod.GET)
//	public void getGTSafeInfo(HttpServletRequest request,HttpServletResponse response,
//			Model model){
//		//基本配置信息
//		Map<String, Object> data = configService.getApiConfig();
//		String appearNumber = data.get("appear_number").toString();
//		
//		String policy = data.get("policy").toString();  //相对路径是也可以使用图片  未完成
//		List<String> listImgSrc = CoreUtils.getImgSrc(policy);
//		for (String string : listImgSrc) {//替换所有图片src 
//			policy = policy.replace(string,CoreUtils.serverUrl(request, string));
//		}
//		data.put("policy", policy);
//		//服务器当前时间
//		data.put("serverTime", CoreUtils.getNowTimestamp());
//		//布告栏
//		data.put("board",noticeBoardService.getApiNoticeBoard());
//		//日志分值
//		List<Map<String, Object>> log = logService.getApiLog(CoreUtils.getNowTimestamp(),"status=2");
//		String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy-MM-dd")+" 23:59:59";
//		int day = logService.dates((int)CoreUtils.formatTimestamp(time));
//		while (day>0&&Integer.parseInt(log.get(day-1).get("id").toString())==0) {
//			log.remove(day-1);
//			day--;
//		}
//		data.put("targetPoint",log);
//		//今天或者昨天的主要原因5条
//		List<Map<String, Object>> cause = null;
//		Integer logId=logService.getApiLogId();
//		if (logId!=null) {
//			cause = contactLogService.getApiContactLog(logId);
//		}
//		if(cause!=null)
//			for (Map<String, Object> map : cause) {
//				if(map.get("attributeMeasures").equals(""))
//					map.put("attributeMeasures", "无");
//			}
//		data.put("cause", cause);
//		//前7天的高频度主要原因
//		List<Map<String, Object>> logList=logService.getApiOldLogList(null);
//		List<Map<String, Object>> oldCause = contactLogService.getApiOldContactLog(logList,appearNumber);
//		if (oldCause!=null) 
//			for (Map<String, Object> map : oldCause) {
//				if(map.get("attributeMeasures").equals(""))
//					map.put("attributeMeasures", "无");
//			}
//		data.put("oldCause", oldCause);
//		//开始时间
//		data.put("startTime", logService.getStartDate());
//		ajaxReturn(response,data);
//	}
	/*
	 * 新api  2016.02.26
	 */
	@Autowired
	private ConfigService configService;
	@Autowired
	private NoticeBoardService noticeBoardService;
	@Autowired
	private LogService logService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private ContactLogService contactLogService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private UserService userService;
	@Autowired
	private ContactSaveLogService contactSaveLogService;
	@Autowired
	private AttributeService attributeService;
	@Autowired
	private UpimgService upimgService;
	@Autowired
	private LogImageService logImageService;
	//获取接口
	@RequestMapping(value="/getGTSafeInfo",method=RequestMethod.GET)
	public void getGTSafeInfo(HttpServletRequest request,HttpServletResponse response,
			Model model){
		//基本配置信息
		Map<String, Object> data = configService.getApiConfig();
		String appearNumber = data.get("appear_number").toString();
		
		String policy = data.get("policy").toString();  //相对路径是也可以使用图片  未完成
		List<String> listImgSrc = CoreUtils.getImgSrc(policy);
		for (String string : listImgSrc) {//替换所有图片src 
			policy = policy.replace(string,CoreUtils.serverUrl(request, string));
		}
		data.put("policy", policy);
		//服务器当前时间
		data.put("serverTime", CoreUtils.getNowTimestamp());
		//布告栏
		data.put("board",noticeBoardService.getApiNoticeBoard());
		//日志分值
		List<Map<String, Object>> log = logService.getApiLog(CoreUtils.getNowTimestamp(),"status=2");
		String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy-MM-dd")+" 23:59:59";
		int day = logService.dates((int)CoreUtils.formatTimestamp(time));
		while (day>0&&Integer.parseInt(log.get(day-1).get("id").toString())==0) {
			log.remove(day-1);
			day--;
		}
		data.put("targetPoint",log);
		//今天或者昨天的主要原因5条
		List<Map<String, Object>> cause = null;
		Integer logId=logService.getApiLogId();
		if (logId!=null) {
			cause = contactLogService.getApiContactLog(logId);
		}
		if(cause!=null)
			for (Map<String, Object> map : cause) {
				if(map.get("attributeMeasures").equals(""))
					map.put("attributeMeasures", "无");
			}
		data.put("cause", cause);
		//前7天的高频度主要原因
		List<Map<String, Object>> logList=logService.getApiOldLogList(null);
		List<Map<String, Object>> oldCause = contactLogService.getApiOldContactLog(logList,appearNumber);
		if (oldCause!=null) 
			for (Map<String, Object> map : oldCause) {
				if(map.get("attributeMeasures").equals(""))
					map.put("attributeMeasures", "无");
			}
		data.put("oldCause", oldCause);
		//开始时间
		data.put("startTime", logService.getStartDate());
		ajaxReturn(response,data);
	}
	
	
	//检查用户登录情况
	@RequestMapping(value="/checkLogin",method=RequestMethod.GET)
	public void checkLogin(HttpServletRequest request,HttpServletResponse response,@RequestParam("account")String account,@RequestParam("password")String password){
		User user = userService.findByAccount(account);
		if(((user!=null)&&password.equals(user.getPassword()))||(user != null && Utils.isPasswordMatch(password, user.getPassword()))) {
//			Map<String, Object> data = new HashMap<String,Object>();
//			data.put("userName", user.getAccount());
//			data.put("password", user.getPassword());
//			data.put("logId", user.getId());
			String[] data = new String [3];
			data [0] = user.getAccount();
			data [1] = user.getPassword();
			data [2] = String.valueOf(user.getId());
			ajaxReturn(response,data, "登录成功", 1);
		}else{
			ajaxReturn(response,null,"账号或密码错误",0);
		}
	}
	
	//获取模板
	@RequestMapping(value="/getTemplate",method=RequestMethod.GET)
	public void getTemplate(HttpServletRequest request,HttpServletResponse response,Model model,String type) throws Exception{
		List<Map<String, Object>> tempList = null;
		Map<String, Object> data = configService.getApiConfig();
		int templateId = Integer.valueOf(data.get("templateId").toString());
		if(type.equals("default")){
			tempList = templateService.templateApi(templateId);
			Map<String, Object> save = contactSaveLogService.findSaveNow(); 
			if (save!=null&&save.size()!=0) {
				tempList = contactSaveLogService.contactSaveLog();
			}
		}else if(type.equals("history")){
			int logId = Integer.parseInt(request.getParameter("logId")); 
			Log log = logService.getLogById(logId);
			tempList = contactLogService.contactLog(logId);
			//System.out.println("老日志");
		}
		if(tempList.size()==0){
			//System.out.println("dada");
			ajaxReturn(response,null, "昨日没有内容,请先填写昨日内容", 0);
		}
		ajaxReturn(response,tempList,"获取成功",1);
	}
	
	//获取日志列表
	@RequestMapping(value="/getLogList",method=RequestMethod.GET)
	public void getLogList(HttpServletRequest request,HttpServletResponse response,Model model,int page,int rows,Integer state){
		String where= "status<>1";
		if (state!=null&&state!=-1) {
			where += " and status="+state;
		}
		Map<String, Object> list= logService.getList(page, rows,where);
		ajaxReturn(response,list);	
	}

	
	//返回图片地址
	@RequestMapping(value="/getImgList",method=RequestMethod.GET)
	public void getImgList(HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> data = TestImg();
		ajaxReturn(response,data);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> TestImg(){
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashedMap();
		for(int i=1;i<=4;i++){
			Imgurl url = new Imgurl();
			url.setName("image"+i); 
			url.setUrl("http://192.168.1.110:8080/gtsafe/logimage/"+i+".jpg");
			map = CoreUtils.convertBean(url);
			data.add(map);
		}
		return data;
	}
	
	//上传图片
	@RequestMapping(value="/uploadImg",method=RequestMethod.POST)
	public void uploadImg(HttpServletRequest request,HttpServletResponse response,@RequestParam("upImage") MultipartFile image){
		String imageName = ""; //图片保存名
		Map<String,Object> info = UploadUtils.saveFileForApi(image);
		if((Integer)info.get("status")>0){ //上传完成
			imageName = info.get("saveName").toString();
		}else{ //上传出错
			ajaxReturn(response,null,info.get("errorMsg").toString(),0);
		}
	}
	
	//加载昨日填报内容
	@RequestMapping(value="/yesterday")
	public void yesterday(HttpServletRequest request,HttpServletResponse response) throws IOException {
		Map<String, Object> yesterdayList = logService.yesterdayList(1);
		if(yesterdayList!=null){
			System.out.println("嵇建峰>>>>>>>>>>>>>"+yesterdayList.size());
			System.out.println(yesterdayList.toString());
			ajaxReturn(response,yesterdayList,"获取昨日模板成功",1);
		}
		else {
			ajaxReturn(response,null, "昨日没有内容,请先填写昨日内容", 0);
		}
	}
	//接收安卓端填报信息并处理
	@RequestMapping(value="/uploadInfo",method=RequestMethod.POST)
	public void uploadInfo(HttpServletRequest request,HttpServletResponse response,Integer logId){
		List<Map<String, Object>> list = logService.getTime();
		if (list.size()==0) {
			ajaxReturn(response,null, "到今天为止,所有日志已全部填写完成,请不要再提交日志。", 0);
			return ;
		}
		//获取数据
		String json = request.getParameter("json");
		List<Map<String,Object>> jsonData = CoreUtils.readJson(json);
		System.out.println(">>>>>>>>>>>>>"+jsonData.size()+":"+json);
		if(jsonData.size() == 0){
			ajaxReturn(response,null,"获取上传数据失败", 0);
		}
		//处理数据
		Map<String, Object> data = configService.getApiConfig();
		//System.out.println(data.size()+"经济法"+data.toString());
		int score = this.getScore(jsonData);
		int templateId = Integer.valueOf(data.get("templateId").toString());
		int flag = 0;
		String infoString = "增加日志成功";
		if (logId!=null) {
			flag = logService.update(logId, score);
			infoString = "更新日志成功";
		}else {
			String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy-MM-dd")+" "+CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"HH:mm:ss");
			Integer addTime =CoreUtils.getNowTimestamp();
			flag = logService.add(templateId,score,time,addTime);
			logId = flag;
			//System.out.println("今天的日志Id"+logId);
		}
		if(flag > 0){
			for(int i = 0;i<jsonData.size();i++){
				Map<String,Object> map = jsonData.get(i);
				String attrId =  (String) map.get("attr");
				String contactId = (String) map.get("contact");
				contactLogService.add(contactId,logId,attrId);
			}
			//返回日志Id
			ajaxReturn(response,logId, infoString, 1);
		}else if(flag==-2){
			ajaxReturn(response,null,"已添加过日志,不能再添加", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		
	}
	
	//填报用于计算分数的方法
	private Integer getScore(List<Map<String,Object>> data){
		int score = 0;
		for(int i = 0;i<data.size();i++){
			Map<String,Object> map = data.get(i);
			int type = Integer.parseInt((String) map.get("type"));
			if(type == 1){
				int attrId = Integer.parseInt((String) map.get("attr"));
				score = score + attributeService.getScore(attrId);
			}
		}
		return score;
	}
	
	@RequestMapping(value={"photo_upload"},method = RequestMethod.POST)
	public String listAddPost(Model model,HttpServletRequest request,HttpServletResponse response ,
			@RequestParam("file") MultipartFile file ) throws Exception {
		String zipName = "";
		System.out.println(request.getParameter("logid"));
		int logId = Integer.valueOf(request.getParameter("logid"));
		if (!(file.isEmpty())) {
		      Map zipInfo = UploadUtils.saveMultipartFile(file, new String[0]);
		      if (((Integer)zipInfo.get("status")).intValue() > 0)
		      {
		        zipName = UploadUtils.parseFileUrl(zipInfo.get("saveName").toString());
//		        String osName = System.getProperty("os.name").toLowerCase();
//		        if (osName.indexOf("windows") != -1) {
//		          zipName = UploadUtils.getFullSavePath(zipInfo.get("saveName").toString()) + "\\" + zipInfo.get("saveName").toString();
//		        }
//		        else {
//		          zipName = UploadUtils.getFullSavePath(zipInfo.get("saveName").toString()) + "/" + zipInfo.get("saveName").toString();
//		        }
		        System.out.println(">>>>>>>>>>>>>>>>>>>图片路径："+zipName);
		        //添加图片信息
		        LogImage logImage = new LogImage();
		        logImage.setLogId(logId);
		        logImage.setPath(zipName);
		        int i = logImageService.add(logImage);
		        System.out.println(">>>>>>>>>>.保存ID"+i);
		        if(i>0){
		        	return ajaxReturn(response, null, "上传成功", 1);
		        }
		        else{
		        	return ajaxReturn(response, null, "上传失败", 0);
		        }
		      }
		      return ajaxReturn(response, null, zipInfo.get("errorMsg").toString(), 0);
		    }
		    return ajaxReturn(response, null, "上传文件不能为空", 0);
	}
	/**
	 * 获取
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value="/get_image_list")
	public void getImageList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> where = new HashMap<String,Object>();
		String logId = request.getParameter("logId");
		where.put("logId", logId);
		//System.out.println(">>>>>>>>"+ajaxReturn(response,logImageService.getLogImageList(where, UIUtils.getPageParams(request)).get("data").toString()));
		String text = logImageService.getLogImageList(where, UIUtils.getPageParams(request)).get("data").toString().replace("[", "").replace("]", "").replace("=", ":'").replace("}", "'}");
		//System.out.println(text);
		ajaxReturn(response,text);
	}
}
