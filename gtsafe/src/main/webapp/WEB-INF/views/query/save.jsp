<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div id="load" class="messager-button">
	<ul id="titleArea">
		<li class="fl ml30 mb10">
			<span class="f14">请输入需要保存的名称</span>
		</li>
		<li></li>
		<li class="ml-3">
			<input type="text">
		</li>
	</ul>
</div>
<div class="messager-button">
	<button class="btn btn-success" id="add_query_ok"><i class="icons icon-white icons-ok"></i>确定</button>
	&nbsp;&nbsp;&nbsp;
	<button class="btn btn-default" id="add_query_cancel"><i class="icons icons-remove"></i>取消
	</button>
</div>

<script>
	//加载页面
	var select = '${select}';
	var where = '${where}';
	var fieldName = '${fieldName}';
	var content = '${content}';
	var id = '${id}';
	var title = '${title}';
	$(document).ready(function(){
		if (title != "") {
			$("#titleArea li input").val(title);
		};
	})
	
	//确定
	$("#add_query_ok").on("click",function(){
		title = $("#titleArea li input").val();
		$.post("query/add",{
			content:content,
			where:where,
			select:select,
			fieldName:fieldName,
			title:title,
			id:id
		},function(data){
			if (data.status == 1) {
				$.alert(data.info);
				getList();
				ViewDialog.dialog('destroy');
			} else {
				$.alert(data.info);
			}
		},"json")
		
	})

	//取消
	$("#add_query_cancel").on("click",function(){
		ViewDialog.dialog('destroy');
	})
</script>
