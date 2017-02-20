package com.zlzkj.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.DocsCateDao;
import com.zlzkj.app.model.DocsCate;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class DocsCateServiceImpl implements DocsCateService {
	
	@Autowired
	private DocsCateDao docsCateDao;

	@Override
	public List<Map<String, Object>> getCateList(int level) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(DocsCate.class);
		sqlBuilder.fields("id","name as text").where("pid=0");
		List<Map<String, Object>> cateList = docsCateDao.findBySQL(sqlBuilder.buildSql());
		if(level > 1){
			for(Map<String, Object> map : cateList){
				List<Map<String, Object>> childList = docsCateDao.findBySQL(sqlBuilder
						.fields("id","name as text")
						.where("pid="+map.get("id").toString()).buildSql());
				map.put("children", childList);
			}
			return cateList;
		}else{
			Map<String, Object> cateMap = new HashMap<String, Object>();
			cateMap.put("id", 0);
			cateMap.put("text", "顶级分类");
			cateMap.put("children", cateList);
			List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
			data.add(0, cateMap);
			return data;
		}

	}

	@Override
	public int add(DocsCate docsCate) {
		List<DocsCate> cateList = docsCateDao.findByProperty("name", docsCate.getName());
		if(cateList.size() > 0){
			return -1;
		}
		docsCate.setCount(0);
		docsCate.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) docsCateDao.save(docsCate);
	}

	@Override
	public List<Map<String, Object>> getCateAll() {		
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(DocsCate.class);
		sqlBuilder.fields("id","name as cateName","count","pid").where("pid=0");
		List<Map<String, Object>> cateList = docsCateDao.findBySQL(sqlBuilder.buildSql());
		for(Map<String, Object> map : cateList){
			List<Map<String, Object>> childList = docsCateDao.findBySQL(sqlBuilder
					.fields("id","name as cateName","pid","count")
					.where("pid="+map.get("id").toString()).buildSql());
			map.put("children", childList);
			//map.put("is_parent", 1);
		}
		return cateList;
	}
	
	@Override
	public List<Map<String, Object>> getCate(Map<String, Object> where) {		
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(DocsCate.class);
		sqlBuilder.fields("*").where(where);
		List<Map<String, Object>> cateList = docsCateDao.findBySQL(sqlBuilder.buildSql());
		
		return cateList;
	}
	@Override
	public int update(DocsCate docsCate) {
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(DocsCate.class);
		sbName.fields("name").where("id <> "+docsCate.getId()+" and name=\'"+docsCate.getName()+"\'");
		List<DocsCate> list = docsCateDao.findBySQLToEntity(sbName.buildSql());
		if (list.size()>0) {
			return -1;
		}
		return docsCateDao.update(docsCate.getId(), docsCate);
	}

	@Override
	public Boolean isLibEmpty(int id) {
		DocsCate docsCate = docsCateDao.getById(id);
		if(docsCate != null){
			return (docsCate.getCount() > 0)? false:true;
		}
		return false;		
	}

	@Override
	public void del(int id) {
		docsCateDao.delete(id);
	}

	@Override
	public DocsCate findById(Integer id) {
		List<DocsCate> docsCate = docsCateDao.findByProperty("id", id);
		return( docsCate != null && !docsCate.isEmpty() )? docsCate.get(0):null;
	}

	@Override
	public void countPlus(int id) {
		docsCateDao.setIncDec("id="+id, "count", 1, "+");
		docsCateDao.setIncDec("id="+docsCateDao.getById(id).getPid(), "count", 1, "+");
	}

	@Override
	public void countMinus(int id) {
		docsCateDao.setIncDec("id="+id, "count", 1, "-");
		docsCateDao.setIncDec("id="+docsCateDao.getById(id).getPid(), "count", 1, "-");
	}

	@Override
	public String getCateIds(Integer cateId) {
		String idsString = cateId+",";
		List<DocsCate> list = docsCateDao.findByProperty("pid", cateId);
		for (DocsCate docsCate : list) {
			idsString+=docsCate.getId()+",";
		}
		if (idsString.endsWith(",")) {
			idsString = idsString.substring(0, idsString.length()-1);
		}
		return idsString;
	}

}
