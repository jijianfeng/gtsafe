package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.Upimg;

public interface UpimgService{
	/**
	 * 增加图片记录
	 * @param img
	 * @return                            
	 */
	public int add(Upimg img);
	public int update(Upimg img);
	public void del(int id);
	public List<Map<String,Object>> getDayListApi(String time);
}