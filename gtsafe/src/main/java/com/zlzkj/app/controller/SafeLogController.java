package com.zlzkj.app.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.Log;
import com.zlzkj.app.model.User;
import com.zlzkj.app.service.AttributeService;
import com.zlzkj.app.service.ConfigService;
import com.zlzkj.app.service.ContactLogService;
import com.zlzkj.app.service.ContactSaveLogService;
import com.zlzkj.app.service.ContactService;
import com.zlzkj.app.service.LogHistoryService;
import com.zlzkj.app.service.LogImageService;
import com.zlzkj.app.service.LogReasonService;
import com.zlzkj.app.service.LogService;
import com.zlzkj.app.service.NoticeBoardService;
import com.zlzkj.app.service.TemplateService;
import com.zlzkj.core.utils.UIUtils;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.UploadUtils;

/**
 * 安全日志控制器
 */
@Controller
@RequestMapping(value="/safeLog")
public class SafeLogController extends CoreController{
	@Autowired
	private LogService logService;
	@Autowired
	private AttributeService attributeService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private ContactSaveLogService contactSaveLogService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private NoticeBoardService noticeBoardService;
	@Autowired
	private ContactLogService contactLogService;
	@Autowired
	private ContactService contactService;
	@Autowired
	private LogReasonService logReasonService;
	@Autowired
	private LogHistoryService logHistoryService;
	@Autowired
	private LogImageService logImageService;
	/**
	 * 获取日志列表
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public void list(HttpServletRequest request,HttpServletResponse response,int page,int rows,Integer state){
		String where= "status<>1";
		if (state!=null&&state!=-1) {
			where += " and status="+state;
		}
		Map<String, Object> list= logService.getList(page, rows,where);
		ajaxReturn(response,list);	

	}
	@RequestMapping(value="/list2",method=RequestMethod.POST)
	public void list2(HttpServletRequest request,HttpServletResponse response,int page,int rows,Integer state){
		Map<String, Object> list= logHistoryService.getList(page, rows);
		ajaxReturn(response,list);	
		
	}

	/**
	 * 显示昨日内容
	 * @param model
	 * @throws IOException 
	 */
	@RequestMapping(value="/yesterday")
	public void yesterday(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		Map<String, Object> yesterdayList = logService.yesterdayList(1);
		if(yesterdayList!=null){
			ajaxReturn(response,yesterdayList,"",1);
		}else {
			ajaxReturn(response,null, "昨日没有内容,请先填写昨日内容", 0);
		}
		return;
	}
	/**
	 * 保存内容
	 * @param ids
	 * @throws Exception 
	 */
	@RequestMapping(value="/saveNow",method=RequestMethod.POST)
	public void saveNow(HttpServletRequest request,HttpServletResponse response,String[] ids)  {
		Integer isOut = this.checkAndScore(request,response,ids);
		if (isOut==null) {
			return ;
		}
		Map<String, Object> data = configService.getApiConfig();
		int templateId = Integer.valueOf(data.get("templateId").toString());
		contactSaveLogService.delNow();
		Integer addTime =CoreUtils.getNowTimestamp();
		if (request.getParameter("addTime")!=null&&request.getParameter("addTime")!="") {
			addTime = Integer.valueOf(request.getParameter("addTime"));
		}
		for(String id:ids){
			if(request.getParameter("contact_"+id)!=null){
				System.out.println("胡海杰正在打印：contact_"+id);
				String attr = request.getParameter("contact_"+id);
				attr = attr.trim();
				contactSaveLogService.add(id,templateId,attr,addTime);
			}
		}
		ajaxReturn(response,null, "保存成功~", 1);
		return ;
	}


