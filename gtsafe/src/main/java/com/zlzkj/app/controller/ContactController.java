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

import com.zlzkj.app.model.Attribute;
import com.zlzkj.app.model.Contact;
import com.zlzkj.app.model.ContactCate;
import com.zlzkj.app.model.Template;
import com.zlzkj.app.model.TemplateContact;
import com.zlzkj.app.service.AttributeService;
import com.zlzkj.app.service.ContactAttributeService;
import com.zlzkj.app.service.ContactCateService;
import com.zlzkj.app.service.ContactService;
import com.zlzkj.app.service.TemplateContactService;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.UploadUtils;

/**
 * 触点控制器
 */
@Controller
@RequestMapping(value="/contact")
public class ContactController extends CoreController{

	@Autowired
	private ContactService contactService;
	@Autowired
	private ContactCateService contactCateService;
	@Autowired
	private AttributeService attributeService;
	@Autowired
	private ContactAttributeService contactAttributeService;
	@Autowired
	private TemplateContactService templateContactService;

	/**
	 * 获取触点项列表
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public void list(HttpServletRequest request,HttpServletResponse response,int page,int rows){
		int id = 0;
		if (request.getParameter("id")!=null) {
			id = Integer.parseInt(request.getParameter("id"));
		}
		Map<String, Object> contactList = contactService.getSearchList(page,rows,id);
		ajaxReturn(response,contactList);
	}

	/**
	 * 添加触点项
	 * @param contact
	 * @param contactCateId
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public void save(HttpServletRequest request,HttpServletResponse response,Contact contact) {
		contact.setName(CoreUtils.htmlEscape(contact.getName().toString().trim()));
		int flag = contactService.add(contact);
		if(flag > 0){
			contactCateService.addCount(contact.getContactCateId(),1);
			ajaxReturn(response,null, "触点项增加成功~", 1);
		}else if(flag==-1){
			ajaxReturn(response,null, "触点项编号已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 更新触点项
	 * @param Contact
	 */
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public void edit(HttpServletRequest request,HttpServletResponse response,Contact contact,int oldPidId) {
		contact.setName(CoreUtils.htmlEscape(contact.getName().toString().trim()));
		int oldId = contact.getId();
		int flag = contactService.update(contact);
		if(flag > 0){
			templateContactService.update(oldId,flag);
			contactCateService.delCount(oldPidId,1);
			contactCateService.addCount(contact.getContactCateId(),1);
			ajaxReturn(response,null,"更新成功~",1);
		}else if(flag==-1){
			ajaxReturn(response,null,"触点项编号已存在",0);
		}else{
			ajaxReturn(response,null,"发生错误,请重试",0);
		}
		return ;
	}

	/**
	 * 删除触点项
	 * @param id
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,int id,int contactCateId){
		contactService.del(id);
		contactCateService.delCount(contactCateId,1);
		ajaxReturn(response,null, "删除成功~", 1);
	}

	/**
	 * 触点分类列表
	 */
	@RequestMapping(value="/cate",method=RequestMethod.POST)
	public void cate(HttpServletRequest request,HttpServletResponse response){
		List<Map<String, Object>> cateList = contactCateService.getCateList();
		ajaxReturn(response,cateList);
	}
	/**
	 * 获取分类目录
	 * @param level
	 */
	@RequestMapping(value="/listContactCate",method=RequestMethod.POST)
	public void listContactCate(HttpServletRequest request,HttpServletResponse response,int level){
		List<Map<String, Object>> list = contactCateService.getAllCateList(level);
		ajaxReturn(response,list);
	}

	@RequestMapping(value="/addCateContents",method=RequestMethod.POST)
	public void addCateContents(HttpServletRequest request,HttpServletResponse response,int level){
		List<Map<String, Object>> AllList  = new ArrayList<Map<String, Object>>();
		Map<String, Object> list  = new HashMap<String, Object>();
		list.put("text", "顶级分类");
		list.put("id", 0);
		List<Map<String, Object>> listContactCate = contactCateService.getAllCateList(level);
		list.put("children", listContactCate);
		AllList.add(0,list);
		ajaxReturn(response,AllList);
	}

