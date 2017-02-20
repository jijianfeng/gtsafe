package com.zlzkj.app.controller;


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zlzkj.app.model.Backup;
import com.zlzkj.app.model.Config;
import com.zlzkj.app.service.BackupService;
import com.zlzkj.app.service.ConfigService;
import com.zlzkj.app.service.TemplateService;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.DBUtils;
import com.zlzkj.core.utils.UploadUtils;

/**
 * 系统控制器
 */
@Controller
@RequestMapping(value="/system")
public class SystemController extends CoreController{

	@Autowired
	private ConfigService configService;

	@Autowired
	private BackupService backupService;
	@Autowired
	private TemplateService templateService;
	/**
	 * 获取配置信息&显示页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/info")
	public String info(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String, Object> config = configService.getConfig();
		List<Map<String, Object>> tempNameList = templateService.getTemplateNameList();
		//System.out.println(config);
		model.addAttribute("tempName",tempNameList);
		model.addAttribute("config",config);
		return "system/info";
	}
	/**
	 * 提交
	 * @param request
	 * @param response
	 * @param config
	 * @param templateId
	 * @param benchmark
	 * @param undulate
	 * @param begin_time
	 * @param end_time
	 * @param policy
	 * @param message
	 * @param chart_day
	 * @param appear_number
	 * @param show_day
	 * @param auto_time
	 */
	@RequestMapping(value="/saveInfo",method=RequestMethod.POST)
	public void saveInfo(HttpServletRequest request,HttpServletResponse response,Config config
			,String templateId,String benchmark,String undulate,String begin_time
			,String end_time,String policy,String message,String chart_day
			,String appear_number,String show_day,String auto_time){

		config.setName("templateId");
		config.setValue(templateId);
		int r0 = configService.updateConfig(config);

		config.setName("benchmark");
		config.setValue(benchmark);
		int r1 = configService.updateConfig(config);

		config.setName("undulate");
		config.setValue(undulate);
		int r2 = configService.updateConfig(config);

		config.setName("begin_time");
		config.setValue(Long.toString(CoreUtils.formatTimestamp(begin_time, "yyyy-MM-dd")));
		int r3 = configService.updateConfig(config);

		config.setName("end_time");
		config.setValue(Long.toString(CoreUtils.formatTimestamp(end_time, "yyyy-MM-dd")));
		int r4 = configService.updateConfig(config);

		config.setName("policy");
		config.setValue(CoreUtils.htmlEscape(policy));
		int r5 = configService.updateConfig(config);

		config.setName("message");
		config.setValue(message);
		int r6 = configService.updateConfig(config);

		config.setName("chart_day");
		config.setValue(chart_day);
		int r7 = configService.updateConfig(config);
		
		config.setName("show_day");
		config.setValue(show_day);
		int r8 = configService.updateConfig(config);
		
		config.setName("appear_number");
		config.setValue(appear_number);
		int r9 = configService.updateConfig(config);
		

		if(r1 >0 && r2 >0 && r3 >0 && r4 >0 && r5 >0 && r6 >0 && r7 >0 && r8>0 && r9>0 && r0 >0){
			ajaxReturn(response,null, "更新成功~", 1); 
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}

	}
	/**
	 * 备份数据库
	 * @param backup
	 */
	@RequestMapping(value="/saveBackup",method=RequestMethod.POST)
	public void saveBackup(HttpServletRequest request,HttpServletResponse response,Backup backup) {
		String saveName = "";
		String gzipSize = "";
		Map<String,Object> info = DBUtils.backupDBToSql("gtsafe",UploadUtils.getFileRepository());
		if((Integer)info.get("status")>0){ //备份成功
			saveName = info.get("saveName").toString();
			gzipSize = info.get("gzipSize").toString();
		}else{ //备份出错
			ajaxReturn(response,null,info.get("errorMsg").toString(),0);
			return ;
		}
		backup.setFileName(saveName);
		backup.setSize(gzipSize);
		backup.setNumber(1);
		backup.setFormat("GZ");

		int flag = backupService.add(backup);
		if(flag > 0){
			ajaxReturn(response,null, "备份成功~", 1);
		}else if(flag == -1){
			ajaxReturn(response,null, "备份文件名已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 显示备份数据页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/backup")
	public String backup(HttpServletRequest request,HttpServletResponse response,Model model) {
		return "system/backup";
	}
	/**
	 * 获取备份信息列表
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/backup",method=RequestMethod.POST)
	public void backup(HttpServletRequest request,HttpServletResponse response,int page,int rows) {
		Map<String, Object> backupList = backupService.getBackupList(page, rows);
		ajaxReturn(response,backupList);
	}
	/**
	 * 删除备份
	 * @param id
	 */
	@RequestMapping(value="/delBackup",method=RequestMethod.POST)
	public void delBackup(HttpServletRequest request,HttpServletResponse response,int id) {
		backupService.del(id);
		ajaxReturn(response,null, "删除成功~", 1);
	}

	@RequestMapping(value="/node")
	public String node(HttpServletRequest request,HttpServletResponse response,Model model) {
		return "system/node";
	}
	
	@RequestMapping(value="/editorUpload",method=RequestMethod.POST)
	public void editorUpload(HttpServletRequest request,HttpServletResponse response,@RequestParam(value="imgFile",required=false) MultipartFile coverPicFile){

		String fileNameString = "";
		if(!coverPicFile.getContentType().contains("image")){
			ajaxReturn(response,null,"请上传图片!",0);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		if(coverPicFile!=null){
			Map<String,Object> info = UploadUtils.saveMultipartFile(coverPicFile);
			if((Integer)info.get("status")>0){ //上传完成
				fileNameString = info.get("saveName").toString();
				//使用绝对路径,需要配置,配置文件 upload.properties
				String url = UploadUtils.parseFileUrl(fileNameString);
				data.put("url",url) ;
				data.put("alt", "");
			}else{
				ajaxReturn(response,null,"上传失败!"+info.get("errorMsg"),0);
				return ;
			}
		}
		ajaxReturn(response,data,"上传成功!",1);
		return ;
	}
}
