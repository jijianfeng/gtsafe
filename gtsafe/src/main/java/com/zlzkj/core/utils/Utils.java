package com.zlzkj.core.utils;

import org.apache.commons.codec.digest.DigestUtils;

import com.zlzkj.core.constants.Constants;

public final class Utils {

	public static String encryptPassword(String pwd){
//		String a = DigestUtils.md5Hex(Constants.PASSWORD_SALT+DigestUtils.md5Hex(pwd));
		return DigestUtils.md5Hex(Constants.PASSWORD_SALT+DigestUtils.md5Hex(pwd));
	}
	
	public static boolean isPasswordMatch(String pwd,String dbPwd){
		return dbPwd.equals(encryptPassword(pwd));
	}
}
