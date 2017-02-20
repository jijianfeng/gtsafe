package com.zlzkj.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class UIUtils {

	/**
     * 获取前端控件传回来的分页参数
     * @return
     */
	public static Map<String,String> getPageParams(HttpServletRequest req) {
		
		Map<String,String> map= new HashMap<String,String>();
		String[] params = {"page","rows","sort","order"};
		for(String p:params){
			String v = req.getParameter(p);
			if(v!=null){
				map.put(p, v);
			}
		}
		return map;
	}
	
	/**
	 * 组装前端datagrid组件需要的数据
	 * @param totalCount 记录总数
	 * @param data 结果集
	 * @return
	 */
	public static Map<String, Object> getGridData(Integer totalCount,List<?> data){
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("total", totalCount);
		res.put("rows", data);
		return res;
	}
	
}
