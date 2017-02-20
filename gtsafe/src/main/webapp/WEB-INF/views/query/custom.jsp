<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="jq-layout rel" data-options="fit:true" >
	<!-- 左侧 -->
	<div data-options="region:'west',border:true,split:true"
		style="padding-top:8px;width:230px;padding-left:5px">
		<div class="jq-layout rel" data-options="fit:true" >
			<div data-options="region:'center',border:true,split:true"
				style="padding-top:8px;width:230px;padding-left:5px">
				<select id="AllContents" name="AllContents" class="jq-combobox"
					data-options="required:true,onSelect:contentChange">
					<c:forEach items="${AllContents}" var="list"
						varStatus="number_list">
						<option value="${number_list.index}">${list}</option>
					</c:forEach>
				</select>
				<div class="btn-group mt5">
					<button type="button" id="query_find" class="btn btn-xs btn-default">构造查询</button>
					<button id="query_edit" type="button" class="btn btn-xs btn-default">
						<i class="icons icons-edit"></i>修改
					</button>
					<button id="query_del" type="button" class="btn btn-xs btn-default">
						<i class="icons icons-minus-sign"></i>删除
					</button>
					<button id="query_run" type="button" class="btn btn-xs btn-default">运行</button>
				</div>
				<div>
					<ul id="list" class="mt10">
					<li></li>
					</ul>
				</div>
			</div>
			
		</div>
	</div>
	<!-- 右侧 -->
	<div class="main-header" data-options="region:'center'"
		style="border:1px solid #d4d4d4;">
		<div class="jq-layout rel" data-options="fit:true" >
			<div class="main-header" data-options="region:'north'"
				style="border:1px solid #d4d4d4;">
				<div class="btn-group fr">
				<button id="query_explain" class="btn btn-default" type="button">自定义查询使用说明</button>
					<%--<button class="btn btn-default">报表设计</button>
					<button class="btn btn-default">报表打印</button>
					<button class="btn btn-default">直接打印</button>
					--%><button id="query_export" class="btn btn-default" type="button">导出到Excel</button>
				</div>
			</div>


			<div data-options="region:'center',border:false">
				<table id="result" class="jq-datagrid J_grid" fit="true"
					title="查询结果" data-options="
						method:'post',
						pagination: false,
						fitColumns:false
					">
				</table>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		 getList();
	})
	function getList() {
		$.post("query/list", function(data) {
			var list = $("#list");
			$("#list li").remove();
			for ( var i = 0; i < data.length; i++) {
				list.append("<li data-value="+data[i].id+" style='cursor: pointer;'>"+data[i].name+"<input type='hidden' value="+data[i].status+" ></li>");
			}
		}, "json");
	 }
	function contentChange(v) {
		
		var content = v.text;
		$.post("query/list",{
			content:content
		}, function(data) {
			var list = $("#list");
			$("#list li").remove();
			for ( var i = 0; i < data.length; i++) {
				list.append("<li data-value="+data[i].id+" style='cursor: pointer;'>"+data[i].name+"<input type='hidden' value="+data[i].status+" ></li>");
			}
		}, "json"); 
		console.log(content);
	 }
	//运行
	$("#query_run").on("click",function(){
		if(!$("#list li").hasClass('checked')){
			$.alert("请先选择查询项");
			return false;
		}
		$("#list li").each(function(){
			var val = $(this).data("value");
			if($(this).hasClass('checked')){
				//判断当前记录是否存在@@
				var status = $(this).find("input").val();
				if (status == 1) {
					openW("query/condition?id="+val, {
						title: "添加条件",
						width: "400"
					}, function() {
						$("#add_queryCondition").attr("action","query/condition?id="+val);
					});
				}else{
					//不存在@@直接执行
					$.post("query/run?id="+val,function(data){ 
						$("#result").datagrid({
							columns: [data.title],
							data: data.list
						})
					},"json")
				}
			}
		})
	})
	//左侧底部查询按钮
	$("#bt_query_find").on("click", function() {
		var bt_condition = $("#bt_condition").val();
		$.post("query/find",{
				value: bt_condition
		}, function(data) {
			var list = $("#list");
			$("#list li").remove();
			for ( var i = 0; i < data.length; i++) {
				list.append("<li data-value="+data[i].id+">"+data[i].name+"<input type='hidden' value="+data[i].status+" ></li>");
			}
		}, "json")
	})

	//点击导出到Excel
	$("#query_export").on("click", function() {
		var data = $("#result").datagrid("getData");
		var title = $("#result").datagrid("getColumnFields");
		title = JSON.stringify(title);
		data = JSON.stringify(data.rows);
		if(data=="[]"&&title=="[]"){
			$.alert("请先查询数据");
			return false;
		}
		$.confirm("确认要导出到Excel吗？", function(r) {
			$.post("query/export",{
				data : data,
				title : title
			}, function(data) {
				if (data.status == 1) {
					$.alert(data.info+"【<a style='color:red' target='_blank' href='"+ data.data +"'>点击下载表格</a>】");
				} else {
					$.alert(data.info);
				}
			},"json")
		})
	})

	//点击构造查询
	$("#query_find").on("click", function() {
		var v = $("#AllContents").combobox('getText');
		v = encodeURI(encodeURI(v));
		if (v == "") {
			$.alert("您还没有选择查询对象,不能构造查询");
			return false;
		}
		openW("query/select?content=" + v, {
			title : "通用查询",
			width : "900",
			height : "580"
		});
	});

	//点击修改
	$("#query_edit").on("click", function() {
		if(!$("#list li").hasClass('checked')){
			$.alert("请先选择查询项");
			return false;
		}
		$("#list li").each(function() {
			var val = $(this).data("value");
			if ($(this).hasClass('checked')) {
				openW("query/edit?id="+val, {
					title : "修改",
					width : "900",
					height : "580",
					id:val
				});
			}
		})
	})

	//点击删除
	$("#query_del").on("click", function() {
		if(!$("#list li").hasClass('checked')){
			$.alert("请先选择查询项");
			return false;
		}
		$("#list li").each(function() {
			var val = $(this).data("value");
			if ($(this).hasClass('checked')) {
				$.confirm("确定要删除吗？", function(r) {
					$.post("query/del?id=" + val, function(data) {
						if (data.status == 1) {
							$.alert(data.info);
							getList();
						} else {
							$.alert(data.info);
						}
					},"json")
				})
			}
		})
	})
	$("#query_explain").on("click", function() {
		openW("query/explain", {
				title : "说明",
				width : "400",
				height : "200",
		});
	})
	//选中保存的标题
	$("#list").on("click","li", function() {
		var val = $(this).data("value");
		$(this).addClass("checked").siblings("li").removeClass("checked");
	});
</script>