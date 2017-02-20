<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="p-box">
	<div class="bd system">
	<c:if test="${open==1 }">加载查询目录出错,请先确保项目根目录下存在自定义查询的示例文件夹,如果没有请先添加,然后删除gtsafe-data文件夹下的 custom-ini文件夹,再重新刷新</c:if>
	<c:if test="${open==2 }">项目-data文件夹下的 custom-ini 文件夹 和 自定义查询的示例文件夹都不存在,请先添自定义查询的示例文件夹到项目根目录,不然无法使用,然后重新刷新</c:if>
	<c:if test="${open!=1&&open!=2}">初始加载完后,可以去服务器更目录找到,项目-data文件夹下的 custom-ini文件夹中,里面有几个样例,可以根据自己的需求更改</c:if>
	</div>
</div>