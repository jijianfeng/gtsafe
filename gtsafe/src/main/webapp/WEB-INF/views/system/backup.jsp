<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'" style="border: 1px solid #d4d4d4;">
		<ul class="f-cbli">
			<li>
				<button class="btn btn-success btn-sm J_add"  type="button">备份数据</button>
			</li>	
		</ul>
	</div>
	<div data-options="region:'center',border:false" style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" title="数据库备份" 
		data-options="
		url:'system/backup',
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
		{field:'name',title:'备份名称',width: 200},
		{field:'number',title:'卷数',width: 200},
		{field:'format',title:'压缩格式',width: 200},
		{field:'size',title:'数据大小',width: 200},
		{field:'addTime',title:'备份时间',width: 200},
		{field:'id',title:'操作',width:150,formatter: handleGrid}
		]],
		onLoadSuccess:loadGrid
		">
</table>
</div>	
</div>
<script>
	PanelContent.find('.J_add').on("click", function() {
		$(this).attr('disabled','disabled').text('数据备份中...');
		$.post("system/saveBackup", function(data) {
			if (data.status == 1) {
				$.alert(data.info);
				reloadGrid();

			}else{
				$.alert(data.info);
			}
			PanelContent.find('.J_add').removeAttr('disabled').text('备份数据');
		},"json");
	});

	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			if (type == "del") {
				updateItem("system/delBackup",{
					param: {id:id}
				});
			} else if (type == "restore") {
				updateItem("url",{
					title: "确定要还原吗？",
					param: {id:id}
				});
			}
		});
	}
	function handleGrid(v,r,i) {
		var html = '<a href="'+r.filePath+'" class="icons icons-download-alt J_pop" title="下载" id="a" data-type="download" data-index='+i+'></a>';
		html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-minus-sign J_pop" title="删除" data-type="del" data-index='+i+'></a>';
		/* html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-time J_pop" title="还原" data-type="restore" data-index='+i+'></a>'; */
		return html;
	}
</script>