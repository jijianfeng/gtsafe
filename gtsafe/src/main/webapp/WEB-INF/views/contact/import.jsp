<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<!-- 导入excel弹出层 -->

<div id="dlg_excel" style="padding:10px;">
	<form method="post" enctype="multipart/form-data" id="input_excel">
		<table>
			<tbody>
				<tr>
					<td style="padding-bottom: 10px;">选择Excel记录文件：&nbsp;</td>
					<td style="padding-bottom: 10px;">
						<input id="fileinput" class="jq-validatebox" data-options="required:true" type="file" name="excel" />	
					</td>
				</tr>
				<tr>
					<td style="padding-bottom: 10px;"><input type="hidden" name="type" id="type" value="0" /></td>
					<td align="right" style="padding-bottom: 10px;"><button  class="btn btn-primary" id="input_excel_btn" type="submit">导入记录</button></td>
				</tr>
			</tbody>
		</table>
	</form>
	<p style="font-size:12px;" class="blacklist-input-tips">
		上传格式说明：
		<br>	
		1.Excel上传的文件有严格的格式限制，<font color="red">请先下载Excel样本填入相应的数据后再上传</font>
		[
		<a id="wg_tags" style="color:blue"> <i class="icons icons-file">&nbsp;</i>
			点击下载Excel样本
		</a>
		]
		<br>	
		2.Excel导入只支持一次导入一张表，若Excel文件中包含多张表，则请拆分成不同的Excel文件后上传；
		<br>
		3.Excel表，中的单元格格式要都变成文本格式,不然又可能出现异常报错；	
		<br>
		4.Excel文件除了“文件标题栏”和“字段标题”之外，其他的不是数据库数据条目的注释需全部删除。
		<br></p>
</div>
<!-- /导入excel弹出层 -->
<script>
	function excelInt(obj) {
		var action = obj.action;
		var href = obj.href;
		$("#input_excel").attr("action", action);
		$("#wg_tags").attr("href",href);
		$("#type").val(obj.type);
		$("#input_excel").form({
			onSubmit: function() {
				var isValid = $(this).form('validate');
				if (isValid){
					$("#input_excel_btn").before('<em class="text-error" id="importMsg">导入中...&nbsp;&nbsp;&nbsp;&nbsp;</em>');
				} else {
					return false;
				}
	        },
			success: function(data){
				var data = new (Function('return '+ data))();
				if(typeof(data) !== 'object'){ //返回值不是json
					$.alert("出现异常错误：请下载并使用Excel样本填入数据，保持Excel格式不变，不要进行兼容性转换！请使用微软的OFFICE，不要使用WPS软件录入数据。");
				}else{ //返回正确的json格式
					if(data.status==1){
						$.alert(data.info);
					} else {
						$.alert(data.info);
					}
				}
				$("#importMsg").remove();
			}
		});
	}
</script>