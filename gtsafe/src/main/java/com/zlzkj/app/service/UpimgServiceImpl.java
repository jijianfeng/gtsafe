package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.UpimgDao;
import com.zlzkj.app.model.Upimg;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class UpimgServiceImpl implements UpimgService{
	private UpimgDao upimgDao;
	
	@Override
	public int add(Upimg img) {
		List<Upimg> imgList = upimgDao.findByProperty("imgName", img.getimgName());
		img.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) upimgDao.save(img);
	}

	@Override
	public int update(Upimg img) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void del(int id) {
		upimgDao.delete(id);
	}

	@Override
	public List<Map<String, Object>> getDayListApi(String time) {
		String startTime = time+"00:00:00";
		String endTime = time+"23:59:59";
		long sTime = CoreUtils.formatTimestamp(startTime);
		long eTime = CoreUtils.formatTimestamp(endTime);
		String query = "addtime between"+sTime+"AND"+eTime;
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Upimg.class);
		sqlBuilder.fields("imgName,imgLocal")
		  		  .where(query);
		List<Map<String,Object>> result = upimgDao.findBySQL(sqlBuilder.buildSql());
		if(result.size() != 0){
			return result;
		}else{
			return null;
		}
	}
	
}