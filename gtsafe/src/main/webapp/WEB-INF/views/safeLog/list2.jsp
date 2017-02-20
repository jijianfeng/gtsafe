<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="jq-layout rel" data-options="fit:true">
	<div data-options="region:'center',border:false"
		style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" title="日志历史列表"
			data-options="
			url:'safeLog/list2',
			method:'post',
			toolbar: [{
					iconCls:'icons icons-refresh',
					handler: function(){
						reloadGrid();
					}
			}],
			columns: [[
				{field:'addTime',title:'填报时间',width:200},
				{field:'templateName',title:'所用模板',width: 200},
				{field:'score',title:'直接得分',width: 100},
				{field:'score2',title:'当时基准分',width:100},
				{field:'score3',title:'当时波动值',width:100},
				{field:'summary',title:'分值汇总',width:100},
				{field:'day',title:'施工天数',width:100},
				{field:'id',title:'操作',width:200,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
			">
		</table>
	</div>
	
</div>
<script>
	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			var addTime = data.addTime;
			if (type == "detail") {
				openW("safeLog/data2?id="+id+"&addTime="+addTime, {
		            title: "当日内容详情",
		            width: 1200,
		            height: 500
		            
		        });
			} else if (type == "share") {
				$.confirm("确认导出", function(r) {
		            $.post("safeLog/exportLog",{
		            	id:id
		            }, function(data) {
		                if (data.status == 1) {
		                	$.alert(data.info+"【<a style='color:red' target='_blank' href='"+ data.data +"'>点击下载表格</a>】");
		                } else {
		                    $.alert(data.info);
		                }
		            },"json");
		        });
			}else if (type == "preview") {
				openW("safeLog/preview2?logId="+id, {
					formAction : "safeLog/preview2",
					title : "内容预览",
					width : 1200,
					height : 570
				}, function() {
					// 信息滚动
					$('#preview_reason').vTicker({
						speed : 1500,
						pause : 20000,
						showItems : 1,
						animation : 'fade',
						mousePause : false,
						height : 0,
						direction : 'up'
					});
					$('#preview_title2').vTicker({
				      speed: 1500,
				      pause: 20000,
				      showItems: 1,
				      animation: 'fade',
				      mousePause: false,
				      height: 0,
				      direction: 'up'
				    });
				});
			}
		});
		$(".J_lookReject").on("click", function() {
			var id = $(this).attr("data-id");
			var addTime = $(this).attr("data-addTime");
			openW("safeLog/reason?id="+id+"&addTime="+addTime, {
				title: "驳回详情",
				width: 600,
				height: 200
			});
		});
	}
	
	function rejectFmt(v, r) {
		var html;
		if (v == "已被驳回") {
			var t = "(详情)";
			html = '<a class="txt-info J_lookReject" href="javascript:;" data-id='+r.id+' data-addTime='+r.addTime+'>'+t+'</a>';
			return v + html;
		}
		return v;
	}
	function handleGrid(v,r,i) {
		var html;
		html = '<a href="javascript:;" class="icons icons-info-sign J_pop" title="查看详情" data-type="detail" data-index='+i+'></a>';
		/* html+= '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icon-search J_pop" title="预览" data-type="preview" data-index='+i+'></a>';
		html+= '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-file J_pop" title="导出日志" data-type="share" data-index='+i+'></a>'; */
		return html;
	}
	
	// 筛选
	function changeState(state) {
		state = state-1;
		GridElement.datagrid({
			url:"safeLog/list?state="+state
		})
	}
	
	
</script>