package com.zlzkj.app.shiro;

import java.util.List;
import java.util.Map;

import com.zlzkj.app.service.UserService;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;



public class DBRealm extends AuthorizingRealm {
	@Autowired
	protected UserService userService;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken token2 = (UsernamePasswordToken) token;
		String account = token2.getUsername();
        //查出是否有此用户   
        if(!account.equals("")){  
            //若存在，将此用户存放到登录认证info中  
            return new SimpleAuthenticationInfo(token2.getUsername(), token2.getPassword(), getName());
        }  
        return null;  
		//return super.doGetAuthenticationInfo(token);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String currentUsername = (String)super.getAvailablePrincipal(principals);  
		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo(); 
		//实际中可能会像上面注释的那样从数据库取得  
        if(null!=currentUsername){
            //添加一个角色,不是配置意义上的添加,而是证明该用户拥有admin角色    
            //simpleAuthorInfo.addRole("admin");  
            //添加权限  
        	List<Map<String, Object>> listData = userService.getRoleAll(currentUsername);
        	//System.out.println("\n-----"+userService.getRoleAll("furnace"));
        	//List<Map<String, Object>> roleList = userService.getRoleAll("furnace");
        	for(Map<String, Object> one:listData){
        		simpleAuthorInfo.addStringPermission("/"+one.get("name").toString());
        	}
        	//无需权限验证
        	if (currentUsername.equals("admin")) {
        		simpleAuthorInfo.addStringPermission("/member/accredit");
			}
        	simpleAuthorInfo.addStringPermission("/contact/contactImport");
        	simpleAuthorInfo.addStringPermission("/member/roleList");
        	simpleAuthorInfo.addStringPermission("/member/saveNode");
        	simpleAuthorInfo.addStringPermission("/member/editPassword");
        	simpleAuthorInfo.addStringPermission("/repository/cateList");
        	simpleAuthorInfo.addStringPermission("/logTpl/cateList");
//        	simpleAuthorInfo.addStringPermission("/safeLog/saveNow");
        	simpleAuthorInfo.addStringPermission("/safeLog/selectTime");
        	simpleAuthorInfo.addStringPermission("/safeLog/getImageList");
//        	simpleAuthorInfo.addStringPermission("/safeLog/data");
//        	simpleAuthorInfo.addStringPermission("/safeLog/preview2");
//        	simpleAuthorInfo.addStringPermission("/safeLog/preview");
        	simpleAuthorInfo.addStringPermission("/safeLog/score");
        	simpleAuthorInfo.addStringPermission("/safeLog/score2");
        	simpleAuthorInfo.addStringPermission("/safeLog/reason");
        	simpleAuthorInfo.addStringPermission("/safeLog/backup");
        	simpleAuthorInfo.addStringPermission("/safeLog/export");
//        	simpleAuthorInfo.addStringPermission("/safeLog/yesterday");
        	simpleAuthorInfo.addStringPermission("/safeLog/log");
        	
        	
        	
        	simpleAuthorInfo.addStringPermission("/contact/cateContents");
        	simpleAuthorInfo.addStringPermission("/contact/listContactCate");
        	simpleAuthorInfo.addStringPermission("/contact/addCateContents");
        	simpleAuthorInfo.addStringPermission("/system/editorUpload");
        	simpleAuthorInfo.addStringPermission("/api/getGTSafeInfo");
        	simpleAuthorInfo.addStringPermission("/query/value");
        	simpleAuthorInfo.addStringPermission("/query/build");
        	simpleAuthorInfo.addStringPermission("/query/where");
        	simpleAuthorInfo.addStringPermission("/query/run");
        	simpleAuthorInfo.addStringPermission("/query/soon");
        	simpleAuthorInfo.addStringPermission("/query/title");
        	simpleAuthorInfo.addStringPermission("/query/list");
        	simpleAuthorInfo.addStringPermission("/query/condition");
        	simpleAuthorInfo.addStringPermission("/query/find");
        	simpleAuthorInfo.addStringPermission("/query/explain");
        	simpleAuthorInfo.addStringPermission("/query/select");
        	simpleAuthorInfo.addStringPermission("/query/export");
            return simpleAuthorInfo;  
        }
		return null;
		//return super.doGetAuthorizationInfo(principals);
	}

}
