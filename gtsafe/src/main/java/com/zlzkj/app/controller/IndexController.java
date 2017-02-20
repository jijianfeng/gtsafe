package com.zlzkj.app.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zlzkj.app.model.Role;
import com.zlzkj.app.model.User;
import com.zlzkj.app.service.NodeService;
import com.zlzkj.app.service.RoleService;
import com.zlzkj.app.service.UserRoleService;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.controller.CoreController;

/**
 * 首页控制器
 */
@Controller

public class IndexController extends CoreController{
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private NodeService nodeService;
	@RequestMapping(value={"/"})
	public String index(HttpServletRequest request,HttpServletResponse response,Model model) {
		User user = (User) request.getSession().getAttribute(Constants.USER_INFO);
		int roleId = userRoleService.findRoleIdByUserId(user.getId());
		Role role = roleService.findById(roleId);
		model.addAttribute("name",user);
		model.addAttribute("role",role);
		
		List<Map<String, Object>> list = nodeService.leftNodeList(roleId);
//		System.out.println(list);
		model.addAttribute("node",list);
		return "index/index";
	}
	
	@RequestMapping(value="/home")
	public String home(HttpServletRequest request,HttpServletResponse response,Model model){
//		//基本配置信息
//		Map<String, Object> data = configService.getApiConfig();
//		//服务器当前时间
//		data.put("serverTime", CoreUtils.getNowTimestamp());
//		//布告栏
//		data.put("board",noticeBoardService.getApiNoticeBoard());
//		data.put("targetPoint", logService.getApiLog());
//		data.put("cause", contactService.getApiContact());
//		model.addAttribute("data",data);
		return "index/home";
	}
}
