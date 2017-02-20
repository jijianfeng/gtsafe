package com.zlzkj.app.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zlzkj.app.model.NoticeBoard;
import com.zlzkj.app.service.NoticeBoardService;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;

/**
 * 布告栏控制器
 */
@Controller
@RequestMapping(value="/notice")
public class NoticeController extends CoreController{
	
	@Autowired
	private NoticeBoardService noticeBoardService;
	/**
	 * 显示列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/notice")
	public String notice(HttpServletRequest request,HttpServletResponse response,Model model) {
		Map<String, Object> notice=noticeBoardService.getNotice();
		model.addAttribute("notice", notice);
		return "notice/notice";
	}
//	/**
//	 * 获取列表信息
//	 * @param page
//	 * @param rows
//	 */
//	@RequestMapping(value="/notice",method=RequestMethod.POST)
//	public void notice(int page,int rows) {
//		Map<String, Object> data = noticeBoardService.getNoticeBoardAll(page, rows);
//		ajaxReturn(response,data);
//		return ;
//	}
	/**
	 * 添加布告
	 * @param noticeBoard
	 */
	@RequestMapping(value="/saveNotice",method=RequestMethod.POST)
	public void saveNotice(HttpServletRequest request,HttpServletResponse response,NoticeBoard noticeBoard){
		noticeBoard.setContent(CoreUtils.htmlEscape(noticeBoard.getContent().toString()));
		int flag = noticeBoardService.update(noticeBoard);
		if(flag > 0){
			ajaxReturn(response,null, "更新成功~", 1);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
		
	}
//	/**
//	 * 显示添加&编辑页面
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value="/saveNotice")
//	public String saveNotice(Model model) {
//		return "notice/saveNotice";
//	}
//	@RequestMapping(value="/editNotice")
//	public String editNotice(Model model) {
//		return "notice/saveNotice";
//	}
//	/**
//	 * 提交编辑更新
//	 * @param noticeBoard
//	 */
//	@RequestMapping(value="/editNotice",method=RequestMethod.POST)
//	public void editNotice(NoticeBoard noticeBoard) {
//		noticeBoard.setContent(CoreUtils.htmlEscape(noticeBoard.getContent().toString()));
//		int flag = noticeBoardService.update(noticeBoard);
//		if(flag > 0){
//			ajaxReturn(response,null, "更新成功~", 1);
//		}else{
//			ajaxReturn(response,null, "发生错误,请重试", 0);
//		}
//		return ;
//	}
//	/**
//	 * 删除布告
//	 * @param id
//	 */
//	@RequestMapping(value="/delNotice",method=RequestMethod.POST)
//	public void delNotice(int id){
//		noticeBoardService.del(id);
//		ajaxReturn(response,null, "删除成功~", 1);		
//	}
}
