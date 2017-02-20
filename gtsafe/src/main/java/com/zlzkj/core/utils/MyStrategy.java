package com.zlzkj.core.utils;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
/**
 * Hibernate反向生成pojo时用到的命名规则解析，跟项目代码无关
 * 作用：
 *   删除表前缀，如z_test数据表会转成Test实体类
 * @author Simon
 *
 */
public class MyStrategy extends DelegatingReverseEngineeringStrategy {
	
	private String packageName = "com.zlzkj.app.model";
	private String mainPrefix = "gt";
	
	public MyStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}
	
	@Override
	public String tableToClassName(TableIdentifier tableIdentifier) {
		
		String fullName = super.tableToClassName(tableIdentifier); //包含包名并已变成驼峰命名
		String arr[] = fullName.split("\\.");
		String className = arr[arr.length-1];
		int length = mainPrefix.length();
		className =  className.substring(length);
		return packageName+"."+className;
	}

}
