<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="p-box">
	<div class="hd">[${time}]的日志驳回理由</div>
	<div class="bd system">
		<c:forEach items="${reason}" var="reason" varStatus="number_reason">
			${number_reason.index+1}&nbsp;&nbsp;：&nbsp;&nbsp;审核人：&nbsp;&nbsp;${reason.auditor}&nbsp;&nbsp;，驳回时间：${reason.addTime}，驳回理由：${reason.reason } <br>
		</c:forEach>
	</div>
</div>