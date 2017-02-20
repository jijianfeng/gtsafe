<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="add" method="post">
    <fieldset>
        <table align="center" class="form-table">
            <tr>
                <td>角色名称：</td>
                <td>
                    <input name="name" type="text" class="jq-validatebox" data-options="required:true" />
                </td>
            </tr>
            <tr>
                <td>备注：</td>
                <td>
                   <textarea name="remark" rows="3"></textarea>
                </td>
            </tr>
            <tr>
                <td>状态：</td>
                <td>
                    <label><input name="status" type="radio" value="1" checked="">&nbsp;启用</label>&nbsp;&nbsp;
                    <label><input name="status" type="radio" value="0">&nbsp;禁用</label>
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