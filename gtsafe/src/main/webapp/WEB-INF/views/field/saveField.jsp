<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<form id="add" method='post'>
	<fieldset>
		<table align="center" class="form-table">
			<tr id="parent_module">
				<td>所属项目：</td>
				
				<td><input class="jq-combotree" type="text" name="pid"
					data-options="{
	                    required:true,
	                    url:'field/addFieldContents?level=1',
	                    method: 'post',
                    }" />
				</td>
			</tr>
			<tr>
				<td>子工地名称(或项目名称)：</td>
				<td><textarea name="fieldName" class="jq-validatebox"
						data-options="required:true" rows="3"></textarea></td>
			</tr>
			<tr>
				<td>排序：</td>
				<td><input class="jq-validatebox" type="text" name="orderId"
					value="1" /></td>
			</tr>
			<!-- <tr>
				<td>编号：</td>
				<td><input class="jq-validatebox" type="text" name="number"
					value="1" /></td>
			</tr> -->
			<input type ="hidden" name ="oldPidId" >
			<tr class="tl">
				<td></td>
				<td class="form-actions">
					<button type="submit" class="btn btn-success">确定</button></td>
			</tr>
		</table>
	</fieldset>
</form>
