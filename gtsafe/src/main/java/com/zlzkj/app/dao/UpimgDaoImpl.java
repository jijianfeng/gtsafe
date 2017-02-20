package com.zlzkj.app.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.zlzkj.app.model.Upimg;
import com.zlzkj.core.dao.CoreDaoImpl;

@Repository
@Transactional
public class UpimgDaoImpl extends CoreDaoImpl<Upimg> implements UpimgDao{
	
}