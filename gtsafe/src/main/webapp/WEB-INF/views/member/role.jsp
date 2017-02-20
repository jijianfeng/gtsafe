<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'" style="border: 1px solid #d4d4d4;">
		<ul class="f-cbli">
			<li>
				<button class="btn btn-success btn-sm J_add" type="button">新增角色</button>
			</li>
			 <li>
				 <!-- <button class="btn btn-success btn-sm" id="add_btn_node" type="button">新增权限节点</button>  -->
			</li>
			</ul>
	</div>
	<div data-options="region:'center',border:false" style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" title="角色列表" 
			data-options="
			url:'member/role',
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
				{field:'name',title:'角色名称',width: 200},
				{field:'remark',title:'备注',width: 200},
				{field:'status',title:'状态',width:80, formatter: statusFmt},
				{field:'addTime',title:'添加时间',width:140},
				{field:'id',title:'操作',width:80,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
			">
		</table>
	</div>	
</div>
<script>
	PanelContent.find(".J_add").on("click", function() {
		openW("member/saveRole", {
			formAction:"member/saveRole",
			title: "新增角色"
		});
	});
	PanelContent.find("#add_btn_node").on("click", function() {
		openW("member/saveNode", {
			formAction:"member/saveNode",
			title: "新增权限节点"
		});
	});
	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			if (type == "edit") {
				openW("member/editRole", {
					formAction:"member/editRole",
					title: "编辑角色",
					id: id
				}, function() {
					$("#add").form("load", {
						name: data.name,
						remark: data.remark,
						status: data.status
					});
				})
			} else if (type == "del") {
				updateItem("member/delRole",{
					param: {id:id}
				});
			} else if (type == "accredit") {
				openW("member/accredit?id="+id,{
					formAction: "member/accredit",
					title: "角色授权",
					id: id,
					width: 600,
					height:500,
					formId: "#accredit"
				})
			}
		});
	}
	function handleGrid(v,r,i) {
		var html = '<a href="javascript:;" class="icons icons-edit J_pop" title="编辑" data-type="edit" data-index='+i+'></a>';
		html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-lock J_pop" title="用户授权" data-type="accredit" data-index='+i+'></a>';
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