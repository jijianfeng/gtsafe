<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'" style="border: 1px solid #d4d4d4;">
	<ul class="f-cbli">
		<li>
			<button class="btn btn-success btn-sm J_add"  type="button">新增触点分类</button>
		</li>
	</ul>
	</div>
	<div data-options="region:'center',border:false" style="padding-top:8px;">
		<table fit="true" title="触点分类列表" class="jq-treegrid J_grid" data-options="{
			url:'contact/cate',
			method: 'post',
			treeField:'name',
			pagination: false,
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
				{field:'name',title:'分类名称',width:640},
				{field:'number',title:'编号',width: 60},
				{field:'counts',title:'触点项数量统计',width:120, formatter: dataFmt},
				{field:'isShow',title:'是否显示',width:80, formatter: isBool},
				{field:'id',title:'操作',width:120,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
		}">
		</table>
	</div>
</div>

<script>
PanelContent.find('.J_add').on("click", function() {
	openW("contact/saveCate", {
		formAction:"contact/saveCate",
		title: "新增触点分类",
		gridType: 1
	});
});
function loadGrid() {
	popHandle().on('click',function() {
		var data = new Function("return" + decodeURIComponent($(this).data("role")))();
		var type = $(this).attr('data-type');
		var id = data.id;
		if (type == 'edit') {
			openW("contact/editCate", {
				formAction:"contact/editCate",
				title: "编辑触点分类",
				gridType: 1,
				id: id
			}, function() {
				// 如果是父节点则移除上级分类选项
				if (data.pid1==0) {
					$("#parent_module").remove();
				}
				$("#add").form('load', {
					pid: data.pid,
					name: data.name,
					isShow: data.isShow,
					orderId: data.orderId,
					upNumber:data.upNumber,
					counts: data.counts,
					oldPidId: data.pid
				});
			})
		} else if (type == 'del') {
			updateItem("contact/delCate",{
				param: {id:id},
				gridType: 1
			});
		}
	})
}
function handleGrid(val,row) {
	var data =  encodeURIComponent(JSON.stringify(row));// 处理空格等特殊字符
	var delBtn = '<a href="javascript:;" class="icons icons-minus-sign J_pop" title="删除" data-type="del" data-role='+data+'></a>';
	if (row.pid1==0) {
		delBtn = '';
	}
	return '<a href="javascript:;" class="icons icons-edit J_pop" title="编辑" data-type="edit" data-role='+data+'></a>&nbsp;&nbsp;&nbsp;&nbsp;'+delBtn;
}
</script>