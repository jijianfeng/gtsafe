package com.zlzkj.app.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.model.UserRole;
import com.zlzkj.core.dao.CoreDaoImpl;
@Repository
@Transactional
public class UserRoleDaoImpl extends CoreDaoImpl<UserRole> implements UserRoleDao {

}
