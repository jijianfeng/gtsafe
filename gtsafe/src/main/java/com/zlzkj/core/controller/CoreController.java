package com.zlzkj.core.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 通用控制器基类
 * @author Simon
 *
 */
public class CoreController {

//	@Autowired
//	protected HttpServletRequest request;
//	protected HttpServletResponse response;	
//
//	/**
//	 * 初始化一些值
//	 */
//	@ModelAttribute
//	private void init(HttpServletResponse response) {
//
//		//设置response
//		this.response = response;
//
//		String contextPath = CoreUtils.rtrim(SpringContextUtil.getContextPath(),"/");
//		//网站根路径
//		request.setAttribute("__root__", contextPath);
//		//把以上值放入全局常量中
//		if(Constants.CONTEXT_PATH==null){
//			Constants.CONTEXT_PATH = contextPath;
//		}
//		//CSS、JS静态资源目录
//		request.setAttribute("__static__", contextPath + "/" +CoreUtils.ltrim(Constants.STATIC_PATH,"/"));
//		//当前页面的URL
//		if(request.getQueryString()!=null){
//			request.setAttribute("__url__",request.getRequestURI()+"?"+request.getQueryString());
//		}else{
//			request.setAttribute("__url__",request.getRequestURI());
//		}
//
//	}

	/**
	 * ajax返回json数据
	 * @param response
	 * @param data 要返回的数据
	 */
	protected String ajaxReturn(HttpServletResponse response,Object data){
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		render(response,jsonString,"json");
		return null;
	}


	/**
	 * ajax返回json数据，参数重载
	 * @param response
	 * @param data 要返回的数据
	 * @param info 返回的信息
	 * @param status 返回的状态
	 * @return
	 */
	protected String ajaxReturn(HttpServletResponse response,Object data,String info,int status){
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonData = new HashMap<String,Object>();
		jsonData.put("data", data);
		jsonData.put("info", info);
		jsonData.put("status", status);
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(jsonData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		render(response,jsonString,"json");
		return null;
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	protected void render(HttpServletResponse response,String text,String... contentType) {
//		if(contentType.length==0){
//			response.setContentType("text/html");
//		}else if(contentType[0].toLowerCase()=="json"){
//			response.setContentType("application/json");
//		}
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Pragma","No-Cache");
		response.setHeader("Cache-Control","no-cache,must-revalidate"); 
		response.setDateHeader("Expires", 0);
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{  
			pw.close();
		} 
	}
	


}
