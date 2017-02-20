<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="jq-layout rel" data-options="fit:true" id="tpl_content">
	<!-- 上 -->
	<div data-options="region:'north',border:true" style="height:60px;align:center">
		<form method="post" class="ml40" id="select_form">
			<div class="w200 fl">
				<em>查询条件</em> <input id="condition" name="field" class="jq-combobox" type="text"
					data-options="{
							required:true,
							method:'post',
							onChange:changeCate
						}">
			</div>
			<div class="w100 fl mr15">
				<em>比较符</em> <select name="compare" class="jq-combobox w90"
					data-options="required:true,onChange:changeCompare">
					<c:forEach items="${compare}" var="list" varStatus="number_list">
						<option value="${list.val}">${list.compare}</option>
					</c:forEach>
				</select>
			</div>
			<div class="w200 fl">
				<em>比较值</em> <input id="compare_val" name=""
					class="jq-combobox" type="text"
					data-options="{
							required:true,
							method:'post'
						}">
			</div>
			<div class="w100 fl">
				<em>逻辑值</em> <select id="logic" name="logic" class="jq-combobox w90"
					data-options="required:true">
					<option value="0">请选择</option>
					<option value="AND">并且</option>
					<option value="OR">或者</option>
				</select>
			</div>
			
			<button	id="add_where" class="btn btn-default mt15" type="button">增加条件</button>
			<button	id="clear_where" class="btn btn-default mt15" type="button">清空条件</button>
		</form>
		<em class="cl"></em>
	</div>
	<!-- 左 -->
	<div data-options="region:'west',border:true" style="width:200px">
		<div class="fl ml5 mt5 mr2">
			<select name="" class="jq-combobox w120 fl" id="">
				<option value="0">字段查询</option>
			</select>
		</div>
		<div class="btn-group mt7">
			<button id="all_field" class="btn btn-xs btn-default">A</button>
			<button id="opposite_field" class="btn btn-xs btn-default">N</button>
			<button id="clear_field" class="btn btn-xs btn-default">C</button>
		</div>

		<ul id="J_checkbox" class="ml10 mt10">
			<c:forEach items="${fieldNames}" var="list" varStatus="number_list">
				<li><input type="checkbox" value="${list.field}" ${list.check==1?"checked":""}>${list.fieldName}</li>
			</c:forEach>
		</ul>
		<button id="toCenter" class="btn btn-default"
			style="position:absolute;right:10px;bottom:10px">=></button>

	</div>

	<!-- 右 -->
	<div data-options="region:'east',border:true" style="width:160px">
		<div class="btn-group m10">
			<button id="query_soon" class="btn btn-default m10">立即查询</button>
			<button id="query_save" class="btn btn-default m10">保存查询</button>
			<button id="query_empty" class="btn btn-default m10">清空查询</button>
			<button id="query_cancel" class="btn btn-default m10">取消查询</button>
		</div>
	</div>

	<!-- 中 -->
	<div data-options="region:'center',border:true" style="width:510px">
		<div id="innerSQL" class="jq-panel" title="innerSQL" style="width:514px;height:451px;">
			<p id="select_sql">${select}</p>
			<p id="where_sql">${where}</p>
			<%--<textarea name="" id="where_sql" class="bd-none" style="overflow:hidden;resize:none;width:470px;height:160px;box-shadow:inset 0 0px 0px rgba(0, 0, 0, 0);"></textarea>
		--%>
		</div>
	</div>
