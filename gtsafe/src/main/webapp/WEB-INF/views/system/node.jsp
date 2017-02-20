<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="add_node" action="{z:u('admin/addNode')}" method="post">
			<table align="center" class="form-table" style="margin-top: 50px;">
				<tr>
					<td>权限节点：</td>
					<td>
						<input class="jq-validatebox" type="text" name="name"  data-options="required:true" />
					</td>
				</tr>
				<tr>
					<td>权限说明：</td>
					<td>
						<input class="jq-validatebox" type="text" name="alias" />
					</td>
				</tr>
				<tr>
					<td>上级节点：</td>
					<td>
						<select class="jq-combobox" name="node_pid" id="node_pid">
							<option selected="selected" value="0">==主节点==</option>
							<volist name='nodeList' id="vo">
								<option value="{$vo.id}">{$vo.alias}</option>
								<volist name="vo['child']" id="vo2">
									<option value="{$vo2.id}">　{$vo2.alias}</option>
								</volist>
							</volist>
						</select>
					</td>
				</tr>
				<tr>
					<td>备注：</td>
					<td>
						<input id="remark" type="text" name="remark"/>
					</td>
				</tr>
				<tr>
					<td>状态：</td>
					<td style="text-align:left;">
						<label for="trued">
							有效
							<input checked="checked" name="is_disabled" value="0" style="width:20px;" id="trued" type="radio"></label>
						&nbsp;&nbsp;
						<label for="falsed">
							禁用
							<input name="is_disabled" style="width:20px;" id="falsed" value="1" type="radio"></label>
					</td>
				</tr>
				<tr class="tar">
                <td></td>
                <td class="form-actions">
                    <button type="submit"  class="btn btn-success">确定</button>
                </td>
            </tr>
			</table>
		</form>
<script>
	// 表单提交
	$("#add_node").form({
		success: function(data) {
			var data = new Function("return" + data)(); 
			$.alert(data.info);
			if(data.status==1){
				setTimeout(function(){
					location.reload();
				},500);
			}
		}
	});
</script>