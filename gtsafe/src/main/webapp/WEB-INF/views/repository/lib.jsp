<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'" style="border: 1px solid #d4d4d4;">
		<ul class="f-cbli">
			<li>
				<button class="btn btn-success btn-sm J_add"  type="button">新增文档</button>
			</li>	
			<li class="fr m0">
				<span class="f12">分类筛选：</span>
				<input class="jq-combotree" id="lib2_id"  type="text" name="docsCateId" data-options="{
                      required:true,
                      url:'repository/cateList?level=2&top=1',
                      onChange: change
                  }">
			</li>	
		</ul>
	</div>
	<div data-options="region:'center',border:false" style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" title="文档列表" 
			data-options="
			url:'repository/lib',
			remoteSort: 'false',
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
				{field:'number',title:'文档编号',width: 200,sortable:true},
				{field:'docsName',title:'文档主题',width: 200,sortable:true},
				{field:'docsCateName',title:'所属分类',width: 200},
				{field:'userName',title:'上传人',width: 200},
				{field:'buildTime',title:'施行日期',width: 200,sortable:true},
				{field:'releaseUnit',title:'发布单位',width: 200,sortable:true},
				{field:'remarks',title:'备注',width: 200},
				{field:'addTime',title:'上传时间',width: 200,sortable:true},
				{field:'id',title:'操作',width:150,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
			">
		</table>
	</div>	
</div>
<script>
	PanelContent.find('.J_add').on("click", function() {
		openW("repository/saveLib", {
			title: "新增文档",
		});
	});
	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			if (type == 'edit') {
				openW("repository/editLib", {
					formAction:"repository/editLib",
					title: "编辑文档",
					gridType: 1,
					id: id
				}, function() {
					// 如果是父节点则移除上级分类选项
					$("#add").form('load', {
						number: data.number,
						docsName: data.docsName,
						docsCateId: data.docsCateId,
						releaseUnit: data.releaseUnit,
						remarks: data.remarks,
						build:data.buildTime
					});
				})
			} else if (type == "del") {
				updateItem("repository/delLib",{
					param: {id:id}
				});
			}
		});
	}
	function handleGrid(v,r,i) {
		var html = '<a href="'+r.filePath+'" class="icons icons-download-alt J_pop" title="下载" id="a" data-type="download" data-index='+i+' target="_balnk"></a>';
			html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-edit J_pop" title="编辑" data-type="edit" data-index='+i+'></a>';
			html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-minus-sign J_pop" title="删除" data-type="del" data-index='+i+'></a>';
		return html;
	}
	function change(id) {
		GridElement.datagrid({
			url:"repository/lib?cateId="+id
		})
	}
</script>