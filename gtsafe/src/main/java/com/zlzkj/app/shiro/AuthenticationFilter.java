package com.zlzkj.app.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationFilter extends FormAuthenticationFilter {
	 @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);
	 
	/**
	 * 所有需要被拦截的方法都会执行该方法
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		
		//拦截ajax请求
		if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
			if(!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))){
					saveRequestAndRedirectToLogin(request, response);
			}else {
				httpServletResponse.setHeader("Access-Status", "-1");
			}
            return false;
		}
		return super.onAccessDenied(request, response);
	}
	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		//可自定义Token,如登录需要验证码时，需要重写该方法，并定义新的Token类
		return super.createToken(request, response);
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		//登录成功后，回调该方法
		//FormAuthenticationFilter类中是直接转跳到successUrl地址： WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
		
		/**
		 * 我们可能需要根据实际情况做出不同的操作。
		 * 有以下几种情况
		 * 1、登录页面登录：
		 * 		a:form表单形式提交，登录成功后，判断是否需要redirect到之前的页面，否则redirect到默认页面去
		 * 		b:ajax方式提交，返回json数据（{code:1,msg:'',redirect:'...'}），以及需要转跳的地址
		 * 2、登录弹出框登录：登录成功后，只需要关闭当前登录框即可
		 */
		
		return super.onLoginSuccess(token, subject, request, response);
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		//登录失败，回调该方法
		//可返回false来终止请求
		return super.onLoginFailure(token, e, request, response);
	}

}
