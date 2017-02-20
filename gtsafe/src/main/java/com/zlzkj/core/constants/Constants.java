package com.zlzkj.core.constants;

/**
 * 全局系统常量
 * @author Simon
 *
 */
public class Constants {
	
	//用户登录密码杂质
	public static String PASSWORD_SALT = "zlzkj.com";
	
	//用户标识
	public static String USER_ID = "userId";
	public static String USER_INFO = "userInfo";
	
	//css/js/image等网站静态资源目录，跟spring配置文件中一致
	public static String STATIC_PATH = "/static";
	
	//伪静态后缀
	public static String URL_SUFFIX = ".html";
	
	//网站context path - 在CoreController中赋值
	public static String CONTEXT_PATH;
	
	//方法返回错误时，错误的特征码，用来判断返回值是否为错误
	public static String ERROR_FLAG = "error:";
	
	//调试模式  开启:无需超级狗
	public static boolean IS_DEBUG = true;
	
	//超级狗开发商代码
	public static String VENDOR_CODE = "8FslGTjPCmRDIAuHsP" +
			"BZkinRVTVoz9OVJ1GvIGsgpqTidFXiYwC8itW1rBqMwsS" +
			"twD8fcvwG0mXFMFnTQtzdXq254J5T8qsQaM93B4swE3W+" +
			"NXV9Q7+HXfFUlmS6OcaR5n1eb9LPSxJ7KUHZDCRpIu427" +
			"yaCHns7hmgcn+Wbxh7DnIVP+JPLh+C/S3ItijZUobT4NE" +
			"5K9jjg5VF7vSom33iUFhu3zEFIAI2g4xzBFkuOSmyVEkS" +
			"k7wQkIvZD4+KlOUL+Bl9NzA7yDKSoIwl/NxTnqVV4fiZQD" +
			"+oAZ0al2NDP0dpa/NBZhRQUZdhVMap2CsmaCeYveryMd5M" +
			"H7WewDua2plYaxYwfpxc9k1W1r4YZsFOjbP+GwesAyWyze" +
			"VGQuji32oc7p5tKErvM8bPhKgZocXX9Sy46IAiaR8Qndjm" +
			"RV0kVpeFoNduKrE/TWTpRvmYAClf9YvYmfVe/YB1yX/kXL" +
			"j7xItSve29xNIk/aR6Ruwk9LzaKV4oBdIKO/s0uPpK2Zcv" +
			"NqLnt6GqJKRWCrwGsF1+HCpwvs3Z8OEJKPMT+iGOKacMwq" +
			"hgZ3sZQl47kwQb56W6U6MZGILZismQSRS8tDzuK/aFfAST" +
			"UbY6Ei89xQ/u8/n6uK/OjWPnwjr4R/0Wvb0ocW3DOk5qax" +
			"EOvcLncb/wibszLV4E9Yo/PCmfI469GX27JXa9dgeaLMXU" +
			"9U7WyWnr0Z+zSAg43MAQmTXvUHxgSb5gcuMjU8MzjSYRo+" +
			"8FN6cmkxUZNqO+f/Ad5qN05zswEmtts8sHybV7zA4VOO95" +
			"QY4GdPhVN2Xityg8/8o3pHEU+XuTCNIWRG7CGUF+AIiDbw" +
			"1SjXPmozQtAXHiCbvmA6rFE1Cbg9mVmcB+zqQnmrEfoMmI" +
			"5ty2+EP8b6OEd17iujCKZChfDTXkl/ozxsyhX5bn356Eb" +
			"vmoeYKGms40I8eMqDJNCe/hO92qSFz3s+WqWSy002Mbvm/byuA==";
	
	//产品ID
	public static String PRODUCT_SCOPE = "<dogscope>\n" +
		      " <product id=\"1\"/>\n" +
		      "</dogscope>\n";
	
	//特征ID
	public static int FEATURE_ID = 54321;
	
}
