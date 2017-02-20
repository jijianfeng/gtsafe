<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'" style="border: 1px solid #d4d4d4;">
	<ul class="f-cbli">
		<li>
			<button class="btn btn-success btn-sm J_add"  type="button">新增分类</button>
		</li>
	</ul>
	</div>
	<div data-options="region:'center',border:false" style="padding-top:8px;">
		<table fit="true" title="文档管理" class="jq-treegrid J_grid" data-options="{
			url:'repository/cate',
			method: 'post',
			treeField:'cateName',
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
				{field:'cateName',title:'分类名称',width:550},
				{field:'count',title:'文档数量统计',width:100, formatter: dataFmt},
				{field:'id',title:'操作',width:400,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
		}">
		</table>
	</div>
</div>

<script>
PanelContent.find('.J_add').on("click", function() {
	openW("repository/saveCate", {
		title: "新增分类",
		gridType: 1
	});
});
function loadGrid() {
	popHandle().on('click',function() {
		var data = new Function("return" + decodeURIComponent($(this).data("role")))();
		var type = $(this).attr('data-type');
		var id = data.id;
		if (type == 'edit') {
			openW("repository/editCate", {
				formAction:"repository/editCate",
				title: "编辑分类",
				gridType: 1,
				id: id
			}, function() {
				// 如果是父节点则移除上级分类选项
				if (data.pid==0) {
					$("#parent_module").remove();
				}
				$("#add").form('load', {
					pid: data.pid,
					name: data.cateName,
					isShow: data.isShow,
					orderId: data.orderId
				});
			})
		} else if (type == 'del') {
			updateItem("repository/delCate",{
				param: {id:id},
				gridType: 1
			});
		}
	})
}
function handleGrid(val,row) {
	var data =  encodeURIComponent(JSON.stringify(row));// 处理空格等特殊字符
	var delBtn = '<a href="javascript:;" class="icons icons-minus-sign J_pop" title="删除" data-type="del" data-role='+data+'></a>';
	/* if (row.pid==0) {
		delBtn = '';
	} */
	return '<a href="javascript:;" class="icons icons-edit J_pop" title="编辑" data-type="edit" data-role='+data+'></a>&nbsp;&nbsp;&nbsp;&nbsp;'+delBtn;
}
</script>