<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<!-- top -->
<form id="add_safeLog" method='post'>
	<div class="p10 bdc mb10">
		<ul class='fix'>
			<li class='fl'>
				<c:if test="${save!=null }">
					<div class="alert alert-error">${save}</div>
				</c:if>
				<c:if test="${fill!=null }">
					<div class="alert alert-error">	${fill }</div>
				</c:if>
				<span class="f12">选择时间：</span>
				<%--  <select  name="time" class="jq-combobox" data-options="{
				 	
				 	}">
					<c:forEach items="${time}" var="time">
						<option value="${time.id}">${time.addTime}</option>
					</c:forEach>
				</select> 
				 --%>
				<input id="get_select_time" name="time" type="text" class="jq-combobox" 
						data-options="
					        valueField: 'id',
					        textField: 'addTime',
					        editable:false,
							url:'safeLog/selectTime',
							method:'post',
						 	onSelect:changeTime
				" />
			</li>
			<li class="fr m0">
				<button class="btn btn-default btn-small" id="export_word"
					type='button'>
					<i class="icons icons-file"></i>导出本次日志
				</button>
			</li>
		</ul>
	</div>

	<!-- top -->

	<!-- date -->
	<div class="p10 bdc mb10">
		<ul>
			<li><span class="f12">今天是[${serviceTime}]</span>
			</li>
		</ul>
	</div>
	<!-- date -->

	<!-- 主体 -->
	<div class="p10 bdc mb10" style="min-height: 500px;">
		<div id="load_content">正在加载模板...</div>
		<div class="tc">
			<!-- 安全员负责 -->
			<span>
				<button type="button" class="btn btn-info" id="load_log">加载昨日内容</button>&nbsp;&nbsp;
				<button type="button" class="btn btn-success" id="submit_log">提交</button>&nbsp;&nbsp;
				<button type="button" class="btn btn-warning" id="clear_log">清空</button>&nbsp;&nbsp;
				<button type="button" class="btn btn-success" id="save_log">保存</button>&nbsp;&nbsp;
			</span>
			<button type="button" class="btn btn-info" id="preview_log">预览</button>&nbsp;&nbsp;
			<button type="button" class="btn btn-info" id="look_reject" style="display:none">查看驳回消息</button>
		</div>
		<div class="tc f14" id="prompt_message"  style="display:none">本次提交只会更新驳回的日志</div>
	</div>
