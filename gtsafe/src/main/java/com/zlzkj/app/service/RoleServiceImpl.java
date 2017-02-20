package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.RoleDao;
import com.zlzkj.app.model.Role;
import com.zlzkj.app.model.UserRole;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleDao roleDao;

	@Override
	public Integer add(Role role) {
		List<Role> roleList = roleDao.findByProperty("name", role.getName());
		if(roleList.size() > 0){
			return -1;
		}
		role.setPid(0);
		role.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) roleDao.save(role);
	}

	@Override
	public void del(int id) {
		roleDao.delete(id);
	}

	@Override
	public int update(Role role) {
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(Role.class);
		sbName.fields("name").where("id <> "+role.getId()+" and name=\'"+role.getName()+"\'");
		List<Role> list = roleDao.findBySQLToEntity(sbName.buildSql());
		if (list.size()>0) {
			return -1;
		}
		//role.setAddTime(CoreUtils.getNowTimestamp());
		return roleDao.update(role.getId(), role);
	}

	@Override
	public Map<String, Object> getRoleAll(int page,int rows) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Role.class);
		sqlBuilder.fields("id","name","remark","status","addTime")
		.where("id<>1")
		.page(page, rows);
		List<Map<String, Object>> data = roleDao.findBySQL(sqlBuilder.buildSql());
		for (Map<String, Object> map : data) {
			int addTime = (Integer) map.get("addTime");
			map.put("addTime", CoreUtils.formatTimestamp(addTime, "yyyy-MM-dd HH:mm"));
		}
		int count = Integer.parseInt(roleDao.getValueBySQL(sqlBuilder.buildCountSql()));
		return CoreUtils.getUIGridData(count, data);
	}
	
	@Override
	public List<Map<String, Object>> getRoleAll() {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Role.class);
		sqlBuilder.fields("id","name as text").where("id<>1");
		List<Map<String, Object>> data = roleDao.findBySQL(sqlBuilder.buildSql());
		return data;
	}
	@Override
	public Role findById(int id) {
		return roleDao.getById(id);
	}
	
}
