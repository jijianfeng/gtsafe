package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.model.NoticeBoard;

public interface NoticeBoardService {
	
//	public Integer add(NoticeBoard noticeBoard);
//
//	public void del(int id);
//	
	public int update(NoticeBoard noticeBoard);
	
//	public Map<String, Object> getNoticeBoardAll(int page,int rows);
	//电视机端获取布告栏
	public List<Map<String, Object>> getApiNoticeBoard();

	public Map<String, Object> getNotice();



}