	@RequestMapping(value="/cateContents",method=RequestMethod.POST)
	public void cateContents(HttpServletRequest request,HttpServletResponse response,int level){
		List<Map<String, Object>> AllList  = new ArrayList<Map<String, Object>>();
		Map<String, Object> list  = new HashMap<String, Object>();
		list.put("text", "全部分类");
		list.put("id", 0);
		List<Map<String, Object>> listContactCate = contactCateService.getAllCateList(level);
		list.put("children", listContactCate);
		AllList.add(0,list);
		ajaxReturn(response,AllList);
	}

	/**
	 * 新增触点分类
	 * @param contactCate
	 */
	@RequestMapping(value="/saveCate",method=RequestMethod.POST)
	public void saveCate(HttpServletRequest request,HttpServletResponse response,ContactCate contactCate){
		contactCate.setName(CoreUtils.htmlEscape(contactCate.getName().toString().trim()));
		Map<String, Object > where = new HashMap<String, Object>();
		where.put("contactCateId", contactCate.getPid());
		List<Map<String, Object>> noCate = contactService.haveContact(where);
		
		if (noCate!=null&&noCate.size()!=0) {
			ajaxReturn(response,null, "该分类下存在触点项,不能添加分类", 0);
			return;
		}
		int flag = contactCateService.addCate(contactCate);
		if(flag > 0){
			ajaxReturn(response,null, "触点分类添加成功~", 1);
		}else if(flag==-1){
			ajaxReturn(response,null, "触点分类编号已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 更新触点分类
	 * @param contactCate
	 */
	@RequestMapping(value="/editCate",method=RequestMethod.POST)
	public void editCate(HttpServletRequest request,HttpServletResponse response,ContactCate contactCate,int oldPidId) {
		contactCate.setName(CoreUtils.htmlEscape(contactCate.getName().toString().trim()));
		int count = contactCate.getCounts();
		if(oldPidId==0&&oldPidId!=contactCate.getPid()){
			List<Map<String, Object>> noCate = contactCateService.getCate("status=1 and pid="+contactCateService.topCate(contactCate.getId()));
			if (noCate!=null&&noCate.size()!=0) {
				ajaxReturn(response,null, "该分类下还有子分类,不能移动", 0);
				return;
			}
		}
		if (contactCateService.topCate(contactCate.getId())==contactCate.getPid()) {
			ajaxReturn(response,null, "分类不能成为自身的子分类", 0);
			return;
		}
		int flag = contactCateService.update(contactCate);
		if(flag > 0){
			contactCateService.delCount(oldPidId,count);
			contactCateService.addCount(contactCate.getPid(),count);
			ajaxReturn(response,null,"更新成功~",1);
		}else if(flag==-1){
			ajaxReturn(response,null,"触点分类编号已存在",0);
		}else{
			ajaxReturn(response,null,"发生错误,请重试",0);
		}
		return ;
	}
	/**
	 * 删除触点分类
	 * @param id
	 */
	@RequestMapping(value="/delCate",method=RequestMethod.POST)
	public void delCate(HttpServletRequest request,HttpServletResponse response,int id){
		int flag = contactCateService.del(id);
		if(flag > 0){
			ajaxReturn(response,null, "删除成功~", 1);
		}else if(flag == -1){
			ajaxReturn(response,null, "该分类下还有分类,不能删除", 0);
		}else if (flag == 0) {
			ajaxReturn(response,null, "该分类还有触点项,不能删除", 0);
		}else {
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;

	}
	/**
	 * 获取属性列表
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/attr",method=RequestMethod.POST)
	public void attr(HttpServletRequest request,HttpServletResponse response,int page,int rows,int id) {
		Map<String, Object> list = attributeService.getAttrList(page,rows,contactService.topContact(id));
		ajaxReturn(response,list);
	}
	
	
	/**
	 * 添加属性,成功后添加关联表
	 * @param contactAttribute
	 * @param attribute
	 * @param id
	 */
	@RequestMapping(value="/addAttr",method=RequestMethod.POST)
	public void addAttr(HttpServletRequest request,HttpServletResponse response,
			Attribute attribute,int id){
		//attribute.setName(CoreUtils.htmlEscape(attribute.getName().toString().trim()));
		//attribute.setMeasures(CoreUtils.htmlEscape(attribute.getMeasures().toString().trim()));
		int flag = attributeService.addAttr(attribute);
		if(flag > 0){
			contactAttributeService.add(flag,contactService.topContact(id));
			ajaxReturn(response,null, "添加成功~", 1);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	
	/**
	 * 编辑属性
	 * @param attribute
	 */
	@RequestMapping(value="/editAttr",method=RequestMethod.POST)
	public void editAttr(HttpServletRequest request,HttpServletResponse response,
			Attribute attribute){
		int flag = attributeService.update(attribute);
		if(flag > 0){
			ajaxReturn(response,null, "更新成功~", 1);
		}else {
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;

	}
	/**
	 * 删除属性和属性关联
	 * @param id
	 */
	@RequestMapping(value="/delAttr",method=RequestMethod.POST)
	public void delAttr(HttpServletRequest request,HttpServletResponse response,
			int id){
//		contactAttributeService.del(id);
		int flag = attributeService.del(id);
		if(flag > 0){
			ajaxReturn(response,null, "删除成功~", 1);
		}else {
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;

	}
	/**
	 * 导入excel
	 * @param File
	 * @throws Exception
	 */
	@RequestMapping(value="/contactImport",method=RequestMethod.POST)
	public void contactImport(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="excel",required=false) MultipartFile File) throws Exception {
		String fileNameString = "";
		String nameString =File.getOriginalFilename();
		if (!nameString.contains(".xlsx")) {
			ajaxReturn(response,null,"导入文件格式错误,请选择xlsx文件格式进行导入",1);
			return;
		}
		if(File!=null){
			Map<String,Object> info = UploadUtils.saveMultipartFile(File);
			if((Integer)info.get("status")>0){ //上传完成
				String url ="";
				fileNameString = info.get("saveName").toString();
				url = UploadUtils.getFullSavePath(fileNameString)+"/"+fileNameString;
				String flag = contactService.contactImport(url);
				if (flag=="") {
					ajaxReturn(response,null,"导入成功",1);
					return;
				}else {
					ajaxReturn(response,null,flag,0);
				}
				return;
			}
		}
		ajaxReturn(response,null,"请选择文件!",0);
		return ;
	}

	@RequestMapping(value="/cate",method=RequestMethod.GET)
	public String cate(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "contact/cate";
	}

	@RequestMapping(value="/attr",method=RequestMethod.GET)
	public String attr(HttpServletRequest request,HttpServletResponse response,Model model,int id) {
		model.addAttribute("id", id);
		return "contact/attr";

	}

	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){

		return "contact/list";
	}

	@RequestMapping(value="/saveCate",method=RequestMethod.GET)
	public String saveCate(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "contact/saveCate";
	}
	@RequestMapping(value="/editCate",method=RequestMethod.GET)
	public String editCate(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "contact/saveCate";
	}

	@RequestMapping(value="/save",method=RequestMethod.GET)
	public String save(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "contact/save";

	}
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "contact/save";

	}

	@RequestMapping(value="/contactImport",method=RequestMethod.GET)
	public String contactImport(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {

		return "contact/import";
	}
	@RequestMapping(value="/addAttr")
	public String addAttr(HttpServletRequest request,HttpServletResponse response,
			Model model,int id){
		model.addAttribute("id", id);
		return "contact/addAttr";
	}
	@RequestMapping(value="/editAttr")
	public String editAttr(HttpServletRequest request,HttpServletResponse response,
			Model model){
		return "contact/editAttr";
	}
}
