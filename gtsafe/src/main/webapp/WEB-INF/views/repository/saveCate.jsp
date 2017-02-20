<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="add" method='post'>
    <fieldset>
        <table align="center" class="form-table">
            <tr id="parent_module">
                <td>上级分类：</td>
                <td>
                   <input class="jq-combotree" type="text" name="pid" data-options="{
                    required:true,
                    url:'repository/cateList?level=1'
                    }" />
                </td>
            </tr>
            <tr>
                <td>分类名称：</td>
                <td>
                    <textarea name="name"  class="jq-validatebox" data-options="required:true" rows="3"></textarea>
                </td>
            </tr>
<!--             <tr>
                <td>排序：</td>
                <td>
                    <input class="jq-validatebox" type="text" name="orderId" value="1" />            
                </td>
            </tr> -->
            <tr class="tl">
                <td></td>
                <td class="form-actions">
                    <button type="submit"  class="btn btn-success">确定</button>
                </td>
            </tr>
        </table>
    </fieldset>  
</form>
