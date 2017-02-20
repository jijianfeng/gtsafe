package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.RoleNodeDao;
import com.zlzkj.app.model.RoleNode;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class RoleNodeServiceImpl implements RoleNodeService{
	@Autowired
	private RoleNodeDao roleNodeDao;

	@Override
	public int add(RoleNode roleNode) {
		return (Integer) roleNodeDao.save(roleNode);
	}

	@Override
	public Map<String, Object> findBy(int roleId, int nodeId) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(RoleNode.class);
		sqlBuilder.fields("id")
				  .where("roleId="+roleId+" AND nodeId="+nodeId);
		List<Map<String, Object>> list = roleNodeDao.findBySQL(sqlBuilder.buildSql());
		return( list != null && !list.isEmpty() )? list.get(0):null;
	}

	@Override
	public int add(int roleId, String[] nodeId) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(RoleNode.class);
		sqlBuilder.where("roleId="+roleId);
		roleNodeDao.delete(sqlBuilder.buildDeleteSql());
		for(String id:nodeId){
			RoleNode roleNode = new RoleNode();
			roleNode.setAddTime(CoreUtils.getNowTimestamp());
			roleNode.setRoleId(roleId);
			roleNode.setNodeId(Integer.parseInt(id));
			roleNodeDao.save(roleNode);
		}
		return 1;
	}

}


