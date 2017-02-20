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

import com.zlzkj.app.model.Docs;
import com.zlzkj.app.model.DocsCate;
import com.zlzkj.app.service.DocsCateService;
import com.zlzkj.app.service.DocsService;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.UIUtils;
import com.zlzkj.core.utils.UploadUtils;

/**
 * 知识库控制器
 */
@Controller
@RequestMapping(value="/repository")
public class RepositoryController extends CoreController{

	@Autowired
	private DocsCateService docsCateService;
	@Autowired
	private DocsService docsService;
	/**
	 * 获取分类列表
	 */
	@RequestMapping(value="/cateList",method=RequestMethod.GET)
	public void cateList(HttpServletRequest request,HttpServletResponse response,int level,Integer top) {
		List<Map<String, Object>> data = docsCateService.getCateList(level);
		if (top!=null) {
			Map<String, Object> cateMap = new HashMap<String, Object>();
			cateMap.put("id", 0);
			cateMap.put("text", "全部分类");
			data.add(0, cateMap);
			ajaxReturn(response,data);
		}else {
			ajaxReturn(response,data);
		}


	}
	/**
	 * 新增分类
	 * @param docsCate
	 */
	@RequestMapping(value="/saveCate",method=RequestMethod.POST)
	public void saveCate(HttpServletRequest request,HttpServletResponse response,DocsCate docsCate){
		int flag = docsCateService.add(docsCate);
		if(flag > 0){
			ajaxReturn(response,null, "新增成功~", 1);
		}else if(flag == -1){
			ajaxReturn(response,null, "分类名已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;

	}
	/**
	 * 显示新增&编辑页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/saveCate")
	public String saveCate(HttpServletRequest request,HttpServletResponse response,Model model){

		return "repository/saveCate";

	}
	@RequestMapping(value="/editCate")
	public String editCate(HttpServletRequest request,HttpServletResponse response,Model model){

		return "repository/saveCate";

	}
	/**
	 * 获取所有分类列表信息
	 */
	@RequestMapping(value="/cate",method=RequestMethod.POST)
	public void cate(HttpServletRequest request,HttpServletResponse response) {
		List<Map<String, Object>> data = docsCateService.getCateAll();
		ajaxReturn(response,data);
		return ;
	}
	/**
	 * 显示所有分类列表信息页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/cate")
	public String cate(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "repository/cate";
	}
	/**
	 * 提交分类更新
	 * @param docsCate
	 */
	@RequestMapping(value="/editCate",method=RequestMethod.POST)
	public void editCate(HttpServletRequest request,HttpServletResponse response,DocsCate docsCate) {
		int flag = docsCateService.update(docsCate);
		if(flag > 0){
			ajaxReturn(response,null,"更新成功~",1);
		}else if(flag==-1){
			ajaxReturn(response,null, "分类名已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 删除分类
	 * @param id
	 */
	@RequestMapping(value="/delCate",method=RequestMethod.POST)
	public void delCate(HttpServletRequest request,HttpServletResponse response,int id) {
		Map<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("pid", id);
		List<Map<String, Object>> list= docsCateService.getCate(whereMap);
		if (list.size()>0) {
			ajaxReturn(response,null, "该分类下存在分类，不能删除~", 1);
		}
		if(docsCateService.isLibEmpty(id)){
			docsCateService.del(id);
			ajaxReturn(response,null, "删除成功~", 1);
		}else {
			ajaxReturn(response,null, "该分类下存在文档，不能删除~", 0);
		}

	}
	/**
	 * 显示文档列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/lib")
	public String lib(HttpServletRequest request,HttpServletResponse response,Model model){
		return "repository/lib";
	}
	/**
	 * 获取文档列表
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/lib",method=RequestMethod.POST)
	public void lib(HttpServletRequest request,HttpServletResponse response,int page,int rows){
		String cateId = request.getParameter("cateId");
		Map<String, Object> where = new HashMap<String, Object>();
		if (cateId!=null&&!cateId.equals("0")) {
			String ids = docsCateService.getCateIds(Integer.valueOf(cateId));
			where.put("_string", "docsCateId IN ("+ids+")");
		}
		Map<String, Object> docsList = docsService.getDocsList(where,UIUtils.getPageParams(request));
		ajaxReturn(response,docsList);
	}
	/**
	 * 显示新增&编辑文档页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/saveLib")
	public String saveLib(HttpServletRequest request,HttpServletResponse response,Model model){
		return "repository/saveLib";
	}
	@RequestMapping(value="/editLib")
	public String editLib(HttpServletRequest request,HttpServletResponse response,Model model){
		return "repository/saveLib";
	}
	/**
	 * 保存文档信息
	 * @param docs
	 * @param coverPicFile
	 */
	@RequestMapping(value="/saveLib",method=RequestMethod.POST)
	public void saveLib(HttpServletRequest request,HttpServletResponse response,Docs docs,@RequestParam("cover_pic") MultipartFile coverPicFile){
		docs.setBuildTime((int)CoreUtils.formatTimestamp(request.getParameter("build"), "yyyy-MM-dd"));
		String coverPic = ""; //图片保存名
		Map<String,Object> info = UploadUtils.saveMultipartFile(coverPicFile);
		if((Integer)info.get("status")>0){ //上传完成
			coverPic = info.get("saveName").toString();
		}else{ //上传出错
			ajaxReturn(response,null,info.get("errorMsg").toString(),0);
		}
		docs.setUserId((Integer)request.getSession().getAttribute(Constants.USER_ID));
		docs.setName(coverPic);
		int flag = docsService.add(docs);
		if(flag > 0){
			docsCateService.countPlus(docs.getDocsCateId());
			ajaxReturn(response,null, "新增成功~", 1);
		}else if(flag == -1){
			ajaxReturn(response,null, "文档名已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	@RequestMapping(value="/editLib",method=RequestMethod.POST)
	public void editLib(HttpServletRequest request,HttpServletResponse response,Docs docs,@RequestParam("cover_pic") MultipartFile coverPicFile){
		docs.setBuildTime((int)CoreUtils.formatTimestamp(request.getParameter("build"), "yyyy-MM-dd"));
		String coverPic = ""; //图片保存名
		Map<String,Object> info = UploadUtils.saveMultipartFile(coverPicFile);
		if((Integer)info.get("status")>0){ //上传完成
			coverPic = info.get("saveName").toString();
			docs.setName(coverPic);
		}else if (!info.get("status").toString().equals("-2")) {
			//上传出错
			ajaxReturn(response,null,info.get("errorMsg").toString(),0);
		}
		Docs docs2 = docsService.findById(docs.getId());
		int flag = docsService.update(docs);
		if(flag > 0){
			docsCateService.countMinus(docs2.getDocsCateId());
			docsCateService.countPlus(docs.getDocsCateId());
			ajaxReturn(response,null, "更新成功~", 1);
		}else if(flag == -1){
			ajaxReturn(response,null, "文档名已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 删除文档
	 * @param id
	 */
	@RequestMapping(value="/delLib",method=RequestMethod.POST)
	public void delLib(HttpServletRequest request,HttpServletResponse response,int id) {
		docsCateService.countMinus(docsService.findById(id).getDocsCateId());
		docsService.del(id);
		ajaxReturn(response,null, "删除成功~", 1);
	}

}
