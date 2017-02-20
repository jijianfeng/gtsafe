<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'" style="border: 1px solid #d4d4d4;">
		<ul class="f-cbli">
			<li>
				<button class="btn btn-success btn-sm J_add"  type="button">新增模板</button>
			</li>	
		</ul>
	</div>
	<div data-options="region:'center',border:false" style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" title="模板列表" 
			data-options="{
			url:'logTpl/list',
			method:'post',
			toolbar: [{
				iconCls: 'icons icons-plus-sign',
					handler: function() {
						PanelContent.find('.J_add').click();
					}
				},'-',{
					iconCls:'icons icons-refresh',
					handler: function(){
						reloadGrid();
					}
			}],
			columns: [[
				{field:'name',title:'模板名称',width: 200},
				{field:'counts',title:'触点项数量',width:120},
				{field:'remark',title:'备注',width:120},
				{field:'addTime',title:'添加时间',width:200},
				{field:'id',title:'操作',width:120,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
			}">
		</table>
	</div>	
</div>
<script>
	PanelContent.find('.J_add').on("click", function() {
		location.href = '#logTpl/add';
	});
	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			var typeName = data.type == "选择"?1:0;
			if (type == "edit") {
				openW("${__root__}/logTpl/edit?templateId="+id, {
					title: "编辑模板",
					width: 1200,
					height: 500,
					formId: "#add_tpl",
					
				}, function() {
					// 设置元素编辑请求地址
					$("#update_tpl").attr('action',"${__root__}/logTpl/edit");
					$("#update_tpl_name").val(data.name);
					$(".tplForm-action").append("<input type='hidden' name='id' value='"+id+"' />");
					$("#update_tpl").form("load", {
						remark: data.remark,
						name: data.name,
					});
				})
			} else if (type == "del") {
				updateItem("${__root__}/logTpl/del",{
					param: {id:id}
				});
			}
		});
		$(".J_addAttr").on("click", function() {
			var id = $(this).attr("data-id");
			var name = $(this).attr("data-name");
			openW("${__root__}/contact/attr?id="+id, {
				title: '【'+name + "】属性明细",
				width: 1000,
				height: 400
			});

		})
	}
	function handleGrid(v,r,i) {
		var html = '<a href="javascript:;" class="icons icons-edit J_pop" title="编辑" data-type="edit" data-index='+i+'></a>';
			html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-minus-sign J_pop" title="删除" data-type="del" data-index='+i+'></a>';
		return html;
	}

	// 筛选
	function changeCate(id) {
		GridElement.datagrid({
			url:"url"+id
		})
	}
	
</script>