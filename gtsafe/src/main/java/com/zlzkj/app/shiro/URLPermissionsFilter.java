package com.zlzkj.app.shiro;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class URLPermissionsFilter  extends PermissionsAuthorizationFilter{
	/** 
	 *@param mappedValue 指的是在声明url时指定的权限字符串，如/User/create.do=perms[User:create].我们要动态产生这个权限字符串，所以这个配置对我们没用 
	 */  
	public boolean isAccessAllowed(ServletRequest request,  
			ServletResponse response, Object mappedValue) throws IOException {
		return super.isAccessAllowed(request, response, buildPermissions(request));     
	} 
	
	protected String[] buildPermissions(ServletRequest request) {  
		String[] perms = new String[1];  
		HttpServletRequest req = (HttpServletRequest) request;  
		String path = req.getServletPath();  
		perms[0] = path;//path直接作为权限字符串  
		/*String regex = "/(.*?)/(.*?)\\.(.*)"; 
        if(url.matches(regex)){ 
            Pattern pattern = Pattern.compile(regex); 
            Matcher matcher = pattern.matcher(url); 
            String controller =  matcher.group(1); 
            String action = matcher.group(2); 

        }*/  
		return perms;  
	}  

	/**
	 * 所有需要被拦截的方法都会执行该方法
	 * @throws IOException 
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;

		//拦截ajax请求
		if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
			httpServletResponse.setContentType("text/html");
			httpServletResponse.setCharacterEncoding("UTF-8");
			httpServletResponse.setHeader("Cache-Control", "no-cache");
			httpServletResponse.setDateHeader("Expires", 0);
			PrintWriter out = httpServletResponse.getWriter();
			if (httpServletRequest.getMethod().equals("POST")) {
				out.println("{\"data\":\"null\",\"info\":\"您没有权限!\",\"status\":-2}");
			}else if (httpServletRequest.getMethod().equals("GET")) {
				out.println("您没有权限!");
			}
			out.flush();
			out.close();
			return false;
		}
		return super.onAccessDenied(request, response);
	}
}
