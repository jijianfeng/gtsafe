package com.zlzkj.app.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.dao.FieldDao;
import com.zlzkj.app.dao.RoleNodeDao;
import com.zlzkj.app.dao.UserDao;
import com.zlzkj.app.dao.UserRoleDao;
import com.zlzkj.app.model.Node;
import com.zlzkj.app.model.Field;
import com.zlzkj.app.model.Role;
import com.zlzkj.app.model.RoleNode;
import com.zlzkj.app.model.User;
import com.zlzkj.app.model.UserRole;
import com.zlzkj.core.constants.Constants;
import com.zlzkj.core.utils.CoreUtils;
import com.zlzkj.core.utils.SQLBuilder;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private RoleNodeDao roleNodeDao;
	
	@Autowired
	private FieldDao fieldDao;
	
	@Override
	public Map<String, Object> getUserRoleAll(int page,int rows) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(UserRole.class);
		sqlBuilder.fields("User.id as id","User.number as number","User.account as account",
				"User.name as name","User.phone as phone","User.remark as remark","User.status as status",
				"Role.name as role","Role.id as roleId","User.fieldId as fieldId")
				.where("User.id > 2")
				.join(User.class, "User.id=UserRole.userId","left")
				.join(Role.class, "Role.id=UserRole.roleId","left")
				.page(page, rows)
				.order("User.id", "asc");
		List<Map<String, Object>> data = userRoleDao.findBySQL(sqlBuilder.buildSql());
		int count = Integer.parseInt(userRoleDao.getValueBySQL(sqlBuilder.buildCountSql()));
		return CoreUtils.getUIGridData(count, data);
	}

	//基于上面的方法，增加了工地名称的查找
	@Override
	public Map<String, Object> getUserRoleFieldAll(int page,int rows) {
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(UserRole.class);
		sqlBuilder.fields("User.id as id","User.number as number","User.account as account",
				"User.name as name","User.phone as phone","User.remark as remark","User.status as status",
				"Role.name as role","Role.id as roleId","User.fieldId as fieldId","Field.fieldName as fieldName")
				.where("User.id > 2")
				.join(User.class, "User.id=UserRole.userId","left")
				.join(Role.class, "Role.id=UserRole.roleId","left")
				.join(Field.class, "User.fieldId = Field.id" , "left")
				.page(page, rows)
				.order("User.id", "asc");
		List<Map<String, Object>> data = userRoleDao.findBySQL(sqlBuilder.buildSql());
		for(Map<String, Object> map : data){
			if(map.get("fieldName")==null)
				map.put("fieldName", "无");
		}
		int count = Integer.parseInt(userRoleDao.getValueBySQL(sqlBuilder
				.fields("count(*)")
				.join(User.class, "User.id=UserRole.userId","left")
				.join(Role.class, "Role.id=UserRole.roleId","left")
				.where("User.id > 2")
				.join(Field.class, "User.fieldId = Field.id" , "left").buildSql()));
		return CoreUtils.getUIGridData(count, data);
	}
	@Override
	public Integer add(User user) {
		//验证重复
		List<User> userList = userDao.findByProperty("account", user.getAccount());
		if(userList.size()>0){
			return -1; //
		}
		userList = userDao.findByProperty("phone", user.getPhone());
		if(userList.size()>0){
			return -2; //
		}
		userList = userDao.findByProperty("number", user.getNumber());
		if(userList.size()>0)
			return -3;
		//处理密码
		
		user.setPassword(DigestUtils.md5Hex(Constants.PASSWORD_SALT+DigestUtils.md5Hex(user.getPassword())));
		user.setAddTime(CoreUtils.getNowTimestamp());
		user.setFieldId(user.getFieldId());
		return (Integer) userDao.save(user);
	}

	@Override
	public int update(User user) {
		SQLBuilder sbName = SQLBuilder.getSQLBuilder(User.class);
		sbName.fields("name").where("id <> "+user.getId()+" and account=\'"+user.getAccount()+"\'");
		List<User> list = userDao.findBySQLToEntity(sbName.buildSql());
		if (list.size()>0) {
			return -1;
		}
		String pass = user.getPassword();
		if(pass != null && !pass.equals("")){
			user.setPassword(DigestUtils.md5Hex(Constants.PASSWORD_SALT+DigestUtils.md5Hex(pass)));
		}
		return userDao.update(user.getId(), user);
	}

	@Override
	public void del(int id) {
		userDao.delete(id);
	}
	
	@Override
	public User findByAccount(String account){
		List<User> users = userDao.findByProperty("account", account);
		return( users != null && !users.isEmpty() )? users.get(0):null;
	}
	/**
	 * 获取该用户下的所有权限节点
	 */
	@Override
	public List<Map<String, Object>> getRoleAll(String userName) {
		User user = this.findByAccount(userName);
		if(user!=null){
			SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(UserRole.class);
			sqlBuilder.fields("Role.id AS id,Role.name AS name")
			.join(Role.class, "UserRole.roleId=Role.id")
			.where("UserRole.userId="+user.getId());
			
			List<Map<String, Object>> data = userRoleDao.findBySQL(sqlBuilder.buildSql());
			
			sqlBuilder = SQLBuilder.getSQLBuilder(RoleNode.class);
			sqlBuilder.fields("Node.id AS id,Node.Name AS name")
			.join(Node.class, "RoleNode.nodeId=Node.id")
			.where("RoleNode.roleId="+data.get(0).get("id"));
			
			data = roleNodeDao.findBySQL(sqlBuilder.buildSql());
			
			return data;
		}
		return null;
	}

	@Override
	public User findById(Integer id) {
		List<User> users = userDao.findByProperty("id", id);
		return( users != null && !users.isEmpty() )? users.get(0):null;
	}

	@Override
	public int updatePass(User user) {
		return userDao.update(user.getId(), user);
	}
}
