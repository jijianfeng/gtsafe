<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<!-- top -->
<form id="update_safeLog" method='post'>
	<div class="p10 bdc mb10">
		<ul class='fix'>
			<input type ="hidden" name ="logId" value="${updateId}">
			<li class="fr m0">
				<button class="btn btn-default btn-small" id="update_export_word"
					type='button'>
					<i class="icons icons-file"></i>导出本次日志
				</button>
			</li>
		</ul>
	</div>

	<!-- top -->


	<!-- 主体 -->

	<div class="p10 bdc mb10" style="min-height: 500px;">
		<div id="update_load_content">正在加载模板...</div>
		<div class="tc">
				<button type="button" class="btn btn-success" id="update_submit_log">重新提交</button>&nbsp;&nbsp;
				<button class="btn btn-default J_close" type="button">取消</button>
		</div>
	</div>
</form>
<!-- 主体 -->
<script>
	/*  表单提交 */
	$("#update_safeLog").form({
		success : function(data) {
			var data = new Function("return" + data)();
			if(data.data!=null){
				$.alert(data.info+"【<a style='color:red' target='_blank' href='"+ data.data +"'>点击下载表格</a>】");
			}else{
				$.alert(data.info);
			}
		}
	});
	
	// 默认加载第一个模板	
	$(function updateLoadLog() {
		$("#update_load_content").load("safeLog/log?id=" + '${updateId}' + "&type=update");
	})
	
	//重新提交
	$("#update_submit_log").on("click", function() {
		// 设置表单的提交地址
		$("#update_safeLog").attr("action", "safeLog/update");
		$.confirm("确定要重新提交吗？", function(r) {
			$("#update_submit_log").attr('disabled', 'disabled').text('重新提交中');
			submitFormLog(function() {
				$("#update_submit_log").removeAttr('disabled').text('重新提交');
			});
		});
	});
	// 表单提交事件
	function submitFormLog(callBack) {
		$("#update_safeLog").submit();
		setTimeout(function() {
			callBack();
		}, 1000);
	}
	// 导出
	$("#update_export_word").on("click", function() {
		$.confirm("确人要将当前日志导出吗？", function(r) {
			$("#update_safeLog").attr("action", "safeLog/export");
			$("#update_safeLog").submit();
		});
	});
</script>