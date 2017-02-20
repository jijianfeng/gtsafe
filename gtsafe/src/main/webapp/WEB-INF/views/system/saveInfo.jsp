<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<!-- <input class="jq-combobox" type="text" data-options="{
	required:true,
	url:'${__static__}/data/member_role2.json'
}"> -->
<form id="add" method='post'>
	<fieldset>
		<table align="center" class="form-table">
		<tr>
			<td>设置项：</td>
			<td>
					<input name="setting" class="jq-combobox" type="text" data-options="{
					required:true,
					url:'${__static__}/data/system_info2.json'
				}">
			</td>
		</tr>
		<tr>
			<td>值：</td>
			<td>
				<input name="values" type="text" class="jq-validatebox" data-options="required:true" />
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