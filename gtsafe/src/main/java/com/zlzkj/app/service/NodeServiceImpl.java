package com.zlzkj.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.NodeDao;
import com.zlzkj.app.model.Node;
import com.zlzkj.app.model.RoleNode;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class NodeServiceImpl implements NodeService{
	@Autowired
	private NodeDao nodeDao;
	@Autowired
	private RoleNodeService roleNodeService;

	@Override
	public List<Map<String, Object>> getNodeList() {
		SQLBuilder sqlBuilder = SQLBuilder .getSQLBuilder(Node.class);
		sqlBuilder.fields("id,name,alias").where("pid IN (68,69,70,71)");
		List<Map<String, Object>> list = nodeDao.findBySQL(sqlBuilder.buildSql());
		for(Map<String, Object> one:list){
			sqlBuilder.fields("id,name,alias").where("pid="+one.get("id").toString());
			one.put("node", nodeDao.findBySQL(sqlBuilder.buildSql()));
		}
		return list;
	}

	@Override
	public int add(Node node) {
		return (Integer) nodeDao.save(node);
	}

	@Override
	public List<Map<String, Object>> getRoleNodeList(int roleId) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(Node.class);
		sqlBuilder.fields("id,alias")
		.where("pid IN (68,69,70,71)").order("orderId", "ASC");
		List<Map<String, Object>> list = nodeDao.findBySQL(sqlBuilder.buildSql());
		for(Map<String, Object> one:list){
			one.put("isCheck", false);
			Map<String, Object> roleNode = roleNodeService.findBy(roleId, (Integer)one.get("id"));
			if(roleNode!=null)
				one.put("isCheck", true);

			sqlBuilder.fields("id,alias")
			.where("pid="+one.get("id")).order("orderId", "ASC");
			List<Map<String, Object>> list2 = nodeDao.findBySQL(sqlBuilder.buildSql());
			for(Map<String, Object> two:list2){
				two.put("isCheck", false);
				Map<String, Object> roleNode2 = roleNodeService.findBy(roleId, (Integer)two.get("id"));
				if(roleNode2!=null)
					two.put("isCheck", true);

				sqlBuilder.fields("id,alias")
				.where("pid="+two.get("id")).order("orderId", "ASC");
				List<Map<String, Object>> list3 = nodeDao.findBySQL(sqlBuilder.buildSql());

				for(Map<String, Object> three:list3){
					three.put("isCheck", false);
					Map<String, Object> roleNode3 = roleNodeService.findBy(roleId, (Integer)three.get("id"));
					if(roleNode3!=null)
						three.put("isCheck", true);
				}
				if(list3==null || list3.isEmpty())
					list3 = null;
				two.put("node",list3);
			}
			one.put("node", list2);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> leftNodeList(int roleId) {

		Map<String, Object> whereMap = new HashMap<String,Object >();
		whereMap.put("Node.pid", 0);
		List<Map<String, Object>> list0 = this.getByPid(whereMap);
		for (Map<String, Object> one : list0) {
			Map<String, Object> whereone = new HashMap<String,Object >();
			whereone.put("Node.pid", one.get("id"));
			List<Map<String, Object>> list = this.getByPid(whereone);
			List<Map<String, Object>> nodeList = new ArrayList<Map<String,Object>>();
			for(Map<String, Object> two:list){
				Map<String, Object> where = new HashMap<String,Object >();
				where.put("Node.pid", two.get("id"));
				where.put("RoleNode.roleId", roleId);
				where.put("Node.isMenu", "1");
				List<Map<String, Object>> list2 = this.getByRoleId(where);
				if (list2!=null&&list2.size()>0) {
					two.put("node", list2);
					nodeList.add(two);
				}
			}
			one.put("node",nodeList);
		}
		return list0;

	}
	public List<Map<String, Object>> getByRoleId(Map<String, Object> where) {
		SQLBuilder sqlBuilder2 = SQLBuilder.getSQLBuilder(Node.class);
		sqlBuilder2.fields("Node.id as id,Node.alias as alias,Node.name as name")
		.where(where).order("orderId", "ASC")
		.join(RoleNode.class, "RoleNode.nodeId=Node.id");
		return nodeDao.findBySQL(sqlBuilder2.buildSql());
	}
	
	public List<Map<String, Object>> getByPid(Map<String, Object> where) {
		SQLBuilder sqlBuilder2 = SQLBuilder.getSQLBuilder(Node.class);
		sqlBuilder2.fields("Node.id as id,Node.alias as alias,Node.name as name")
		.where(where).order("orderId", "ASC");
		return nodeDao.findBySQL(sqlBuilder2.buildSql());
	}

}


