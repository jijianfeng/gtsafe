package com.zlzkj.core.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.zlzkj.core.utils.CoreUtils;

/**
 * 备份数据库类
 * @author twentwo
 *
 */
public class DBUtils {
	
	private static final String CONFIG_FILE = "jdbc.properties";
	/**
	 * 备份数据库  以压缩格式GZ保存
	 * @param dbName 数据库名
	 * @param targetPath 存储路径
	 * @return
	 */
	public static Map<String,Object> backupDBToSql(String dbName,String targetPath) {
		
		int status = 0;  //-1:错误，0:初始状态，1:数据库备份成功
		String errorMsg = ""; //status=-1时的错误信息
		long gzipSize = 0; //备份文件大小
		String saveName = ""; //备份文件保存名，不含路径
		String executeCmd = ""; //mysqldump命令
		
		try {
			String fileSeparator = System.getProperty("file.separator");//获取系统文件分隔符
			String OS = System.getProperty("os.name").toLowerCase();//获取系统名称
			String dbUser = DBUtils.getConfig("jdbc.username");
			String dbPass = DBUtils.getConfig("jdbc.password");
			String dbHost = DBUtils.getConfig("jdbc.url").split("/")[2].split(":")[0];
			
			String name = DBUtils.getConfig("jdbc.url").split("/")[3];
			name=name.substring(0, name.indexOf("?"));
			
			String folderPath = targetPath + fileSeparator + "backup";
	        
	        /*NOTE: Creating Folder if it does not exist*/
	        File f1 = new File(folderPath);
	        f1.mkdir();
	        
	        //保存文件名 时间格式
	        saveName = CoreUtils.formatTimestamp(CoreUtils.getNowTimestamp(), "yyyyMMdd-HHmmss");
	        String savePath = folderPath + fileSeparator + saveName+".sql.gz";
	        
	        /*NOTE: Used to create a cmd command*/
	        executeCmd = "mysqldump --no-defaults -h" + dbHost + " -u" + dbUser + " -p" + dbPass + 
	        		" --add-drop-database -B " + name + " > " + "\""+savePath+"\"";
	        
	        //System.out.println(executeCmd);
	        
	        //区分windows与unix系的系统命令
	        //String cmdA = (OS.indexOf("win") >= 0) ? "cmd /c "+executeCmd : "/bin/sh -c "+executeCmd; 
	        String[] cmdA = new String[3];
	        if(OS.indexOf("win") >= 0){
				cmdA[0] = "cmd";			
				cmdA[1] = "/c";			
				cmdA[2] = executeCmd;			
			}else {
				cmdA[0] = "/bin/sh";			
				cmdA[1] = "-c";			
				cmdA[2] = executeCmd;
			}
	        
	        /*NOTE: Executing the command here*/
			Process runtimeProcess = Runtime.getRuntime().exec(cmdA);
			String exeMsg = getErrMsg(runtimeProcess).toLowerCase();
			int	processComplete = runtimeProcess.waitFor();
			//0表示正常终止
	        if (processComplete == 0 && exeMsg.indexOf("error") < 0 &&
	        		exeMsg.indexOf("errno") < 0 && exeMsg.indexOf("command not found")<0 ) {
	        	status = 1;
	        	File sqlFile = new File(savePath);
	        	gzipSize = sqlFile.length();
	        } else {
	        	errorMsg = "数据库备份失败！原因："+exeMsg;
	        }
	        
		} catch (IOException e) {
			status = -1;
			errorMsg = "系统发生异常！执行命令:"+ executeCmd +" IOException: " + e.getCause();
		} catch (InterruptedException e) {
			status = -1;
			errorMsg = "系统发生异常！执行命令:"+ executeCmd +" InterruptedException: " + e.getCause();
		}
		
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("status", status);
		map.put("errorMsg", errorMsg);
		map.put("gzipSize", gzipSize);
		map.put("saveName", saveName);
		
		return map;
	}
	
	/**
	 * 获取配置文件的值
	 * @param key
	 * @return
	 */
	public static String getConfig(String key){
		ClassLoader loader = DBUtils.class.getClassLoader();
		InputStream in = loader.getResourceAsStream(CONFIG_FILE);
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
	
	/**
	 * 字节转换
	 * @param bytes
	 * @param si 是否以十进制前缀换算
	 * @return
	 */
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
    /** 
     * 获取cmd命令执行的错误信息 
     * @param p Process对象 
     * @return  错误信息字符串 
     * @throws IOException 文件读写错误 
     */  
    private static String getErrMsg(Process p) throws IOException{  
        StringBuilder errMsg = new StringBuilder();  
        InputStream in = p.getErrorStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
        for(String inStr = null;(inStr = br.readLine()) != null;){  
            errMsg.append(inStr).append("<br>");  
        }  
        in.close();  
        br.close();  
        return errMsg.length() == 0 ? "" : errMsg.toString();  
    }

}
