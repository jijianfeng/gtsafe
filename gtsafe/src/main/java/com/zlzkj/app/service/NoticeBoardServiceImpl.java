package com.zlzkj.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.NoticeBoardDao;
import com.zlzkj.app.model.NoticeBoard;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class NoticeBoardServiceImpl implements NoticeBoardService {
	
	@Autowired
	private NoticeBoardDao noticeBoardDao;

//	@Override
//	public Integer add(NoticeBoard noticeBoard) {
//		noticeBoard.setAddTime(CoreUtils.getNowTimestamp());
//		return (Integer) noticeBoardDao.save(noticeBoard);
//	}

//	@Override
//	public void del(int id) {
//		noticeBoardDao.delete(id);
//	}

	@Override
	public int update(NoticeBoard noticeBoard) {
		noticeBoard.setContent(CoreUtils.htmlEscape(noticeBoard.getContent().toString()));
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(NoticeBoard.class);
		sqlBuilder.fields("id","content");
		List<Map<String, Object>> notice = noticeBoardDao.findBySQL(sqlBuilder.buildSql());
		if(notice==null||notice.size()==0){
			return (Integer)noticeBoardDao.save(noticeBoard);
		}
		return noticeBoardDao.update(Integer.valueOf(notice.get(0).get("id").toString()), noticeBoard);
	}

//	@Override
//	public Map<String, Object> getNoticeBoardAll(int page, int rows) {
//		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(NoticeBoard.class);
//		sqlBuilder.fields("id","content","isShow","orderId","addTime").page(page, rows).order("orderId", "ASC");
//		List<Map<String, Object>> data = noticeBoardDao.findBySQL(sqlBuilder.buildSql());
//		for (Map<String, Object> map : data) {
//			map.put("content",CoreUtils.javaEscape(map.get("content").toString()));
//			int addTime = (Integer) map.get("addTime");
//			map.put("addTime", CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
//		}
//		int count = Integer.parseInt(noticeBoardDao.getValueBySQL(sqlBuilder.buildCountSql()));
//		return CoreUtils.getUIGridData(count, data);
//	}

	@Override
	public List<Map<String, Object>> getApiNoticeBoard() {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(NoticeBoard.class);
		sqlBuilder.fields("id","content").where("isShow=1");
		List<Map<String, Object>> data = noticeBoardDao.findBySQL(sqlBuilder.buildSql());
		for (Map<String, Object> map : data) {
			map.put("content",CoreUtils.javaEscape(map.get("content").toString()));
		}
		return data;
	}

	@Override
	public Map<String, Object> getNotice() {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(NoticeBoard.class);
		sqlBuilder.fields("*");
		List<Map<String, Object>> data = noticeBoardDao.findBySQL(sqlBuilder.buildSql());
		for (Map<String, Object> map : data) {
			map.put("content",CoreUtils.javaEscape(map.get("content").toString()));
		}
		Map<String, Object> notice =new HashMap<String, Object>();
		if (data!=null&&data.size()!=0) {
			notice = data.get(0);
		}
		
		return notice;
	}
	

}
