<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="add" method='post'>
    <fieldset>
        <table align="center" class="form-table">
            <tr id="parent_module">
                <td>所属分类：</td>
                <td>
                   <input class="jq-combotree" id="cate_id2"  type="text" name="contactCateId" data-options="{
                        required:true,
                        url:'contact/listContactCate?level=2',
                        method: 'post',
                        onBeforeSelect: function(node) {
                            if (!$(this).tree('isLeaf', node.target)) {
                                return false;
                            }
                        },
                        onClick: function(node) {
                            if (!$(this).tree('isLeaf', node.target)) {
                               $('#cate_id2').combo('showPanel');
                            }
                        }
                    }" />
                </td>
            </tr>
            <tr>
                <td>触点项名称：</td>
                <td>
                    <input name="name" type="text" class="jq-validatebox" data-options="required:true" />
                </td>
            </tr>
            <tr>
                <td>类型：</td>
                <td>
                    <label><input name="type" type="radio" value="1" checked="">&nbsp;选择</label>&nbsp;&nbsp;
                    <label><input name="type" type="radio" value="0">&nbsp;简答</label>
                    <label><input name="type" type="radio" value="2">&nbsp;简答(预置)</label>
                </td>
            </tr>
            <tr>
                <td>是否必选：</td>
                <td>
                    <label><input name="isSelect" type="radio" value="1" checked="">&nbsp;是</label>&nbsp;&nbsp;
                    <label><input name="isSelect" type="radio" value="0">&nbsp;否</label>
                </td>
            </tr>
            <tr>
                <td>排序：</td>
                <td>
                    <input class="jq-validatebox" type="text" name="orderId" value="1" />            
                </td>
            </tr>
             <input type ="hidden" name ="oldPidId"/>
            <%--<tr>
				<td>编号：</td>
				<td><input class="jq-validatebox" type="text" name="number"
					value="1" /></td>
			</tr>
            --%><tr class="tl">
                <td></td>
                <td class="form-actions">
                    <button type="submit"  class="btn btn-success">确定</button>
                </td>
            </tr>
           
        </table>
    </fieldset>  
</form>