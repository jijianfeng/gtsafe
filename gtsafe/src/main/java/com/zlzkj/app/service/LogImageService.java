package com.zlzkj.app.service;

import java.util.Map;

import com.zlzkj.app.model.Docs;
import com.zlzkj.app.model.LogImage;

public interface LogImageService {
	public int add(LogImage LogImage);

	public int update(LogImage logImage);

	public void del(int id);

	public Map<String, Object> getLogImageList(Map<String, Object> where,Map<String, String> pageParams);
}
