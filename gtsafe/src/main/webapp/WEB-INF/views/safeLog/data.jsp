<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="add_safeLog" method='post'>

<!-- date -->
<div class="p10 bdc mb10">
	<ul>
		<li class="tc">
			<span class="f18">[${serviceTime }]日志内容
			<!-- 新增预览图片功能 -->
			<a href="javascript:;" class="icons icon-search J_pop" id="lookImage" title="预览图片" data-type="preview" data-index="0"></a>
			</span>
		</li>		
	</ul>
</div>
<!-- date -->

<!-- 主体 -->
<div class="p10 bdc mb10">
    <ul>
	<c:forEach items="${tempList}" var="pid" varStatus="number_pid">
		<li class="mb20"><span class="f18">${number_pid.index+1}.${pid.name}</span>
			<hr>
			<ul>
				<c:forEach items="${pid.cate}" var="cate" varStatus="number_cate">
					<li class="mb5"><span>${number_pid.index+1}.${number_cate.index+1}
							${cate.name }</span>
						<ul class="mt10">
							<c:forEach items="${cate.contact}" var="contact"
								varStatus="number_contact">
								<li class="mb5"><input type="hidden" value="${contact.id}"
									name="ids" /> 
									<c:if test="${contact.type==1 }">
										<div class="h50 pct100 bdc bg-gray">
											<span class="ml10">${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}
												${contact.name}</span> <br>
											<c:forEach items="${contact.attr}" var="attr">
												<label class="radio inline ml30" > <input
													<c:if test="${contact.val== attr.id}">checked=""</c:if>
													type="radio" value="${attr.id}"
													name="contact_${contact.id}" disabled="disabled"> ${attr.name}
													
													</label>
											</c:forEach>
										</div>
									</c:if> 
									<c:if test="${contact.type==0 }">
										<div class="h110 pct100 bdc bg-gray">
											<span class="ml10">${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}
												${contact.name}</span>
											<textarea name="contact_${contact.id}" cols="30" rows="3"
												class="pct95 ml20 mt10" disabled="disabled">${contact.val }</textarea>
										</div>
									</c:if>
									
									<!-- 预置类型填空题 -->
									<c:if test="${contact.type==2 }">
										<div class="h110 pct100 bdc bg-gray">
											<span class="ml10">${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}
												${contact.name}</span>
											<textarea name="contact_${contact.id}" cols="30" rows="3"
												class="pct95 ml20 mt10" disabled="disabled">${contact.val }</textarea>
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
											<span class="ml10">${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}${contact.name}</span> <br>
											<c:forEach items="${contact.attr}" var="attr">
												<label class="radio inline ml30"> <input
													<c:if test="${contact.val== attr.id}">checked=""</c:if>
													type="radio" value="${attr.id}"
													name="contact_${contact.id}" disabled="disabled"> ${attr.name}
													</label>
											</c:forEach>
										</div>
									</c:if> <c:if test="${contact.type==0 }">
										<div class="h110 pct100 bdc bg-gray">
											<span class="ml10">${number_pid.index+1}.${number_cate.index+1}.${number_contact.index+1}
												${contact.name}</span>
											<textarea name="contact_${contact.id}" cols="30" rows="3"
												class="pct95 ml20 mt10" disabled="disabled">${contact.val}</textarea>
										</div>
									</c:if>
								</li>
							</c:forEach>
					</c:if>
			</ul>
		</li>
	</c:forEach>
</ul>
    </div>
<div class="tc">
				<button class="btn btn-default J_close" type="button">关闭</button>
		</div>
<!-- 主体 -->
</form>
<input type="hidden" id="logId" value="${logid}" >
<script>
$("#lookImage").click(function(){
	$.ajax({
	     type : 'POST',
	     //data : {'gal' : $(this).attr('rel')},
	     url : "${z:u('/api/getImageList')}?logId="+${logId},
	     dataType: 'json',
	     complete: function(data) {
	       var dataX = data.responseText;
	       dataX = dataX.replace(/\"/g, "").replace(/href/g, "\"href\"").replace(/\'/g, "\""); //  /g表示替换全部，先去掉""，然后加上""，形成标准json
//	       alert(dataX);
// 	       var img = [
// 	         {href:'/gtsafe-data/image/20160311/vOPRqChEJw_!!1744x2896.jpg'},
// 	         {href:'/gtsafe-data/image/20160311/VoFrQWHoTo_!!1744x2896.jpg'},
// 	         {href:'/gtsafe-data/image/20160311/leFrqChetF_!!1273x797.jpg'},
// 	         {href:'/gtsafe-data/image/20160311/BephgwrEJg_!!1077x664.png'},];
	       var dataXsplit = dataX.split(',');
	       var dataXarrayObj = new Array();
	       for(var i=0;i<dataXsplit.length;i++){
	    	   dataXarrayObj[i] = $.parseJSON(dataXsplit[i]);
	       }
	       var opts = {
	         prevEffect : 'none',
	         nextEffect : 'none',
	         helpers : {
	           thumbs : {
	             width: 75,
	             height: 50
	           }
	         }
	       };
	       $.fancybox(dataXarrayObj, opts);
	      }
	   });
});
</script>