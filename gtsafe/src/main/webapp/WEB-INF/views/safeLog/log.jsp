<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<ul>
<input type ="hidden" name ="logId" value="${logId}"> 
<input type ="hidden" name ="addTime" value="${addTime}">
	<c:forEach items="${tempList}" var="pid" varStatus="number_pid">
		<li class="mb20">
			<span class="f18 J_collapse">
				<a class='collapse-icon ${pid.NoCate!=1?"on":""}' href="javascript:;"></a>
				${number_pid.index+1}.${pid.name}
			</span>
			<hr />
			<ul class="${pid.NoCate==1?'dn':''} J_leaf J_parent">
				<c:forEach items="${pid.cate}" var="cate" varStatus="number_cate">
					<li class="mb5">
						<span class="J_collapse">
							<a class="collapse-icon" href="javascript:;"></a>
							${number_pid.index+1}.${number_cate.index+1}${cate.name }
						</span>
						<ul class="mt10 dn J_leaf J_node">
							<c:forEach items="${cate.contact}" var="contact" varStatus="number_contact">
								<li class="mb5">
									<input type="hidden" value="${contact.id}" name="ids" />
									<c:if test="${contact.type==1 }">
										<div class="h50 pct100 bdc bg-gray">
											<span class="ml10">
												<em>${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}</em>
												${contact.name}
											</span>
											<br />
											<c:forEach items="${contact.attr}" var="attr">
												<label class="radio inline ml30">
													<input ${contact.val==attr.id?"checked":""} type="radio" value="${attr.id}" name="contact_${contact.id}" />${attr.name}<input class="J_info" type="hidden" data-score="${attr.score}" data-important="${attr.important}" data-title="${contact.encodeName}" data-name="${attr.encodeName}" data-step="${attr.encodeMeasures}" />
												</label>
											</c:forEach>
										</div>
									</c:if>
									<c:if test="${contact.type==0 }">
										<div class="h110 pct100 bdc bg-gray J_input">
											<span class="ml10">
												<em>${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}</em>
												${contact.name}
											</span>
											<textarea name="contact_${contact.id}" cols="30" rows="3" class="pct95 ml20 mt10">${contact.val }</textarea>
										</div>
									</c:if>
									<c:if test="${contact.type==2}">
										<div class="h110 pct100 bdc bg-gray J_input" >
											<span class="ml10">
												<em>${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}</em>
												${contact.name}
											</span>
<textarea name="contact_${contact.id}" cols="30" rows="3" class="pct95 ml20 mt10"><c:forEach items="${contact.attr}" var="attr" varStatus="status">
${attr.name}</c:forEach></textarea>
										<br><br>
										</div>
									</c:if>
								</li>
							</c:forEach>
						</ul>
					</li>
				</c:forEach>

				<c:if test="${pid.NoCate==1 }">
					<c:forEach items="${pid.contact}" var="contact" varStatus="number_contact">
						<li class="mb5">
							<input type="hidden" value="${contact.id}" name="ids" />
							<c:if test="${contact.type==1 }">
								<div class="h50 pct100 bdc bg-gray">
									<span class="ml10">
										<em>${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}</em>
										${contact.name}
									</span>
									<br />
									<c:forEach items="${contact.attr}" var="attr">
										<label class="radio inline ml30">
											<input ${contact.val== attr.id?"checked":""} type="radio" value="${attr.id}" name="contact_${contact.id}" />${attr.name}<input class="J_info" type="hidden" data-score="${attr.score}" data-important="${attr.important}" data-title="${contact.encodeName }" data-name="${attr.encodeName}" data-step="${attr.encodeMeasures}" />
										</label>
									</c:forEach>
								</div>
							</c:if>
							<c:if test="${contact.type==0 }">
								<div class="h110 pct100 bdc bg-gray J_input">
									<span class="ml10">
										<em>${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}</em>
										${contact.name}
									</span>
									<textarea name="contact_${contact.id}" cols="30" rows="1" class="pct95 ml20 mt10">${contact.val}</textarea>
								</div>
							</c:if>
						</li>
					</c:forEach>
				</c:if>
			</ul>
		</li>
	</c:forEach>
</ul>
<script>
	$(".J_collapse").on("click", function() {
		$(this).find(".collapse-icon").toggleClass("on");
		$(this).siblings(".J_leaf").toggle();
	});
</script>