</div>
<script>
	var content = '${content}';
	content = encodeURI(encodeURI(content));
	//组装Select
	$("#toCenter").on("click", function() {
		var str = getChecked();
		if(str == ""||str.length==0){
			$.alert("请先选择查询字段");
			return false;
		}
		$.post("query/build?content="+content,{
			fieldNames : str,
		}, function(data) {
			var sql_input = $("#select_sql");
			sql_input.html(data);
		},"json");
	});
	//组装where
	$("#add_where").on("click", function() {
		var v = $("#compare_val").combobox("getText");
		v = encodeURI(encodeURI(v));
		var where_sql = $("#where_sql").text();
		where_sql = encodeURI(encodeURI(where_sql));
		$("#select_form").attr("action", "query/where?compareVal="+v+"&whereSql="+where_sql+"&content="+content);
		$("#select_form").form({
			success : function(data) {
				var data = new Function("return" + data)();
				if (data.status == 1) {
					$("#where_sql").html(data.data);
				} else {
					$.alert(data.info);
				}
			}
		});
		$("#select_form").submit();
	});
	
	//立即查询
	$("#query_soon").on("click", function() {
		var select = $("#select_sql").text();
		var where = $("#where_sql").text();
		var str = getChecked();
		if(select==""){
			$.alert("请选择左边的条件并点击下方按钮");
			return false;
		}
		if(where.indexOf("@@") >= 0){
			$.alert("立即查询请勿输入@@");
			$("#where_sql").html("");
			return false;
		}
		$.confirm("确定要立即查询吗？", function(r) {
			$.post("query/soon?content="+content,{
				select:select,
				where:where,
				fieldNames:str
			},function(data){
				str = encodeURI(encodeURI(str));
				$("#result").datagrid({
					columns: [data.field],
					data: data.list
				});
			},"json");
			ViewDialog.dialog('destroy');
		})
	})
	//保存事件
	$("#query_save").on("click",function(){
		var select = $("#select_sql").text();
		select = encodeURI(encodeURI(select));
		var where = $("#where_sql").text();
		where = encodeURI(encodeURI(where));
		var str = getChecked();
		str = encodeURI(encodeURI(str));
		ViewDialog.dialog('destroy');
		openW("query/add?content="+content+"&where="+where+"&select="+select+"&fieldNames="+str+"&id="+'${id}', {
			title: "提示",
			width: "280",
			height: "180"
		});
	})
	
	//清空条件
	$("#clear_where").on("click",function(){
		$("#where_sql").html("");
	})
	//清空查询
	$("#query_empty").on("click", function() {
		$("#J_checkbox li input").each(function() {
			$(this).prop("checked", false);
		})
		getCondition();
		$("#innerSQL").children().html("");
	})

	//取消查询
	$("#query_cancel").on("click", function() {
		ViewDialog.dialog('destroy');
	})

	//逐个点击事件
	$("#J_checkbox").on("click", "input", function() {
		getCondition();
	});

	//全选事件
	$("#all_field").on("click", function() {
		$("#J_checkbox li input").each(function() {
			$(this).prop("checked", true);
		})
		getCondition();
	});

	//反选事件
	$("#opposite_field").on("click", function() {
		$("#J_checkbox li input").each(function() {
			if (this.checked) {
				$(this).prop("checked", false);
			} else {
				$(this).prop("checked", true);
			}
		})
		getCondition();
	});

	//清空事件
	$("#clear_field").on("click", function() {
		$("#J_checkbox li input").each(function() {
			$(this).prop("checked", false);
		})
		getCondition();
	});
	function getChecked() {
		var len = $("#J_checkbox li input").length;
		var checkboxs = $("#J_checkbox li input");
		var result = [];
		var str;
		
		for ( var i = 0; i < len; i++) {
			if (checkboxs[i].checked) {
				var val = checkboxs[i].value;
				result.push(val); //数组赋值
			}
		}
		str = result.join(","); //数组转字符串
		return str;
	}
	//获取条件
	function getCondition() {
		var str = getChecked();
		str = encodeURI(encodeURI(str));
		$("#condition").combobox({
			url : 'query/select?fieldNames=' + str + '&content=' + content,
		});
		$("#compare_val").combobox('clear');
	}
	//查询条件点击事件
	function changeCate(r) {
		$("#compare_val").combobox({
			url : 'query/value?field=' +r+ '&content=' + content,
		});
	}
	$(function() { 
		setTimeout(function() {
			getCondition();
        }, 500);
	});
	
	function changeCompare(r) {
		if(r=="Like")
			$("#compare_val").combobox('clear');
	}
</script>