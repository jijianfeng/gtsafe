package com.zlzkj.app.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zlzkj.app.model.*;
import com.zlzkj.app.service.*;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.*;

@Controller
@RequestMapping(value="/field")
public class FieldController extends CoreController{
	@Autowired
	private FieldService fieldService;
	
	/**
	 * 工地列表
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public void list(HttpServletRequest request,HttpServletResponse response){	
			List<Map<String, Object>> fieldList = fieldService.getFieldList();
		    ajaxReturn(response,fieldList);
	}
	
	/**
	 * 新增项目或工地
	 * @param contactCate
	 */
	@RequestMapping(value="/saveField",method=RequestMethod.POST)
	public void saveField(HttpServletRequest request,HttpServletResponse response,Field field){
		int flag = fieldService.addField(field);
		if(flag > 0){
			ajaxReturn(response,null, "工地添加成功~", 1);
		}else if(flag==-1){
			ajaxReturn(response,null, "工地编号已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	
	@RequestMapping(value="/addFieldContents",method=RequestMethod.POST)
	public void addFieldContents(HttpServletRequest request,HttpServletResponse response,int level){
		List<Map<String, Object>> AllList  = new ArrayList<Map<String, Object>>();
		Map<String, Object> list  = new HashMap<String, Object>();
		list.put("text", "顶级项目");
		list.put("id", 0);
		List<Map<String, Object>> listField = fieldService.getAllFieldList(level);
		list.put("children", listField);
		AllList.add(0,list);
		ajaxReturn(response,AllList);
	}
	
	/**
	 * 删除工地或者项目
	 * @param id
	 */
	@RequestMapping(value="/delField",method=RequestMethod.POST)
	public void delField(HttpServletRequest request,HttpServletResponse response,int id){
		int flag = fieldService.del(id);
		if(flag > 0){
			ajaxReturn(response,null, "删除成功~", 1);
		}else if(flag == -1){
			ajaxReturn(response,null, "该项目下还有子工地,请先删除子工地再删除顶级项目", 0);
		}else if (flag == 0) {
			//ajaxReturn(response,null, "该分类还有触点项,不能删除", 0);
		}else {
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;

	}
	
	/**
	 * 更新触点分类
	 * @param contactCate
	 */
	@RequestMapping(value="/editField",method=RequestMethod.POST)
	public void editField(HttpServletRequest request,HttpServletResponse response,Field field,int oldPidId) {
		field.setFieldName(CoreUtils.htmlEscape(field.getFieldName().toString().trim()));
		//int count = field.getCounts();
		if(oldPidId==0&&oldPidId!=field.getPid()){
			List<Map<String, Object>> noCate = fieldService.getField("status=1 and pid="+fieldService.topField(field.getId()));
			if (noCate!=null&&noCate.size()!=0) {
				ajaxReturn(response,null, "该项目下还有子工地,不能改动", 0);
				return;
			}
		}
		if (fieldService.topField(field.getId())==field.getPid()) {
			ajaxReturn(response,null, "分类不能成为自身的子分类", 0);
			return;
		}
		int flag = fieldService.update(field);
		if(flag > 0){
/*			contactCateService.delCount(oldPidId,count);
			contactCateService.addCount(contactCate.getPid(),count);*/
			ajaxReturn(response,null,"更新成功~",1);
		}else if(flag==-1){
			ajaxReturn(response,null,"工地或项目编号已存在",0);
		}else{
			ajaxReturn(response,null,"发生错误,请重试",0);
		}
		return ;
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String cate(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "field/list";
	}
	
	@RequestMapping(value="/saveField",method=RequestMethod.GET)
	public String saveCate(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "field/saveField";
	}
	
	@RequestMapping(value="/editField",method=RequestMethod.GET)
	public String editCate(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "field/saveField";
	}
}
