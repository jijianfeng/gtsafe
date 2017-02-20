<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="edit_password_form" method='post' action="member/editPassword">
	<fieldset>
		<table align="center" class="form-table">
		<tr>
			<td>原密码：</td>
			<td>
				<input id="" name="oldPassword" type="password" class="jq-validatebox" data-options="required:true" />
			</td>
		</tr>
		<tr>
			<td>新密码：</td>
			<td>
				<input id="" name="newPassword" type="password" class="jq-validatebox" data-options="required:true" />
			</td>
		</tr>
		
		<tr class="tl">
			<td></td>
			<td class="form-actions">
				<button type="submit"  class="btn btn-success">确定</button>
			</td>
		</tr>
	</table>
</fieldset>  
</form>
<script>
$("#edit_password_form").form({
		success : function(data) {
			var data = new Function("return" + data)();
			if (data.status == 1) {
				$.alert(data.info);
			}else{
				$.alert(data.info);
			}
		}
	});
</script>