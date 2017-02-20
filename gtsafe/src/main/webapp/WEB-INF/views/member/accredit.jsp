<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="accredit" method='post'>
	<!-- 主体 -->
	<div class="p10 bdc mb10">
		<ul>
			
			<c:forEach items="${nodeList }" var="vo">
				<li class="mb20 f18">
					<span>${vo.alias }</span>
					<hr>
					<ul>
						<c:forEach items="${vo.node }" var="vo2">
							<li class="f14 mb5 J_check">
								<label><input type="checkbox" name="nodeId" class="father ml10" value="${vo2.id }" <c:if test="${vo2.isCheck==true}">checked=""</c:if>>&nbsp;${vo2.alias }</input>
								<c:if test="${vo2.node!=null }"><em>:</em></c:if></label>
								<c:forEach items="${vo2.node }" var="vo3">
									<label><input type="checkbox" name="nodeId" class="son ml10" value="${vo3.id }" <c:if test="${vo3.isCheck==true}">checked=""</c:if>>&nbsp;${vo3.alias }</input></label>
								</c:forEach>
							</li>
						</c:forEach>
					</ul>
				</li>
			</c:forEach>
		</ul>
	</div>
	<!-- 主体 -->
	<div class="form-actions tc">
		<input type="hidden" name="roleId" value="${roleId }" />
		<button type="submit"  class="btn btn-success">确定</button>
	</div>
</form>
