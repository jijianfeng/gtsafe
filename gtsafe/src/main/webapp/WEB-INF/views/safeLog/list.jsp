<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'"
		style="border: 1px solid #d4d4d4;">
		<ul class="f-cbli">
			<li>
				<button class="btn btn-success btn-sm J_add"  type="button">新增日志</button></li>
			<li class="fr m0"><span class="f12">日志状态筛选：</span> <select
				name="state" class="jq-combobox" data-options="onChange:changeState">
					<option value="0">全部</option>
					<option value="1">等待审核</option>
					<option value="3">审核通过</option>
			</select></li>
		</ul>
	</div>
	<div data-options="region:'center',border:false"
		style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" title="日志列表"
			data-options="
			url:'safeLog/list',
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
				{field:'addTime',title:'填报时间',width:200},
				{field:'template',title:'所用模板',width: 200},
				{field:'score',title:'直接得分',width: 100},
				{field:'score2',title:'当时基准分',width:100},
				{field:'score3',title:'当时波动值',width:100},
				{field:'summary',title:'分值汇总',width:100},
				{field:'day',title:'施工天数',width:100},
				{field:'statusName',title:'日志状态',width:120},
				{field:'id',title:'操作',width:200,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
			">
		</table>
	</div>


	
</div>
<script>
	PanelContent.find('.J_add').on("click", function() {
		location.href = '#safeLog/add';
	});
	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			var addTime = data.addTime;
			var typeName = data.type == "选择"?1:0;
			var templateId = data.templateId;
			if (type == "detail") {
				openW("safeLog/data?id="+id+"&templateId="+templateId+"&addTime="+addTime, {
		            title: "当日内容详情",
		            width: 1200,
		            height: 500
		            
		        });
			}else if (type == "edit") {
				openW("safeLog/edit?updateId="+id+"&templateId="+templateId, {
		            title: "重新提交",
		            width: 1200,
		            height: 500
		        }, function() {
		        	$(".panel-tool-close").on("click", function() {
		        		reloadGrid();
		        	});
		        });
			}else if (type == "del") {
				updateItem("url",{
					param: {id:id}
				});
			} else if (type == "right") {
				$.confirm("确认审核通过", function(r) {
		            $("#sure_submit_log").attr('disabled','disabled').text('正在提交');
		            $.post("safeLog/right?id="+id, function(data) {
		                if (data.status == 1) {
		                    $("#sure_submit_log").removeAttr('disabled');
		                    $.alert(data.info);
		                    reloadGrid();
		                } else {
		                    $.alert(data.info);
		                }
		            },"json");
		        });
			} else if (type == "reject") {
				$.prompt("请输入驳回理由", function(r) {
		            // r为返回的输入值
		           // r=encodeURI(encodeURI(r));  
		            if (r) {
		                $.post("safeLog/reject",{
		                	id : id,
		                	reason : r
		                }, function(data) {
		                    if (data.status == 1) {
		                        $.alert(data.info);
		                        reloadGrid();
		                        $("#get_select_time").combobox({url:'safeLog/selectTime'});
		                    } else {
		                        $.alert(data.info)
		                    }
		                },"json");
		            } else {
		                $.alert("请输入驳回理由");
		            }
		        });
				
			}else if (type == "share") {
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
					title : "当日内容预览",
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
		html+= '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icon-search J_pop" title="预览" data-type="preview" data-index='+i+'></a>';
		if (r.status == 0) {
			html = html +'&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-ok J_pop" title="审核通过" data-type="right" data-index='+i+'></a>'
			+ '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-remove J_pop" title="驳回" data-type="reject" data-index='+i+'></a>';
		}if(r.status == 1){
			// html = html +'&nbsp;&nbsp;&nbsp;&nbsp<a href="javascript:;" class="icons icons-check J_pop" title="重新提交" data-type="edit" data-index='+i+'></a>';
		}if(r.status == 2){
			html+= '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-file J_pop" title="导出日志" data-type="share" data-index='+i+'></a>';
		}
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