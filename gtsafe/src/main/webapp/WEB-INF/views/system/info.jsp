<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<form id="system_info_form" method='post'
	action="system/saveInfo">
	<fieldset>
		<table class="form-table">
			<tr>
				<td>日志模板：</td>
				<td><select id="template" name="templateId" class="jq-combobox"
					data-options="required:true">
						<c:forEach items="${tempName}" var="temp" varStatus="number_id">
							<option value="${temp.id}">${temp.name }</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td>基准分：</td>
				<td><input name="benchmark" type="text" class="jq-numberbox"
					data-options="required:true" value="${config.benchmark}" /></td>
			</tr>
			<tr>
				<td>波动值：</td>
				<td><input name="undulate" type="text" class="jq-numberbox"
					data-options="required:true" value="${config.undulate}" /></td>
			</tr>
			<tr>
				<td>生产日期：</td>
				<td><input name="begin_time" type="text" class="jq-datebox"
					data-options="required:true" style="width:110px"
					value="${config.begin_time}" /> <em>到</em> <input name="end_time"
					type="text" class="jq-datebox" data-options="required:true"
					style="width:110px" value="${config.end_time}" /></td>
			</tr>
			<tr>
				<td>方针和目标：</td>
				<td style="width: 500px;"><textarea name="policy"
						style="width: 500px;height: 200px;">${config.policy}</textarea> <span>方针和目标内容可以为一张图片，最佳尺寸为690px*128px</span>
				</td>
			</tr>
			<tr>
				<td>安全生产管理体系：</td>
				<td style="width: 500px;"><textarea class="J_editer"
						name="message" style="width: 500px;height: 200px;">${config.message}</textarea>
				</td>
			</tr>
			<tr>
				<td>前端图表显示天数：</td>
				<td><input name="chart_day" type="text" class="jq-numberbox"
					data-options="required:true" value="${config.chart_day}" /> <span
					class="muted">默认为0显示全部天数</span></td>
			</tr>
			<tr>
				<td>近期不安全因素显示天数：</td>
				<td><input name="show_day" type="text" class="jq-numberbox"
					data-options="required:true" value="${config.show_day}" /></td>
			</tr>
			<tr>
				<td>近期不安全因素出现最少次数：</td>
				<td><input name="appear_number" type="text" class="jq-numberbox"
					data-options="required:true" value="${config.appear_number}" /></td>
			</tr>
			<tr class="tl">
				<td></td>
				<td class="form-actions">
					<button type="submit" class="btn btn-success" id="info_btn">保存</button>
				</td>
			</tr>
		</table>
	</fieldset>
</form>
<script>
	// 表单提交
	$("#system_info_form").form({
		onSubmit: function() {
			var isValid = $(this).form('validate');
			if (isValid){
				$(this).find("button[type='submit']").attr('disabled','disabled').text('保存中');
			} else {
				return false;
			}
		},
		success:function(data){
			var data = new Function("return" + data)();
			$.alert(data.info);
			$("#info_btn").removeAttr('disabled').text('保存');
		}
	});
	$("#template").val('${config.templateId}');
	
	/* 初始化编辑器 */
	 KindEditor.create("[name='policy']",{
		items : [
				'source','|','fontsize','|', 'forecolor',  'bold', 'italic', 'underline','lineheight',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright','|', 'image'],
			// 文件上传
         uploadJson:"system/editorUpload",
         afterChange : function() {
			    this.sync();
			},
			afterBlur:function(){
			    this.sync();
			}
     });
	 KindEditor.create(".J_editer",{
		items : [
				'source','|','fontsize','|', 'forecolor',  'bold', 'italic', 'underline','lineheight',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright'],
			// 文件上传
      	uploadJson:"system/editorUpload",
      	afterChange : function() {
			    this.sync();
			},
			afterBlur:function(){
			    this.sync();
			}
  	});
</script>