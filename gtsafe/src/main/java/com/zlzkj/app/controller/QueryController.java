package com.zlzkj.app.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
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

import com.zlzkj.app.model.Query;
import com.zlzkj.app.service.QueryService;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.controller.CoreController;
import com.zlzkj.core.utils.CopyFile;
import com.zlzkj.core.utils.IniEditor;
import com.zlzkj.core.utils.UploadUtils;

/**
 * 查询分析器
 */
@Controller
@RequestMapping(value="/query")
public class QueryController extends CoreController{
	@Autowired
	private QueryService queryService;
	/**
	 * 显示构造页面信息
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/select",method=RequestMethod.GET)
	public String select(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		String content = URLDecoder.decode(request.getParameter("content"),"utf8");
		String dir = "/custom-ini/"+content+".ini";
		int flag = CheckIfFileExists(dir);
		if (flag==-1) {
			ajaxReturn(response,null,"配置文件不存在,请先添加配置文件",0);
			return null;
		}
		List<Map<String, Object>> AllFields =queryService.getAllFields(content);
		model.addAttribute("fieldNames",AllFields);
		model.addAttribute("content", content);
		List<Map<String,Object>> compareList = queryService.getCompare();
		model.addAttribute("compare",compareList);
		return "query/select";
	}
	/**
	 * 检查路径是否存在
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public int CheckIfFileExists(String dir) throws IOException {
		CopyFile copyFile = new CopyFile();
		String OS = System.getProperty("os.name").toLowerCase();//获取系统名称
		String path = CopyFile.class.getClassLoader().getResource("").getPath();
		path = path.replace("%20", " ");
		String excludeString = File.separator+"WEB-INF"+File.separator+"classes"+File.separator;
		//源文件夹路径
		path = path.substring(0, path.length() - excludeString.length())+"/custom";
		//目标路径
		String url = UploadUtils.getFileRepository()+"/custom-ini";
		url = url+"/" ;
		if(OS.indexOf("win") >= 0 && path.startsWith("/")){
			path = path.substring(1).replace("/", "\\");
			url = url.replace("/", "\\");
		}
		//检查源文件存不存在
		File file1 = new File(path);
		File file3 = new File(url);
		boolean exists1 = file1.exists();
		boolean exists3 = file3.exists();
		if(!exists3)
			if (!exists1) {
				//源文件夹不存在返回-2
				return -2;
			}
		//检查目录
		File dirFile = new File(url);
		if (!dirFile.exists()){
			copyFile.copy(path,url);//创建url并把文件复制过去
		}
		//检查要查询的文件夹或文件存不存在
		String path2 = UploadUtils.getFileRepository();
		File file2 = new File(path2+"/"+dir);
		boolean exists2 = file2.exists();
		if (!exists2) {
			//目标文件夹或文件不存在返回-1
			return -1;
		}
		return 1;
	}
	/**
	 * 获取条件
	 * @param model
	 * @param fieldNames
	 * @throws IOException
	 */
	@RequestMapping(value="/select",method=RequestMethod.POST)
	public void select(HttpServletRequest request,HttpServletResponse response,Model model,String[] fieldNames) throws IOException{
		String content = URLDecoder.decode(request.getParameter("content"),"utf8");
		List<Map<String,Object>> list = queryService.getCondition(content,fieldNames);
		ajaxReturn(response,list);
	}
	/**
	 * 获取条件对应的值
	 * @param model
	 * @param content
	 * @param field
	 * @throws IOException
	 */
	@RequestMapping(value="/value",method=RequestMethod.POST)
	public void value(HttpServletRequest request,HttpServletResponse response,Model model,String content,String field) throws IOException{
		content = URLDecoder.decode(content,"utf8");
		List<Map<String, Object>> list = queryService.getValue(content,field);
		ajaxReturn(response,list);

	}

