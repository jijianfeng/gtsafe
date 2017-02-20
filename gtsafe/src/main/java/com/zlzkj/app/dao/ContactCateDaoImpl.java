package com.zlzkj.app.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zlzkj.app.model.ContactCate;
import com.zlzkj.core.dao.CoreDaoImpl;
@Repository
@Transactional
public class ContactCateDaoImpl extends CoreDaoImpl<ContactCate> implements ContactCateDao{

}

