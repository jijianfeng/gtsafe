package com.zlzkj.core.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;



/**
 * 验证、生成签名和用Token的工具类
 * @author Simon
 *
 */
public class TokenUtils {
		
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TokenUtils.class);
	
	
	//常量定义
	public static String APP_KEY = "appKey";
	public static String SIGN = "sign";
	public static String TIMESTAMP = "timestamp";
	public static String ACCESS_TOKEN = "accessToken";
	
	/**
	 * 合法的appKey和appSceret
	 */
	public static String[] allowKeys = {"21725276","10000001"};
	public static String[] allowScerets = {"d6ac06b713fd5b73578fab023d37b09e","96e79218965eb72c92a549dd5a330112"};
	
	/**
     * 系统级别参数列表
     * @var array
     */
	public static String[] sysParamsList = {APP_KEY,SIGN,TIMESTAMP};
	
	
	/**
	 * 根据appKey获取appSceret
	 * @return return null if can not find the appScerets
	 */
	public static String getAppSceret(String appKey){
		int i = Arrays.asList(allowKeys).indexOf(appKey);
		if(i!=-1){
			return allowScerets[i];
		}else{
			return null;
		}
	}
	
	/**
	 * 检查参数
	 * @param apiParamsList api级别的参数
	 * @param paramMap 所有请求参数
	 * @return
	 */
	public static Map<String,Object> checkParams(Map<String,String> paramMap){

		Map<String,Object> json = new HashMap<String,Object>();
		json.put("data", null);
		json.put("info", null);
		json.put("status", 0);
		
		//检查系统参数参数完整性
		Set<String> keySet = paramMap.keySet();
		String[] requestParamsList = new String[keySet.size()];
		keySet.toArray(requestParamsList);
		for(String one:sysParamsList) {
            if(!Arrays.asList(requestParamsList).contains(one)){
                json.put("info","Missing system param:"+one);
                return json;
            }
        }
		
		//获取appSecret
		String appSecret = getAppSceret(paramMap.get("appKey"));
		if(appSecret!=null){
			//检查签名是否正确
			if(paramMap.get(SIGN).equals(createSign(appSecret,paramMap))){
				//签名验证成功后,验证请求时间,允许前后误差各不超过5分钟即300秒
				int timeMissing = Integer.parseInt(paramMap.get(TIMESTAMP)) - CoreUtils.getNowTimestamp();
				if(timeMissing<300 && timeMissing>-300){
					//最后验证通过
					json.put("status",1);
					return json;
				}else{
					json.put("info","您的设备本地时间误差超过5分钟,请调整后重试");
					return json;
				}
			}else{
				json.put("info","Param sign is illegal");
				return json;
			}
		}else{
			json.put("info","Param appKey is illegal");
			return json;
		}
		
	}
	
	
	
	/**
	 * 生成数据签名
	 * 算法：
	 * 1.排除参数sign、排除空值、排除文件
	 * 2.把参数按照名称a-z做升序排序
	 * 3.所有参数的键和值相连，然后首尾加上appSceret，最后md5
	 */
	public static String createSign(String appSecret,Map<String,String> paramMap){
		//排除传过来的sign参数后签名
		paramMap.remove("sign");
		
		//把参数按照名称a-z做升序排序
		Object[] keys = paramMap.keySet().toArray();
	    Arrays.sort(keys);
	    
	    String stringToBeSigned = appSecret;
	    for(Object one:keys){
	    	String value = paramMap.get(one.toString());
	    	//排除空值
	    	//TODO 排除文件
	    	if(value!=null && !value.isEmpty()){
	    		stringToBeSigned += one.toString() + paramMap.get(one.toString());
	    	}
		}
	    stringToBeSigned +=appSecret;
	    
	    //logger.info(stringToBeSigned);
		return DigestUtils.md5Hex(stringToBeSigned).toLowerCase();
	}
	
	
	
}