	/**
	 * 显示模板
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/log",method=RequestMethod.GET)
	public String log(HttpServletRequest request,HttpServletResponse response,Model model,String type) throws Exception {
		List<Map<String, Object>> tempList = null;
		Map<String, Object> data = configService.getApiConfig();
		int templateId = Integer.valueOf(data.get("templateId").toString());
		if(type.equals("default")){
			tempList = templateService.template(templateId);
			Map<String, Object> save = contactSaveLogService.findSaveNow(); 
			if (save!=null&&save.size()!=0) {
				model.addAttribute("addTime", save.get("addTime"));
				tempList = contactSaveLogService.contactSaveLog();
			}
		}else if (type.equals("change")) {
			model.addAttribute("addTime", "");
			tempList = templateService.template(templateId);
		}else if(type.equals("history")){
			int logId = Integer.parseInt(request.getParameter("logId")); 
			Log log = logService.getLogById(logId);
			model.addAttribute("addTime", log.getVersion());
			tempList = contactLogService.contactLog(logId);
		}else if (type.equals("update")) {
			int logId = Integer.valueOf(request.getParameter("logId"));
			model.addAttribute("logId", logId);
			tempList = contactLogService.contactLog(logId);
		}
		model.addAttribute("tempList", tempList);
		return "safeLog/log";
	}
	/**
	 * 模板列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(HttpServletRequest request,HttpServletResponse response,Model model) {
		model.addAttribute("serviceTime",CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy年MM月dd日"));
		Map<String, Object> save = contactSaveLogService.findSaveNow();
		if (save!=null&&save.size()!=0) {
			model.addAttribute("save", "系统检测到还有未提交的保存内容，请先提交该内容后再新增日志");
		}
		
		List<Map<String, Object>> time = logService.getTime();
		if (time.size()>1) {
			model.addAttribute("fill", "除今天外，有工期未填写日志或者有被驳回的日志，请尽快补全和修改。");
		}
//		model.addAttribute("time", time);
		Map<String, Object> config = configService.getConfig();
		int score2 = Integer.parseInt(config.get("benchmark").toString());
		int	score3 = Integer.parseInt(config.get("undulate").toString());
		model.addAttribute("scoreAll", score2+score3);

		String configBeginTime =config.get("begin_time").toString()+" 00:00:00";
		String configEndTime =config.get("end_time").toString()+" 23:59:59";

		if (CoreUtils.formatTimestamp(configBeginTime)>CoreUtils.getNowTimestamp()) {
			model.addAttribute("fill", "系统检测,还没到开工日期,填写的日志不会应用到当前生产中显示");
		}
		if (CoreUtils.formatTimestamp(configEndTime)<CoreUtils.getNowTimestamp()&&time.size()>0) {
			model.addAttribute("fill", "系统检测,日期已经超过工期最后一天,请将未填完的日志填完。");
		}
		return "safeLog/add";
	}
	@RequestMapping(value="/selectTime",method=RequestMethod.POST)
	public void selectTime(HttpServletRequest request,HttpServletResponse response) {
		ajaxReturn(response,logService.getTime());
	}
	/**
	 * 添加日志
	 * @param ids
	 * @param templateId
	 * @throws Exception 
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response,String[] ids,Integer logId) {
		List<Map<String, Object>> list = logService.getTime();
		if (list.size()==0) {
			ajaxReturn(response,null, "到今天为止,所有日志已全部填写完成,请不要再提交日志。", 0);
			return ;
		}
		if(request.getParameter("time")==""||request.getParameter("time")==null){
			ajaxReturn(response,null, "请先选择提交日志的时间", 0);
			return ;
		}
		Integer score = this.checkAndScore(request,response,ids);
		if (score==null) {
			return ;
		}
		Map<String, Object> data = configService.getApiConfig();
		int templateId = Integer.valueOf(data.get("templateId").toString());
		int flag = 0;
		String infoString = "增加成功~";
		if (logId!=null) {
			flag = logService.update(logId, score);
			infoString = "更新成功~";
		}else {
			String time = request.getParameter("time")+CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp()," HH:mm:ss");
			Integer addTime =CoreUtils.getNowTimestamp();
			if (request.getParameter("addTime")!=null&&request.getParameter("addTime")!="") {
				addTime = Integer.valueOf(request.getParameter("addTime"));
			}
			flag = logService.add(templateId,score,time,addTime);
			logId = flag;
			if (flag>0) {
				contactSaveLogService.delNow();
			}
		}
		if(flag > 0){
			for(String id:ids){
				if(request.getParameter("contact_"+id)!=null){
					String attr = request.getParameter("contact_"+id);
					attr = attr.trim();
					contactLogService.add(id,logId,attr);
				}
			}
			ajaxReturn(response,null, infoString, 1);
		}else if(flag==-2){
			ajaxReturn(response,null, request.getParameter("time")+"已添加过日志,不能再添加", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 获取日志分值
	 * @param model
	 */
	@RequestMapping(value="/score",method=RequestMethod.GET)
	public void score(HttpServletRequest request,HttpServletResponse response,Model model){
		List<Map<String, Object>> scoreList = logService.getPreviewLog();
		String time = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(),"yyyy-MM-dd")+" 23:59:59";
		//昨天的day
		int day = logService.dates((int)CoreUtils.formatTimestamp(time))-1;
		while (day>0&&Integer.parseInt(scoreList.get(day-1).get("id").toString())==0) {
			scoreList.remove(day-1);
			day--;
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("targetPoint", scoreList);
		ajaxReturn(response,data,"",1);
		return ;
	}
	/**
	 * 获取日志分值2
	 * @param model
	 */
	@RequestMapping(value="/score2",method=RequestMethod.GET)
	public void score2(HttpServletRequest request,HttpServletResponse response,Model model,int logId){
		//日志分值
		Log log = logService.getLogById(logId);
		int addTime = log.getAddTime();
		List<Map<String, Object>> logList = logService.getApiLog(addTime,null);
		String time = CoreUtils.formatTimestamp(addTime,"yyyy-MM-dd")+" 23:59:59";
		int day = logService.dates((int)CoreUtils.formatTimestamp(time));
		while (day>0&&Integer.parseInt(logList.get(day-1).get("id").toString())==0) {
			logList.remove(day-1);
			day--;
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("targetPoint", logList);
		ajaxReturn(response,data,"",1);
		return ;
	}


	/**
	 * 显示日志模板信息
	 * @param model
	 * @param id
	 * @return 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/data",method=RequestMethod.GET)
	public String data(HttpServletRequest request,HttpServletResponse response,Model model,int id) throws Exception{
		List<Map<String, Object>> pidCateList = contactLogService.contactLog(id);
		model.addAttribute("serviceTime",request.getParameter("addTime"));
		model.addAttribute("tempList", pidCateList);
		model.addAttribute("logId",id);
		return "safeLog/data";
	}
	@RequestMapping(value="/data2",method=RequestMethod.GET)
	public String data2(HttpServletRequest request,HttpServletResponse response,Model model,int id) throws Exception{
		List<Map<String, Object>> pidCateList = logHistoryService.contactLogHistory(id);
		model.addAttribute("serviceTime",request.getParameter("addTime"));
		model.addAttribute("tempList", pidCateList);
		return "safeLog/data";
	}

	/**
	 * 日志审核
	 * @param model
	 * @param id
	 */
	@RequestMapping(value="/right",method=RequestMethod.POST)
	public void right(HttpServletRequest request,HttpServletResponse response,Model model,int id){
		int flag = logService.statusRight(id);
		if (flag>0) {
			logHistoryService.add(id);
			ajaxReturn(response,null, "审核通过~", 1);
		}else if (flag==-1) {
			ajaxReturn(response,null, "日志已被驳回,请重新填写", 0);
		}else if (flag==-2) {
			ajaxReturn(response,null, "日志已通过审核,请不要重复审核", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}

	}

	/**
	 * 日志驳回
	 * @param id
	 * @param reason
	 */
	@RequestMapping(value="/reject",method=RequestMethod.POST)
	public void reject(HttpServletRequest request,HttpServletResponse response,int id,String reason) {
		User userInfo = (User)request.getSession().getAttribute(Constants.USER_INFO);
		int flag = logService.statusReject(id);
		if (flag>0) {
			logReasonService.add(id,reason,userInfo.getAccount());
			ajaxReturn(response,null, "驳回成功~", 1);
		}else if (flag==-1) {
			ajaxReturn(response,null, "日志已被驳回,请重新填写", 0);
		}else if (flag==-2) {
			ajaxReturn(response,null, "日志已通过审核,不能驳回", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
	}

	

	/**
	 * 日志驳回理由列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/reason",method=RequestMethod.GET)
	public String reason(HttpServletRequest request,HttpServletResponse response,Model model){
		int  logId = Integer.parseInt(request.getParameter("id"));
		Log log = logService.getLogById(logId);
		List<Map<String, Object>> list = logReasonService.getReasonById(logId);
		model.addAttribute("reason",list);
		model.addAttribute("time",CoreUtils.formatTimestamp(log.getAddTime(), "yyyy-MM-dd"));
		return "safeLog/reason";

	}
	/**
	 * 导出填写完整的日志
	 * @param model
	 * @param ids
	 * @throws Exception
	 */
	@RequestMapping(value="/export",method=RequestMethod.POST)
	public void export(HttpServletRequest request,HttpServletResponse response,Model model,String[] ids,Integer addTime) throws Exception{
		
		Integer isOut = this.checkAndScore(request,response,ids);
		if (isOut==null) {
			return ;
		}
		List<Map<String , Object >> list = new ArrayList<Map<String,Object>>();
		for(String id:ids){
			Map<String, Object> map = new HashMap<String, Object>();
			String value = request.getParameter("contact_"+id);
			map.put("contactId", id);
			map.put("value", value);
			list.add(map);
		}
		String flag = logService.exportLog(list,null,addTime);
		flag= UploadUtils.serverUrl(request, flag);
		if (!flag.startsWith(Constants.ERROR_FLAG)) {
			ajaxReturn(response,flag, "导出成功~", 1);
		}else{
			ajaxReturn(response,null, "导出失败，文件创建失败，请确保服务器文件仓库目录下存在log目录，并设置成777权限，错误详情"+flag, 0);
		}

	}
	
	private Integer checkAndScore(HttpServletRequest request,HttpServletResponse response,String[] ids){
		double scoreDouble = 0;
		int noValue = 0;
		String contactNumer = "[";
		for(String id:ids){
			String value = request.getParameter("contact_"+id);
			Contact contact = contactService.findById(Integer.parseInt(id));
			int flag = contact.getIsSelect();
			int tpye = contact.getType();
			if ((flag==1&&value==null)||(flag==1&&value=="")) {
				contactNumer =contactNumer+contact.getNumber()+",";
				noValue=1;
			}
			if(tpye==1&&value!=null){
				int attrId = Integer.parseInt(value);
				scoreDouble = scoreDouble + attributeService.getScore(attrId);
			}
		}
		contactNumer = contactNumer.substring(0, contactNumer.length()-1);
		contactNumer+="]";
		if(noValue==1){
			ajaxReturn(response,null, "以下触点还没有完成："+contactNumer, 0);
			return null;
		}
		scoreDouble = scoreDouble/10;
		int score = (int)Math.round(scoreDouble);
		return score;
	}
	
	/**
	 * 导出通过审核的日志
	 * @param model
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value="/exportLog",method=RequestMethod.POST)
	public void exportLog(HttpServletRequest request,HttpServletResponse response,Model model,int id) throws Exception{
		List<Map<String, Object>> val = contactLogService.getValueByLogId(id);
		String flag = logService.exportLog(val,id,null);
		flag= UploadUtils.serverUrl(request, flag);
		if (!flag.startsWith(Constants.ERROR_FLAG)) {
			ajaxReturn(response,flag, "导出成功~", 1);
		}else{
			ajaxReturn(response,null, "导出失败，文件创建失败，请确保服务器文件仓库目录下存在log目录，并设置成777权限，错误详情"+flag, 0);
		}
	}
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){
		return "safeLog/list";

	}
	@RequestMapping(value="/list2",method=RequestMethod.GET)
	public String list2(HttpServletRequest request,HttpServletResponse response,Model model){
		return "safeLog/list2";
		
	}
	/**
	 * 预览2
	 * @param request
	 * @param response
	 * @param model
	 * @param logId
	 * @return
	 */
	@RequestMapping(value="/preview2")
	public String preview2(HttpServletRequest request,HttpServletResponse response,Model model,int logId){
		//基本配置信息
		model.addAttribute("logId", logId);
		Map<String, Object> data = configService.getApiConfig();
		String appearNumber = data.get("appear_number").toString();

		String policy = data.get("policy").toString();  //相对路径是也可以使用图片  未完成
		List<String> listImgSrc = CoreUtils.getImgSrc(policy);
		for (String string : listImgSrc) {//替换所有图片src 
			policy = policy.replace(string,CoreUtils.serverUrl(request, string));
		}
		//		data.put("policy", policy);
		model.addAttribute("config",data);
		//服务器当前时间
		model.addAttribute("serviceTime",CoreUtils.getNowTimestamp());
		//		data.put("serverTime", CoreUtils.getNowTimestamp());
		//布告栏
		//		data.put("board",noticeBoardService.getApiNoticeBoard());
		List<Map<String, Object>> notice = noticeBoardService.getApiNoticeBoard();
		model.addAttribute("notice",notice);

		//今天或者昨天的主要原因5条
		List<Map<String, Object>> cause = contactLogService.getApiContactLog(logId);;
		//		Integer logId=logService.getApiLogId();
		if(cause!=null)
			for (Map<String, Object> map : cause) {
				if(map.get("attributeMeasures").equals(""))
					map.put("attributeMeasures", "无");
			}
		//		data.put("cause", cause);
		model.addAttribute("cause",cause);
		//前7天的高频度主要原因
		//		data.put("oldCause", oldCause);

		List<Map<String, Object>> logList=logService.getApiOldLogList(logId);
		List<Map<String, Object>> oldCause = contactLogService.getApiOldContactLog(logList,appearNumber);
		if (oldCause!=null) 
			for (Map<String, Object> map : oldCause) {
				if(map.get("attributeMeasures").equals(""))
					map.put("attributeMeasures", "无");
			}
		model.addAttribute("oldCause",oldCause);
		//开始时间
		//		data.put("startTime", logService.getStartDate());
		String startDate = logService.getStartDate();
		startDate = startDate.substring(0, startDate.indexOf(" "));
		model.addAttribute("startDate",startDate);

		//		ajaxReturn(response,data);
		return "safeLog/preview2";
	}
	/**
	 * 预览
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/preview",method=RequestMethod.GET)
	public String preview(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> config = configService.getConfig();
		String appearNumber = config.get("appear_number").toString();
		String policy = config.get("policy").toString();
		List<String> listImgSrc = CoreUtils.getImgSrc(policy);
		for (String string : listImgSrc) {//替换所有图片src 
			policy = policy.replace(string,CoreUtils.serverUrl(request, string));
		}
		config.put("policy", policy);

		model.addAttribute("config",config);
		model.addAttribute("serviceTime",CoreUtils.getNowTimestamp());
		List<Map<String, Object>> notice = noticeBoardService.getApiNoticeBoard();
		model.addAttribute("notice",notice);

		List<Map<String, Object>> logList=logService.getApiOldLogList(null);
		List<Map<String, Object>> oldCause = contactLogService.getApiOldContactLog(logList,appearNumber);
		if (oldCause!=null) 
			for (Map<String, Object> map : oldCause) {
				if(map.get("attributeMeasures").equals(""))
					map.put("attributeMeasures", "无");
			}
		model.addAttribute("oldCause",oldCause);
		String startDate = logService.getStartDate();
		startDate = startDate.substring(0, startDate.indexOf(" "));
		model.addAttribute("startDate",startDate);
		return "safeLog/preview";
	}
	
}
