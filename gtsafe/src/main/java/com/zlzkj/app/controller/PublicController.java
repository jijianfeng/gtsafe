package com.zlzkj.app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import SuperDog.Dog;
import SuperDog.DogStatus;

import com.zlzkj.app.model.User;
import com.zlzkj.app.service.UserService;
import com.zlzkj.core.utils.Utils;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.controller.CoreController;
@Controller
@RequestMapping(value="/public")
public class PublicController  extends CoreController{

	@Autowired
	private UserService userService;

	@RequestMapping(value="/login")
	public String login(HttpServletRequest request,HttpServletResponse response,Model model){
		//验证是否有登录,登录之后自动跳转
		//		if(request.getSession().getAttribute(Constants.USER_INFO)!=null){
		//			try{
		//				response.sendRedirect("/gtsafe");
		//			}catch(Exception e){
		//				
		//			}
		//		}
		return "public/login";
	}

	@RequestMapping(value = "/login",method=RequestMethod.POST)
	public void login(HttpServletRequest request,HttpServletResponse response,@RequestParam("account")String account,@RequestParam("password")String password){

		if(!Constants.IS_DEBUG){
			final String vendorCode = Constants.VENDOR_CODE;

			final String productScope = Constants.PRODUCT_SCOPE;

			Dog curDog = new Dog(Constants.FEATURE_ID);

			curDog.loginScope(productScope,vendorCode);
			int status = curDog.getLastError();

			Map<Integer,String> code = new HashMap<Integer, String>();
			code.put(DogStatus.DOG_NOT_FOUND, "未发现超级狗");
			code.put(DogStatus.DOG_INV_VCODE, "开发商代码无效");
			code.put(DogStatus.DOG_FEATURE_EXPIRED, "该特征许可已失效");
			code.put(DogStatus.DOG_TIME_ERR, "系统时间被篡改");

			if(status == DogStatus.DOG_STATUS_OK){
				curDog.logout();
			}else if(code.containsKey(status)) {
				ajaxReturn(response,null,code.get(status),0);
				return;
			}else {
				ajaxReturn(response,null,"验证超级狗错误,错误码:"+status,0);
				return;
			}
		}

		User user = userService.findByAccount(account);

		if(user != null && Utils.isPasswordMatch(password, user.getPassword())) {			
			request.getSession().setAttribute(Constants.USER_INFO, user);
			request.getSession().setAttribute(Constants.USER_ID, user.getId());
			//使用权限工具进行用户登录，登录成功后跳到shiro配置的successUrl中，与下面的return没什么关系！
			UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getPassword());
			token.setRememberMe(true);
			Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			ajaxReturn(response,null, "登录成功", 1);
			return;
		}else{
			ajaxReturn(response,null,"账号或密码错误",0);
		}
	}
	/**
	 * 退出
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public void logout(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().removeAttribute(Constants.USER_INFO);
		request.getSession().removeAttribute(Constants.USER_ID);
		SecurityUtils.getSubject().logout();
		ajaxReturn(response,null,"成功退出", 1);
	}

//	@RequestMapping(value = "/unauthor",method=RequestMethod.GET)
//	public ModelAndView unauthor(String method){
//		//model.addAttribute("info","{\"data\":null,\"info\":\"您没有权限\",\"status\":-2}");
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("info","您没有权限");
//		mv.setViewName("public/unauthor");
//		ajaxReturn(response,null,"您没有权限",-2);
//		System.out.println(method);
//		return mv;
//	}
	/**
	 * 定时验证超级狗
	 */
	@RequestMapping(value = "/timercheck",method=RequestMethod.GET)
	public void timercheck(HttpServletRequest request,HttpServletResponse response){

		if(!Constants.IS_DEBUG){
			final String vendorCode = Constants.VENDOR_CODE;

			final String productScope = Constants.PRODUCT_SCOPE;

			Dog curDog = new Dog(Constants.FEATURE_ID);

			curDog.loginScope(productScope,vendorCode);

			int status = curDog.getLastError();

			Map<Integer,String> code = new HashMap<Integer, String>();
			code.put(DogStatus.DOG_NOT_FOUND, "未发现超级狗");
			code.put(DogStatus.DOG_INV_VCODE, "开发商代码无效");
			code.put(DogStatus.DOG_FEATURE_EXPIRED, "该特征许可已失效");
			code.put(DogStatus.DOG_TIME_ERR, "系统时间被篡改");

			if(status == DogStatus.DOG_STATUS_OK){
				curDog.logout();
			}else {
				request.getSession().removeAttribute(Constants.USER_INFO);
				request.getSession().removeAttribute(Constants.USER_ID);
				SecurityUtils.getSubject().logout();
				if(code.containsKey(status)){
					ajaxReturn(response,null,code.get(status),0);
					return;
				}
				ajaxReturn(response,null,"验证超级狗错误,错误码:"+status,0);

			}
		}

	}

}
