<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'"
		style="border: 1px solid #d4d4d4;">
		<ul class="f-cbli">
			<li>
				<button class="btn btn-success btn-sm J_add" type="button">新增用户</button></li>
		</ul>
	</div>
	<div data-options="region:'center',border:false"
		style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" title="用户列表"
			data-options="
				url:'member/user',
				method: 'post',
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
					{field:'number',title:'人员编号',width: 50},
					{field:'account',title:'登录账号',width: 70},
					{field:'name',title:'真实姓名',width: 70},
					{field:'role',title:'所属角色',width: 70},
					{field:'phone',title:'手机号',width: 60},
					{field:'fieldName',title:'所属工地或项目',width: 100},
					{field:'remark',title:'备注',width: 100},
					{field:'status',title:'账号状态',width:40, formatter: statusFmt},
					{field:'id',title:'操作',width:80,formatter: handleGrid}
					]],
				onLoadSuccess:loadGrid
				">
		</table>
	</div>
</div>
<script>
	PanelContent.find('.J_add').on("click", function() {
		openW("member/saveUser", {
			title : "新增用户",
			formAction : "member/saveUser"
		});
	});
	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			if (type == "edit") {
				openW("member/editUser", {
					title : "编辑用户",
					formAction : "member/editUser",
					id : id
				}, function() {
					$("#add").form("load", {
						number : data.number,
						account : data.account,
						name : data.name,
						role : data.roleId,
						phone : data.phone,
						remark : data.remark,
						status : data.status,
						fieldId: data.fieldId
					});
				})
			} else if (type == "del") {
				updateItem("member/delUser",{
					param: {id:id}
				});
			} 
		});
	}
	function handleGrid(v, r, i) {
		var html = '<a href="javascript:;" class="icons icons-edit J_pop" title="编辑" data-type="edit" data-index='+i+'></a>';
		html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-minus-sign J_pop" title="删除" data-type="del" data-index='+i+'></a>';

		return html;
	}
	function statusFmt(v) {
		if (v == 0) {
			v = '禁用';
		} else {
			v = '启用';
		}
		return v;
	}
</script>