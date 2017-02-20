package com.zlzkj.app.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zlzkj.app.model.Node;
import com.zlzkj.app.model.Role;
import com.zlzkj.app.model.User;
import com.zlzkj.app.model.UserRole;
import com.zlzkj.app.service.NodeService;
import com.zlzkj.app.service.RoleNodeService;
import com.zlzkj.app.service.RoleService;
import com.zlzkj.app.service.UserRoleService;
import com.zlzkj.app.service.UserService;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CoreUtils;

/**
 * 人员控制器
 */
@Controller
@RequestMapping(value="/member")
public class MemberController extends CoreController{
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private RoleNodeService roleNodeService;
	/**
	 * 显示角色页面内容
	 * @param model
	 */
	@RequestMapping(value="/role",method=RequestMethod.POST)
	public void role(HttpServletRequest request,HttpServletResponse response,int page,int rows) {
		Map<String, Object> data = roleService.getRoleAll(page,rows);
		ajaxReturn(response,data);
		return ;
	}
	/**
	 * 显示角色页面
	 * @return
	 */
	@RequestMapping(value="/role")
	public String role(HttpServletRequest request,HttpServletResponse response,Model model) {

		return "member/role";
	}
	/**
	 * 新增角色
	 * @param role
	 */
	@RequestMapping(value="/saveRole",method=RequestMethod.POST)
	public void saveRole(HttpServletRequest request,HttpServletResponse response,Role role) {
		if (role.getName().toString().contains("\'")||role.getRemark().toString().contains("\'")){
			ajaxReturn(response,null, "有非法字符存在", 0);
			return;
		}
		int flag = roleService.add(role);
		if(flag > 0){
			ajaxReturn(response,null, "新增成功~", 1);
		}else if(flag==-1){
			ajaxReturn(response,null, "角色名已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 显示角色新增&编辑页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/saveRole")
	public String saveRole(HttpServletRequest request,HttpServletResponse response,Model model) {
		return "member/saveRole";
	}

	@RequestMapping(value="/editRole")
	public String editRole(HttpServletRequest request,HttpServletResponse response,Model model) {
		return "member/saveRole";
	}
	/**
	 * 角色编辑更新提交
	 * @param role
	 */
	@RequestMapping(value="/editRole",method=RequestMethod.POST)
	public void editRole(HttpServletRequest request,HttpServletResponse response,Role role) {
		if (role.getName().toString().contains("\'")||role.getRemark().toString().contains("\'")){
			ajaxReturn(response,null, "有非法字符存在", 0);
			return;
		}
		int flag = roleService.update(role);
		if(flag > 0){
			ajaxReturn(response,null,"更新成功~",1);
		}else if(flag==-1){
			ajaxReturn(response,null,"角色名已存在",0);
		}else{
			ajaxReturn(response,null,"发生错误,请重试",0);
		}
		return ;
	}
	/**
	 * 角色删除
	 * @param id
	 */
	@RequestMapping(value="/delRole",method=RequestMethod.POST)
	public void delRole(HttpServletRequest request,HttpServletResponse response,int id){
		if(userRoleService.isUserEmpty(id)){
			roleService.del(id);
			ajaxReturn(response,null, "删除成功~", 1);
		}
		ajaxReturn(response,null, "该角色下存在用户，不能删除~", 0);

	}
	/**
	 * 选择角色列表
	 */
	@RequestMapping(value="/roleList",method=RequestMethod.POST)
	public void roleList(HttpServletRequest request,HttpServletResponse response) {
		List<Map<String, Object>> data = roleService.getRoleAll();
		ajaxReturn(response,data);
	}
	/**
	 * 显示用户列表
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public void user(HttpServletRequest request,HttpServletResponse response,int page,int rows) {
		Map<String, Object> data = userService.getUserRoleFieldAll(page,rows);
		ajaxReturn(response,data);
		return ;
	}
	/**
	 * 显示用户列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/user")
	public String user(HttpServletRequest request,HttpServletResponse response,Model model){
		return "member/user";

	}
	/**
	 * 新增用户
	 * @param user
	 * @param role
	 */
	@RequestMapping(value="/saveUser",method=RequestMethod.POST)
	public void saveUser(HttpServletRequest request,HttpServletResponse response,User user,int role) {
		if (user.getName().toString().contains("\'")||user.getRemark().toString().contains("\'")||user.getAccount().toString().contains("\'")){
			ajaxReturn(response,null, "有非法字符存在", 0);
			return;
		}
		int flag = userService.add(user);
		if(flag > 0){
			userRoleService.add(new UserRole(flag,role,CoreUtils.getNowTimestamp()));
			ajaxReturn(response,null, "新增成功~", 1);
		}else if(flag==-1){
			ajaxReturn(response,null, "用户名已存在", 0);
		}else if(flag==-2){
			ajaxReturn(response,null, "手机号已存在", 0);
		}else if(flag==-3){
			ajaxReturn(response,null, "用户编号已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
		return ;
	}
	/**
	 * 显示新增&编辑页面
	 * @return
	 */
	@RequestMapping(value="/saveUser")
	public String saveUser(HttpServletRequest request,HttpServletResponse response) {
		return "member/saveUser";
	}
	@RequestMapping(value="/editUser")
	public String editUser(HttpServletRequest request,HttpServletResponse response) {
		return "member/editUser";
	}
	/**
	 * 提交用户更新
	 * @param user
	 * @param role
	 */
	@RequestMapping(value="/editUser",method=RequestMethod.POST)
	public void editUser(HttpServletRequest request,HttpServletResponse response,User user,int role) {
		if (user.getName().toString().contains("\'")||user.getRemark().toString().contains("\'")||user.getAccount().toString().contains("\'")){
			ajaxReturn(response,null, "有非法字符存在", 0);
			return;
		}
		int flag = userService.update(user);
		if(flag > 0){
			UserRole userRole = new UserRole();
			userRole.setId(userRoleService.findIdByUserId(user.getId()));
			userRole.setRoleId(role);
			userRole.setUserId(user.getId());
			userRoleService.update(userRole);
			ajaxReturn(response,null, "更新成功~", 1);
		}else if(flag==-1){
			ajaxReturn(response,null, "用户名已存在", 0);
		}else if(flag==-2){
			ajaxReturn(response,null, "手机号已存在", 0);
		}else{
			ajaxReturn(response,null, "发生错误,请重试", 0);
		}
	}
	/**
	 * 删除用户
	 * @param id
	 */
	@RequestMapping(value="/delUser",method=RequestMethod.POST)
	public void delUser(HttpServletRequest request,HttpServletResponse response,int id){
		userService.del(id);
		int userRoleId = userRoleService.findIdByUserId(id);
		userRoleService.del(userRoleId);
		ajaxReturn(response,null, "删除成功~", 1);

	}
	@RequestMapping(value="saveNode")
	public String saveNode(HttpServletRequest request,HttpServletResponse response,Model model){
		model.addAttribute("nodeList",nodeService.getNodeList());
		return "member/saveNode";
	}

	@RequestMapping(value="saveNode",method=RequestMethod.POST)
	public void saveNode(HttpServletRequest request,HttpServletResponse response,Node node){
		node.setAddTime(CoreUtils.getNowTimestamp());
		node.setOrderId(1);
		int flag = nodeService.add(node);
		if(flag>0){
			ajaxReturn(response,false,"保存成功!",1);
		}else{
			ajaxReturn(response,false,"保存失败!",0);
		}
	}
	@RequestMapping(value="accredit")
	public String accredit(HttpServletRequest request,HttpServletResponse response,Model model,Integer id){
		model.addAttribute("nodeList",nodeService.getRoleNodeList(id));
		model.addAttribute("roleId",id);
		int[] str = new int[]{1,2,3};
		model.addAttribute("str",str);
		return "member/accredit";
	}
	@RequestMapping(value="/accredit",method=RequestMethod.POST)
	public void accredit(HttpServletRequest request,HttpServletResponse response,int roleId,String[] nodeId){
		if (nodeId==null) {
			ajaxReturn(response,false,"保存成功",1);
			return;
		}
		roleNodeService.add(roleId,nodeId);
		ajaxReturn(response,false,"保存成功!",1);
	}
	
	@RequestMapping(value="editPassword")
	public String editPassword(HttpServletRequest request,HttpServletResponse response,Model model){
		
		return "index/editPassword";
	}
	@RequestMapping(value="editPassword",method=RequestMethod.POST)
	public void editPassword(HttpServletRequest request,HttpServletResponse response,Model model
			,String oldPassword,String newPassword){
		User user = (User) request.getSession().getAttribute(Constants.USER_INFO);
		oldPassword = DigestUtils.md5Hex(Constants.PASSWORD_SALT+DigestUtils.md5Hex(oldPassword));
		String passString = user.getPassword();
		if (oldPassword.equals(passString)) {
			user.setPassword(DigestUtils.md5Hex(Constants.PASSWORD_SALT+DigestUtils.md5Hex(newPassword)));
			userService.updatePass(user);
			ajaxReturn(response,false,"修改成功！",0);
		}else {
			ajaxReturn(response,false,"原密码不正确，请确认后在输入！",0);
		}
	}
}
