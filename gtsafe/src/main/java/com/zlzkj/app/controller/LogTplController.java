package com.zlzkj.app.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zlzkj.app.model.Template;
import com.zlzkj.app.service.TemplateContactService;
import com.zlzkj.app.service.TemplateService;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;

/**
 * 日志模板控制器
 */
@Controller
@RequestMapping(value="/logTpl")
public class LogTplController extends CoreController{
	@Autowired
	private TemplateService templateService;
	@Autowired
	private TemplateContactService templateContactService;

	/**
	 * 获取模板列表
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public void list(HttpServletRequest request,HttpServletResponse response,int page,int rows){
		Map<String, Object> List = templateService.getTemplateList(page, rows);
		ajaxReturn(response,List);
	}
	/**
	 * 获取所有触点项和触点分类
	 */
	@RequestMapping(value="/cateList",method=RequestMethod.POST)
	public void cateList(Model model,HttpServletRequest request,HttpServletResponse response,Integer templateId){
		List<Map<String, Object>> List = templateContactService.getTempContactList(templateId);
		ajaxReturn(response,List);
	}
	/**
	 * 添加模板和模板触点项关联
	 * @param template
	 * @param contactId
	 */
	@RequestMapping(value="/add" ,method=RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response,Template template,String[] idStrings) {
		template.setName(CoreUtils.htmlEscape(template.getName().toString()));
		template.setRemark(CoreUtils.htmlEscape(template.getRemark().toString()));
		int count = idStrings.length;
		if(count==0){
			ajaxReturn(response,null, "请选择触点项", 0);
		}else {
			template.setCounts(count);			
		}
		int flag = templateService.add(template);
		if(flag > 0){
			templateContactService.add(idStrings,flag);
			ajaxReturn(response,null, "模板增加成功~", 1);
		}else if(flag==-1){
			ajaxReturn(response,null, "模板已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 更新模板
	 * @param template
	 * @param idStrings
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response,Template template,String[] idStrings){
		template.setName(CoreUtils.htmlEscape(template.getName().toString()));
		template.setRemark(CoreUtils.htmlEscape(template.getRemark().toString()));
		int count = idStrings.length;
		if(count==0){
			ajaxReturn(response,null, "请选择触点项", 0);
		}else {
			template.setCounts(count);			
		}
		int flag = templateService.update(template);
		if(flag > 0){
			templateContactService.del(template.getId());
			templateContactService.add(idStrings, template.getId());
			ajaxReturn(response,null,"更新成功~",1);
		}else if(flag==-1){
			ajaxReturn(response,null, "模板已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}

	}

	/**
	 * 删除模板和模板触点项关联
	 * @param id
	 */
	@RequestMapping(value="/del" ,method=RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,int id){
		int flag = templateService.del(id);
		if (flag>0) {
			templateContactService.del(id);
			ajaxReturn(response,null, "删除成功~", 1);
		}else if (flag==-1) {
			ajaxReturn(response,null, "还有日志使用这个模板,不能删除", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}

	}



	@RequestMapping(value="/add")
	public String add(HttpServletRequest request,HttpServletResponse response,Model model,Integer templateId) {
		model.addAttribute("templateId", templateId);
		return "logTpl/add";
	}
	@RequestMapping(value="/edit")
	public String edit(HttpServletRequest request,HttpServletResponse response,Model model,Integer templateId) {
		model.addAttribute("templateId", templateId);
		return "logTpl/update";
	}

	@RequestMapping(value="/list")
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){

		return "logTpl/list";

	}

}
