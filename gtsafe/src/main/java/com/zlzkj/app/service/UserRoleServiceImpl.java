package com.zlzkj.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zlzkj.app.dao.UserRoleDao;
import com.zlzkj.app.model.UserRole;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Override
	public Integer add(UserRole userRole) {
		//userRole.setAddTime(CoreUtils.getNowTimestamp());
		return (Integer) userRoleDao.save(userRole);
	}
	
	@Override
	public Integer update(UserRole userRole) {
		return (Integer) userRoleDao.update(userRole.getId(), userRole);
	}
	
	@Override
	public int findIdByUserId(int userId) {
		List<UserRole> userRoles = userRoleDao.findByProperty("userId", userId);
		return (userRoles == null || userRoles.isEmpty()) ? null : userRoles.get(0).getId();
	}
	
	@Override
	public int findRoleIdByUserId(int userId) {
		List<UserRole> userRoles = userRoleDao.findByProperty("userId", userId);
		return (userRoles == null || userRoles.isEmpty()) ? null : userRoles.get(0).getRoleId();
	}
	
	@Override
	public Boolean isUserEmpty(int roleId){
		List<UserRole> userRoles = userRoleDao.findByProperty("roleId", roleId);
		return userRoles.isEmpty();
	}
	
	@Override
	public void del(int id) {
		userRoleDao.delete(id);
	}
	
}
