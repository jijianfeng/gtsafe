<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="add" method="post">
    <fieldset>
        <table align="center" class="form-table">
            <tr>
                <td>权限节点：</td>
                <td>
                    <input name="name" type="text" class="jq-validatebox" data-options="required:true" />
                </td>
            </tr>
            <tr>
                <td>权限说明：</td>
                <td>
                    <input name="alias" type="text" class="jq-validatebox" data-options="required:true" />
                </td>
            </tr>
            <tr>
				<td>上级节点：</td>
				<td>
					<select name="pid" class="jq-combobox" data-options="{required:true}">
						<option value="0">顶级节点</option>
						<c:forEach items="${nodeList}" var="vo">
							<option value="${vo.id}">${vo.alias}</option>
							<c:forEach items="${vo.node}" var="vo2">
								<option value="${vo2.id}">${vo2.alias }</option>
							</c:forEach>
						</c:forEach>
					</select>
				</td>
			</tr>
            <tr>
                <td>备注：</td>
                <td>
                   <input name="remark" type="text" class="jq-validatebox" data-options="required:true" />
                </td>
            </tr>
            <tr>
                <td>状态：</td>
                <td>
                    <label><input name="status" type="radio" value="1" checked="">&nbsp;启用</label>&nbsp;&nbsp;
                    <label><input name="status" type="radio" value="0">&nbsp;禁用</label>
                </td>
            </tr>
            <tr>
                <td>是否为菜单：</td>
                <td>
                    <label><input name="isMenu" type="radio" value="1" checked="">&nbsp;是</label>&nbsp;&nbsp;
                    <label><input name="isMenu" type="radio" value="0">&nbsp;否</label>
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