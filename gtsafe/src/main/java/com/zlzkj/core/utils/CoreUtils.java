package com.zlzkj.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zlzkj.core.constants.Constants;

/**
 * 通用工具类
 * @author Simon
 *
 */
public class CoreUtils {
	/**
	 * 组装前端datagrid组件需要的数据
	 * @param totalCount 记录总数
	 * @param data 结果集
	 * @return
	 */
	public static Map<String, Object> getUIGridData(int totalCount,List<?> data){
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("total", totalCount);
		res.put("rows", data);
		return res;
	}

	/**
	 * 把""变成null
	 * @param str
	 * @return
	 */
	public static String nullToEmpty(String str){
		return str==null?"":str;
	}


	/**
	 * 给sessionName加上前缀，以避免和其他项目冲突(前缀是项目名称)
	 * @param sessionName
	 * @return
	 */
	public static String formatSessionName(String sessionName){
		// - /usr/local/Cellar/tomcat/7.0.50/libexec/webapps/shdyj-admin/WEB-INF/classes/
		String path = CoreUtils.class.getClassLoader().getResource("").getPath();
		String flagStr = "/WEB-INF/classes/";
		if(path.indexOf(flagStr)!=-1){
			path = path.substring(0, path.length() - flagStr.length());
			path = path.substring(path.lastIndexOf("/")+1);
		}
		return path+"_"+sessionName;
	}