	/**
	 * 组装Select语句
	 * @param fieldNames
	 * @param content
	 * @throws IOException
	 */
	@RequestMapping(value="/build",method=RequestMethod.POST)
	public void build(HttpServletRequest request,HttpServletResponse response,String[] fieldNames,String content) throws IOException  {
		content = URLDecoder.decode(content,"utf8");
		String sqlString = queryService.buildSelect(content,fieldNames);
		ajaxReturn(response,sqlString);
	}
	/**
	 * 组装where
	 * @param field
	 * @param compare
	 * @param compareVal
	 * @param logic
	 * @param whereSql
	 * @throws IOException
	 */
	@RequestMapping(value="/where",method=RequestMethod.POST)
	public void where(HttpServletRequest request,HttpServletResponse response,String content,String field,String compare,String compareVal,String logic,String whereSql) throws IOException  {
		content = URLDecoder.decode(content,"utf8");
		compareVal = URLDecoder.decode(compareVal,"utf8");
		whereSql = URLDecoder.decode(whereSql,"utf8");
		if(compareVal.contains("\"")||compareVal.contains("'")){
			ajaxReturn(response,null,"请不要在比较值中使用引号和双引号,可以使用Like来进行模糊查询",0);
			return ;
		}
		if(compareVal.length()>20){
			ajaxReturn(response,null,"比较值不宜超过20个字符,查询长的比较值时请使用Like来进行模糊查询",0);
			return ;
		}
		if(whereSql.contains("WHERE")){
			if("0".equals(logic)){
				ajaxReturn(response,null,"请选择逻辑符",0);
				return ;
			}
		}
		if (compare.equals("Like")&&!compareVal.contains("%")&&!compareVal.contains("@@")) {
			ajaxReturn(response,null,"使用LIKE时,请在想匹配的字符前或后添加%或者使用@@",0);
			return ;
		}
		if(whereSql==""){
			IniEditor IniEditor = queryService.getIni(content);
			logic = "0";
			whereSql= IniEditor.get("BASESQL", "where");
		}
		String whereString = queryService.buildWhere(content,field, compare, compareVal);
		if (whereString.equals("-1")) {
			ajaxReturn(response,null,"这个比较符不能使用,请选择其他比较符",0);
			return ;
		}
		if (whereString.equals("-2")) {
			ajaxReturn(response,null,"时间格式不支持@@",0);
			return ;
		}
		if (whereString.equals("-10")) {
			ajaxReturn(response,null,"由于限制这个字段不能填写条件",0);
			return ;
		}
		if(whereSql.contains(whereString)){
			ajaxReturn(response,null,"同样的条件,请不要重复添加",0);
			return ;
		}
		if (!"0".equals(logic)) {
			whereString = logic+" "+whereString+" ";
		}
		whereSql = whereSql+" "+whereString;
		ajaxReturn(response,whereSql,"",1);
	}
	/**
	 * 立即运行sql语句
	 * @param select
	 * @param where
	 * @param content
	 * @param fieldNames
	 * @throws IOException
	 */
	@RequestMapping(value="/soon",method=RequestMethod.POST)
	public void soon(HttpServletRequest request,HttpServletResponse response,String select,String where,String content,String[] fieldNames,Integer page,Integer rows) throws IOException{
		List<Map<String, Object >> list =queryService.getTitle(fieldNames);
		content = URLDecoder.decode(content,"utf8");
		select = URLDecoder.decode(select,"utf8");
		where = URLDecoder.decode(where,"utf8");
		String sql = select+" "+where;
		List<Map<String, Object>> list2 = queryService.runSql(sql,content,fieldNames);
		Map<String, Object > map =new HashMap<String, Object>();
		map.put("field", list);
		map.put("list", list2);
		ajaxReturn(response,map);
	}
	/**
	 * 运行保存的sql
	 * @param id
	 * @throws IOException
	 */
	@RequestMapping(value="/run",method=RequestMethod.POST)
	public void run(HttpServletRequest request,HttpServletResponse response,int id,Integer page,Integer rows) throws IOException{
		Map<String, Object > map =queryService.run(id);
		ajaxReturn(response,map);
	}