</form>
<!-- 主体 -->
<script>
	/*  表单提交 */
	$("#add_safeLog").form({
		success : function(data) {
			var data = new Function("return" + data)();
			if (data.status == 1) {
				if(data.data!=null){
					$.alert(data.info+"【<a style='color:red' target='_blank' href='"+ data.data +"'>点击下载表格</a>】");
				}else{
					$.alert(data.info);
					$("#get_select_time").combobox({url:'safeLog/selectTime'}); 
					$("#load_content").text("正在重置模板...");
					$("#load_content").load("safeLog/log?type=default"+"&_="+Math.random());
					$("#prompt_message").hide();
					$("#look_reject").hide();
				}
			}else{
				$.alert(data.info);
			}
		}
	});
	// 加载模板	
	$(function loadLog() {
		$("#load_content").load("safeLog/log?type=default&_="+Math.random());
	})
	
	// 日志提交
	$("#submit_log").on("click", function() {
		var msg = [];
		var getNodeArray = function(self) {
			var val = self.find("input[type=radio]:checked").val();
			var textarea = self.find("textarea");
			if (textarea.length > 0) {
				if (textarea.val() == "") {
					var number = self.find("em").text().toString();
					msg.push(number);	
				}
				return false;
			}
			if (typeof val === "undefined") {
				var number = self.find("em").text().toString();
				msg.push(number);
			}
		};
		$(".J_parent").each(function() {
			var node = $(this).find(".J_node li");
			if (node.length > 0) {
				node.each(function() {
					getNodeArray($(this));
				});
			} else {
				$(this).find("li").each(function() {
					getNodeArray($(this));
				});
			}
		});
		if (msg.length > 0) {
			$.alert("以下触点还没有完成<div style='word-wrap: break-word;word-break:break-word;'>"+msg+"</div>");
			return false;
		}
		// 设置表单的提交地址
		$("#add_safeLog").attr("action", "safeLog/add");
		$.confirm("确定要提交吗？", function(r) {
			$("#submit_log").attr('disabled', 'disabled').text('保存中');
			submitFormLog(function() {
				$("#submit_log").removeAttr('disabled').text('提交');
			});
		});
	});
	
	// 保存当前内容
	$("#save_log").on("click", function() {
		// 设置表单的提交地址
		$("#add_safeLog").attr("action", "safeLog/saveNow");
		$("#save_log").attr('disabled', 'disabled').text('正在保存');
		submitFormLog(function() {
			$("#save_log").removeAttr('disabled').text('保存');
		});
	});
	// 表单提交事件
	function submitFormLog(callBack) {
		$("#add_safeLog").submit();
		setTimeout(function() {
			callBack();
		}, 1000);
	}
	$("#look_reject").on("click", function() {
		var id = $(this).data("id");
		openW("safeLog/reason?id="+id, {
			title: "驳回详情",
			width: 600,
			height: 200
		});
	});
	
	function changeTime(v) {
		var arr = v.id.split("-");  
		if(arr[0].indexOf('logId')!=-1){
			var logId = arr[1];
			$("#look_reject").data("id", logId);
			$("#look_reject").show();
			$("#prompt_message").show();
			$("#load_content").text("正在加载驳回的模板...");
			$("#load_content").load("safeLog/log?type=update&logId="+logId+"&_="+Math.random());
		 }
	}
	// 加载昨日内容
	$("#load_log").on("click",function() {
		$.confirm("确定要加载昨日内容吗？", function(r) {
			$.get("safeLog/yesterday", function(data) {
				if (data.status == 1) {
					$("#load_content").text("正在加载昨日模板...");
					$("#load_content").load("safeLog/log?type=history&logId="+ data.data.id+"&_="+Math.random());
				} else {
					$.alert(data.info);
				}
			}, "json");
		});
	});
	//  清空所有内容
	$("#clear_log").on("click", function() {
		$.confirm("确定要清空所有内容吗？", function(r) {
			$("#load_content").text("正在清空模板...");
			$("#load_content").load("safeLog/log?type=change"+"&_="+Math.random());
		});
	});
	
	// 导出
	$("#export_word").on("click", function() {
		$.confirm("确人要将当前日志导出吗？", function(r) {
			$("#add_safeLog").attr("action", "safeLog/export");
			$("#add_safeLog").submit();
		});
	});
	// 预览
	$("#preview_log").on("click",function() {
		// 获取触点项名称和应急措施
		var touchDatas = [];
		$("input:checked").each(function(i, e) {
			var $o = $(e).siblings(".J_info");
			var title = decodeURIComponent($o.data("title"));
			var name = decodeURIComponent($o.data("name"));
			var step = decodeURIComponent($o.data("step"));
			var score = $o.data("score");
			var important = $o.data("important");
			touchDatas.push({
				title : title,
				name : name,
				step : step,
				score : score,
				important: important
			});
		});
		openW("safeLog/preview", {
			formAction : "safeLog/preview",
			title : "当日内容预览",
			width : 1200,
			height : 570
		}, function() {
			var html = '';
			var point = "";
			touchDatas.sort(function(a,b){
			  return b.important-a.important;
			});
			for ( var i = 0; i < touchDatas.length; i++) {
				if (i < 5) {
					if(touchDatas[i].score!=0){
						html += '<div class="info">';
						html += i+1+'.'+touchDatas[i].title;
						html += '：';
						html += touchDatas[i].name;
						html += '。<em class="fuchsia">应对提示：';
						if(touchDatas[i].step){
							html += touchDatas[i].step;
						}else{
							html += '无';
						}
						html += '</em></div>';
					}
				}
				point = parseInt(touchDatas[i].score + point);
			}
			point =Math.round(point/10) + parseInt('${scoreAll}');
			$("#load_content").data("todayPoint",point);
			$("#preview_reason .cause").html(html);
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
			$('#preview_title').vTicker({
		      speed: 1500,
		      pause: 20000,
		      showItems: 1,
		      animation: 'fade',
		      mousePause: false,
		      height: 0,
		      direction: 'up'
		    });
		});
	});
	
</script>