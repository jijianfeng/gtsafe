package com.zlzkj.app.dao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.model.Role;
import com.zlzkj.core.dao.CoreDaoImpl;

@Repository
@Transactional
public class RoleDaoImpl extends CoreDaoImpl<Role> implements RoleDao {

}