	/**
	 * 显示保存查询项名称页面
	 * @param select
	 * @param where
	 * @param content
	 * @param title
	 * @param fieldNames
	 * @return 
	 * @throws IOException
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(HttpServletRequest request,HttpServletResponse response,Model model,String select,String where,String content,String[] fieldNames,Integer id) throws IOException{
		select = URLDecoder.decode(select,"utf8");
		where = URLDecoder.decode(where,"utf8");
		String fieldName = "";
		for (String string : fieldNames) {
			string = URLDecoder.decode(string,"utf8");
			fieldName+=string+",";
		}
		fieldName=fieldName.substring(0, fieldName.length()-1);
		content = URLDecoder.decode(content,"utf8");
		model.addAttribute("select", select);
		model.addAttribute("where", where);
		model.addAttribute("content", content);
		model.addAttribute("fieldName", fieldName);
		if (id!=null) {
			model.addAttribute("id", id);
			Query query = queryService.getQueryById(id);
			model.addAttribute("title", query.getName());
		}
		return "query/save";
	}
	/**
	 * 保存更新查询项
	 * @param select
	 * @param where
	 * @param content
	 * @param title
	 * @param fieldName
	 * @param id
	 * @throws IOException
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public void add(HttpServletRequest request,HttpServletResponse response,String select,String where,String content,String title,String fieldName,Integer id) throws IOException{
		if(id==null){
			int flag = queryService.add(select,where,content,title,fieldName);
			if (flag >0) {
				ajaxReturn(response,null,"添加成功~",1);
			}else if(flag==-1){
				ajaxReturn(response,null,"查询项已存在",0);
			}else{
				ajaxReturn(response,null,"发生错误,请重试",0);
			}
		}else{
			int flag = queryService.update(select,where,content,title,fieldName,id);
			if (flag >0) {
				ajaxReturn(response,null,"更新成功~",1);
			}else if(flag==-1){
				ajaxReturn(response,null,"查询项已存在",0);
			}else{
				ajaxReturn(response,null,"发生错误,请重试",0);
			}
		}

	}
	/**
	 * 显示查询列表
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public void list(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException  {
		Map<String, Object> whereMap =new HashMap<String, Object>();
		String content = request.getParameter("content");
		if (content!=null) {
			whereMap.put("content", content);
		}
		List<Map<String, Object>> list =queryService.getQueryList(whereMap);
		ajaxReturn(response,list);
	}
	/**
	 * 显示这个查询里所有需要填写的条件
	 * @param model
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/condition",method=RequestMethod.GET)
	public String condition(HttpServletRequest request,HttpServletResponse response,Model model,int id) throws IOException  {
		List<Map<String, Object>> list =queryService.getInputList(id);
		model.addAttribute("status",list);
		return "query/condition";
	}
	/**
	 * 运行保存好带@@的sql
	 * @param id
	 * @param oldValue
	 * @throws IOException
	 */
	@RequestMapping(value="/condition",method=RequestMethod.POST)
	public void condition(HttpServletRequest request,HttpServletResponse response,int id,String[] oldValue,Integer page,Integer rows) throws IOException  {
		List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
		for (String name : oldValue) {
			Map<String, Object > map =new HashMap<String, Object>();
			map.put("val", request.getParameter(name));
			if(request.getParameter(name).length()>20){
				ajaxReturn(response,null,"比较值过长,可能会查询不到结果,比较值长度不宜超过20,或者可以修改查询项条件为Like进行模糊查询",0);
				return ;
			}
			map.put("name", name);
			list.add(map);
		}
		Map<String, Object > map =queryService.runCondition(id,list);
		ajaxReturn(response,map,"",1);
	}
	/**
	 * 显示自定义查询页面
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/custom",method=RequestMethod.GET)
	public String custom(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException  {
		String dir = "/custom-ini/查询目录.ini";

		int flag = CheckIfFileExists(dir);
		if (flag==-1) {
			model.addAttribute("open",1);
			return "query/explain";
		}else if(flag==-2){
			model.addAttribute("open",2);
			return "query/explain";
		}
		List<String> contentsList =queryService.getAllContents();
		model.addAttribute("AllContents",contentsList);
		return "query/custom";
	}
	/**
	 * 删除自定义查询
	 * @param model
	 * @param id
	 * @throws IOException
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	public void del(HttpServletRequest request,HttpServletResponse response,Model model,int id) throws IOException  {
		int flag = queryService.del(id);
		if (flag >0) {
			ajaxReturn(response,null,"删除成功",1);
		}else{
			ajaxReturn(response,null,"发生错误",0);
		}
	}
	/**
	 * 编辑保存的查询项
	 * @param model
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(HttpServletRequest request,HttpServletResponse response,Model model,int id) throws IOException  {
		Query query = queryService.getQueryById(id);
		String fields = query.getFields();
		String content = query.getContent();
		List<Map<String, Object>> AllFields =queryService.getAllFields(content);
		for (Map<String, Object> map : AllFields) {
			if(fields.contains(map.get("field").toString())){
				map.put("check", 1);
			}else{
				map.put("check", 1);
			}
		}
		model.addAttribute("fieldNames",AllFields);
		model.addAttribute("content", content);
		List<Map<String,Object>> compareList = queryService.getCompare();
		model.addAttribute("compare",compareList);
		model.addAttribute("select",query.getSqlTop());
		if (query.getSqlEnd().equals(" ")) {
			model.addAttribute("where","");
		}else {
			model.addAttribute("where",query.getSqlEnd());
		}
		model.addAttribute("id",id);
		return "query/select";
	}
	/**
	 * 查找查询项
	 * @param model
	 * @param value
	 * @throws IOException
	 */
	@RequestMapping(value="/find",method=RequestMethod.POST)
	public void find(HttpServletRequest request,HttpServletResponse response,Model model,String value) throws IOException  {
		List<Map<String,Object>> list =queryService.findQuery(value);
		ajaxReturn(response,list);
	}
	/**
	 * 使用说明
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/explain",method=RequestMethod.GET)
	public String explain(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException  {
		return "query/explain";
	}
	/**
	 * 导出查询项
	 * @param data
	 * @param title
	 * @throws IOException
	 */
	@RequestMapping(value="/export",method=RequestMethod.POST)
	public void export(HttpServletRequest request,HttpServletResponse response,String data,String title) throws IOException  {
		String flag = queryService.exportQuery(data,title);
		if (!flag.startsWith(Constants.ERROR_FLAG)) {
			ajaxReturn(response,flag, "导出成功~", 1);
		}else{
			ajaxReturn(response,null, "导出失败，文件创建失败，请确保服务器文件仓库目录下存在query目录，并设置成777权限，错误详情"+flag, 0);
		}

	}

}