	/**
	 * 格式化参数map，将参数最后一个值作为真实值
	 * ;如/?a=1&b=2&a=3，最终a=3
	 * @param paramMap
	 * @return 
	 */
	public static Map<String,String> formatParamMap(Map<String,String[]> paramMap){
		Map<String,String> map = new HashMap<String,String>();
		Iterator<String> keys = paramMap.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();//key
			String[] value = paramMap.get(key);//上面key对应的value
			map.put(key, value[value.length-1]);
		}
		return map;
	}

	/**
	 * 获取当前时间的时间戳，单位秒
	 * @return
	 */
	public static int getNowTimestamp(){
		Long time = System.currentTimeMillis()/1000;
		return time.intValue();
	}
	/**
	 * 将日期转成时间戳
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static long formatTimestamp(String time,String...format ){

		String formatString = "yyyy-MM-dd HH:mm:ss";
		if(format.length==1){
			formatString = format[0];
		}

		SimpleDateFormat df = new SimpleDateFormat(formatString);
		if("".equals(time)){
			return 0;
		}else{
			Date date = null;
			try {
				date = df.parse(time);
			} catch (ParseException e) {
			}
			return date.getTime()/1000;
		}
	}

	/**
	 * 格式化时间戳成日期时间
	 * @param timestamp
	 * @param format
	 * @return
	 */
	public static String formatTimestamp(int timestamp,String... format){
		if(timestamp==0){
			return "0";
		}
		String formatString = "yyyy-MM-dd HH:mm:ss";
		if(format.length==1){
			formatString = format[0];
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		return sdf.format(new Date(timestamp*1000L));
	}

	/**
	 * 将一个 JavaBean 对象转化为一个  Map
	 * @param bean 要转化的JavaBean 对象
	 * @return 转化出来的  Map 对象
	 * @throws IntrospectionException 如果分析类属性失败
	 * @throws IllegalAccessException 如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
	 */
	public static Map<String, Object> convertBean(Object bean){
		if(bean==null){
			return null;
		}
		Class<? extends Object> type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(type);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
		for (int i = 0; i< propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = null;
				try {
					result = readMethod.invoke(bean, new Object[0]);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}catch (IllegalAccessException e2) {
					e2.printStackTrace();
				}catch (InvocationTargetException e3) {
					e3.printStackTrace();
				}

				//                if (result != null) {
				//                    returnMap.put(propertyName, result);
				//                } else {
				//                    returnMap.put(propertyName, "");
				//                }
				returnMap.put(propertyName, result);
			}
		}
		return returnMap;
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * @param type 要转化的类型
	 * @param map 包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException 如果分析类属性失败
	 * @throws IllegalAccessException 如果实例化 JavaBean 失败
	 * @throws InstantiationException 如果实例化 JavaBean 失败
	 * @throws InvocationTargetException 如果调用属性的 setter 方法失败
	 */
	public static Object convertMap(Class<?> type, Map<?, ?> map){
		if(map==null){
			return null;
		}
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(type);
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 获取类属性
		Object obj = null;
		try {
			obj = type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		}// 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
		for (int i = 0; i< propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);

				Object[] args = new Object[1];
				args[0] = value;

				try {
					descriptor.getWriteMethod().invoke(obj, args);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e2) {
					e2.printStackTrace();
				} catch (InvocationTargetException e3) {
					e3.printStackTrace();
				}
			}
		}
		return obj;
	}

	/**
	 * 端掉字符串左侧的N多指定字符
	 * @param str
	 * @param needle
	 * @return
	 */
	public static String ltrim(String str,String needle){
		int start = 0;
		while (start != str.length() && needle.indexOf(str.charAt(start)) != -1) {
			start++;
		}
		if(start == str.length()){
			return "";
		}
		return str.substring(start);
	}
	/**
	 * 端掉字符串右侧的N多指定字符
	 * @param str
	 * @param needle
	 * @return
	 */
	public static String rtrim(String str,String needle){
		int end = str.length();
		while (end != 0 && needle.indexOf(str.charAt(end-1)) != -1) {
			end--;
		}
		if(end == 0){
			return "";
		}
		return str.substring(0,end);
	}

	/**
	 * 生成url,并把驼峰变成下划线
	 * 假设当前访问的应用为home
	 * {z:u("public/login")} =》home/public/login
	 * {z:u("user/info?id=1")} =》 home/user/list?id=1
	 * {z:u("adminz/user/list")} =》 adminz/user/list
	 * @param uri
	 * @return
	 */
	public static String generateUrl(String uri) {
		String url = CoreUtils.rtrim(SpringContextUtil.getContextPath(),"/")+"/";
		if (uri == null || uri.length() == 0) {
			return url;
		}
		//删除uri前面的N多个杠
		uri = CoreUtils.ltrim(uri,"/");
		if(uri==""){
			return url;
		}

		//删除末尾的杠
		uri = CoreUtils.rtrim(uri,"/");

		//驼峰转成下划线
		uri = CoreUtils.underscoreName(uri);

		String urlHtmlSuffix = Constants.URL_SUFFIX;
		if(urlHtmlSuffix!=null && urlHtmlSuffix.isEmpty()){ //接上url伪静态
			if(urlHtmlSuffix.indexOf(".")!=0){ //没有点就加上
				urlHtmlSuffix = "."+urlHtmlSuffix;
			}
			if(uri.indexOf("?")==(uri.length()-1)){
				uri = uri.substring(0, uri.length()-1); //删除末尾的问号
			}
			if(uri.indexOf("?")!=-1){
				String[] arr = uri.split("\\?"); //提取url参数
				url += CoreUtils.rtrim(arr[0],"/") + urlHtmlSuffix + "?" + arr[1];
			}else{
				url += uri + urlHtmlSuffix;
			}
		}else{
			url += uri;
		}

		return url;
	}

	/**
	 * 命名由驼峰法变下划线小写
	 * @return return "" if name is null or empty
	 */
	public static String underscoreName(String name) {
		if(name==null || name.isEmpty()){
			return "";
		}
		StringBuilder result = new StringBuilder();
		String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		// 将第一个字符处理成小写
		result.append(name.substring(0, 1).toLowerCase());
		// 循环处理其余字符
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			// 在大写字母前添加下划线
			if (Arrays.asList(letters).contains(s)) {
				result.append("_");
			}
			// 其他字符直接转成小写
			result.append(s.toLowerCase());
		}
		return result.toString();
	}

	/**
	 * 命名由下划线小写变驼峰法
	 * @param name 待转换的下划线名称
	 * @param isFirstUpperCase 首字母是否需要大写，默认小写
	 * @return  return null if name is null or empty
	 */
	public static String camelName(String name,Boolean... isFirstUpperCase) {
		if(name==null || name.isEmpty()){
			return "";
		}
		StringBuilder result = new StringBuilder();
		if(isFirstUpperCase.length!=0 && isFirstUpperCase[0]){
			// 将第一个字符处理成大写
			result.append(name.substring(0, 1).toUpperCase());
		}else{
			// 将第一个字符保持不变
			result.append(name.substring(0, 1));
		}

		// 循环处理其余字符
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			// 遇到下划线则舍弃，并把后一个字符变大写
			if (s.equals("_")) {
				i++;
				String nextChar = name.substring(i, i+1);
				result.append(nextChar.toUpperCase());
			}else{
				// 其他字符直接追加上
				result.append(s);
			}

		}
		return result.toString();
	}


	/**
	 * ajax返回json数据
	 * @param response
	 * @param data 要返回的数据
	 */
	public static String ajaxReturn(HttpServletResponse response,Object data){
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
	public static String ajaxReturn(HttpServletResponse response,Object data,String info,int status){
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
	public static void render(HttpServletResponse response,String text,String... contentType) {
		//		if(contentType.length==0){
		//			response.setContentType("text/html");
		//		}else if(contentType[0].toLowerCase()=="json"){
		//			response.setContentType("application/json");
		//		}
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
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

	/**
	 * 缩放图片
	 * @param imageUrl 原始图片的网址
	 * @param resizeString 缩放尺寸 100x200
	 * @return
	 */
	public static String resizeImage(String imageUrl,String resizeString){
		if(!UploadUtils.isImageResize()){
			return imageUrl;
		}
		String extName = imageUrl.substring(imageUrl.lastIndexOf(".")).toLowerCase();
		return imageUrl+"_r"+resizeString+"."+extName;
	}

	/**
	 * 裁剪图片
	 * @param imageUrl 原始图片的网址
	 * @param cropString 裁剪尺寸 100x200
	 * @return
	 */
	public static String cropImage(String imageUrl,String cropString){
		if(!UploadUtils.isImageResize()){
			return imageUrl;
		}
		String extName = imageUrl.substring(imageUrl.lastIndexOf(".")).toLowerCase();
		return imageUrl+"_c"+cropString+extName;
	}
	/**
	 * 检验是否为数字
	 * @param str
	 * @return
	 */
	public final static boolean isNumeric(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^[0-9]*$");
		else
			return false;
	}

	/**
	 * 组装whereIN
	 * @param info IN  (要查询的条件)
	 * @param whereList  (被查询的list)
	 * @param whereString  (被查询list的条件)
	 * @return
	 */
	public static String whereIN(String info,List<Map<String ,Object>> whereList,String whereString){
		String where = null;
		if (whereList!=null&&whereList.size()!=0) {
			where =info+" IN ( ";
			for(Map<String, Object> map : whereList){
				where = where+"'"+map.get(whereString)+"',";
			}
			where = where.substring(0,where.length()-1);
			where = where + ")";	
		}
		return where;

	}
	/**
	 * 计算两个日期的相差天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long DateDays(String date1, String date2) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		long myTime;
		Date aDate2;
		Date aDate;
		long myTime2;
		long days = 0;
		try {
			aDate = formatter.parse(date1);// 任意日期，包括当前日期
			myTime = (aDate.getTime() / 1000);

			// SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
			aDate2 = formatter.parse(date2);// 任意日期，包括当前日期
			myTime2 = (aDate2.getTime() / 1000);

			if (myTime > myTime2) {
				days = (myTime - myTime2) / (1 * 60 * 60 * 24);
			} else {
				days = (myTime2 - myTime) / (1 * 60 * 60 * 24);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return days;

	}
	/**
	 * html转义
	 * @param content
	 * @return
	 */
	public static String htmlEscape(String content) {
		if(content==null) return "";        
		String html = content;
		html = html.replace( "'", "&apos;");
		//		html = html.replaceAll( "&", "&amp;");
		//		html = html.replace( "\"", "&quot;");  //"
		//		html = html.replace( "\t", "&nbsp;&nbsp;");// 替换跳格
		//		html = html.replace( " ", "&nbsp;");// 替换空格
		//		html = html.replace("<", "&lt;");
		//		html = html.replaceAll( ">", "&gt;");
		return html;
	}
	/**
	 * java转义
	 * @param content
	 * @return
	 */
	public static String javaEscape(String content) {
		if(content==null) return "";        
		String html = content;
		html = html.replace("&apos;","'");
		//		html = html.replaceAll("&amp;","&");
		//		html = html.replace( "&quot;","\"");  //"
		//		html = html.replace( "&nbsp;&nbsp;","\t");// 替换跳格
		//		html = html.replace( "&nbsp;","\t");// 替换空格
		//		html = html.replace( "&lt;","<");
		//		html = html.replaceAll( "&gt;",">");
		return html;
	}
	/** 
	 * 将源数据前补零，补后的总长度为指定的长度，以字符串的形式返回 
	 * @param integer 
	 * @param formatLength 
	 * @return 重组后的数据 
	 */  
	public static String frontCompWithZore(int integer,int formatLength)  
	{  
		/* 
		 * 0 指前面补充零 
		 * formatLength 字符总长度为 formatLength 
		 * d 代表为正数。 
		 */  
		String newString = String.format("%0"+formatLength+"d", integer);  
		return  newString;  
	}  
	/**
	 * 对list进行排序
	 * @param list
	 * @param number 排序字符串
	 * @return
	 */
	public static List<Map<String, Object>> sortList(List<Map<String, Object>> list ,String number) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i; j < list.size(); j++) {
				Map<String, Object> tmp0 = list.get(i);
				int number0 = Integer.valueOf(tmp0.get(number).toString());
				Map<String, Object> tmp1 = list.get(j);
				int number1 = Integer.valueOf(tmp1.get(number).toString());
				if (number0 > number1) {
					Map<String, Object> tmp = new HashMap<String, Object>();
					tmp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, tmp);
				}
			}
		}
		return list;
	}
	/**
	 * 获取一个字符串中的图片src 的list
	 * @param urlString
	 * @return
	 */
	public static List<String> getImgSrc(String urlString) {
		String IMGURL_REG = "\\<img\\s+src=.*?\\s*\\/?\\>";  
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(urlString);  
		List<String> listImgUrl = new ArrayList<String>();  
		List<String> listImgSrc = new ArrayList<String>();  
		while (matcher.find()) {  
			listImgUrl.add(matcher.group());  
		}  
		for (String string : listImgUrl) {//获取所有图片 标签
			Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(string);
			while (m.find()) {
				listImgSrc.add(m.group(1));
			}
		}
		return listImgSrc;
	}
	/**
	 * 替换一个字符串中的src 在前面加上当前服务器的ip 和 端口号
	 * @param request
	 * @param urlString
	 * @return
	 */
	public static String replaceImgSrc(HttpServletRequest request,String urlString) {
		if (urlString!=null&&urlString!="") {
			List<String> listImgSrc = getImgSrc(urlString);
			for (String string : listImgSrc) {//替换所有图片src 
				urlString = urlString.replace(string,CoreUtils.serverUrl(request, string));
			}
		}
		return urlString;
	}
	/**
	 * 添加服务器的ip
	 * @param request
	 * @param url
	 * @return
	 */
	public static String serverUrl(HttpServletRequest request, String url) {
		if (url.indexOf("http://")<0) {
			String http = "http://" + request.getServerName() //服务器地址  
					+ ":"   
					+ request.getServerPort(); //端口号  
			if (url.startsWith("/")) {
				url= http+url;
			}else {
				url= http+"/"+url;
			}
		}
		return url;
	}
	/**
	 * 检验导入的时候是否为数字 都返回字符串
	 * @param number
	 * @return String
	 */
	public final static String importIsNumber(String number) {
		String valString=number;
		if(number.indexOf(".")!=-1){
			valString = number.substring(0, number.indexOf("."));
		}
		return valString;
	}

	/**
	 * 读取手机端JSON
	 * @param data
	 * @return
	 */
	public static List<Map<String,Object>> readJson(String json){
		ObjectMapper mapper = new ObjectMapper();
		try {
	        List<Map<String, Object>> list = mapper.readValue(json, List.class);
	        return list;
	    } catch (JsonParseException e) {
	        e.printStackTrace();
	        return null;
	    } catch (JsonMappingException e) {
	        e.printStackTrace();
	        return null;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
