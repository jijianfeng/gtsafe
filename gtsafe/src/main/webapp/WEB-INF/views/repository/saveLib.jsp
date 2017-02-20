<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<!-- <input class="jq-combobox" type="text" data-options="{
	required:true,
	url:'${__static__}/data/member_role2.json'
}"> -->
<form id="add" method='post' enctype="multipart/form-data">
	<fieldset>
		<table align="center" class="form-table">
		<tr>
			<td>所属分类：</td>
			<td>
			<input class="jq-combotree" id="lib_id"  type="text" name="docsCateId" data-options="{
                      required:true,
                      url:'repository/cateList?level=2',
                      onBeforeSelect: function(node) {
                          if (!$(this).tree('isLeaf', node.target)) {
                              return false;
                          }
                      },
                      onClick: function(node) {
                          if (!$(this).tree('isLeaf', node.target)) {
                             $('#lib_id').combo('showPanel');
                          }
                      }
                  }">
			</td>
		</tr>
		<tr>
			<td>文档编号：</td>
			<td>
				<input name="number" type="text" class="jq-validatebox" data-options="required:true" />
			</td>
		</tr>
		<tr>
			<td>文档主题：</td>
			<td>
				<input name="docsName" type="text" class="jq-validatebox" data-options="required:true" />
			</td>
		</tr>
		<tr>
			<td>施行日期：</td>
			<td><input name="build" type="text" class="jq-datebox"	data-options="required:true,editable:false"  /> 
		</tr>
		<tr>
            <td>上传文档：</td>
            <td>
               <input class="jq-validatebox" type="file" name="cover_pic" data-options="" style="width:175px;">
               <br/>
            </td>   
        </tr>
		<tr>
			<td>发布单位：</td>
			<td>
				<input name="releaseUnit" type="text" class="jq-validatebox" />
			</td>
		</tr>
        <tr>
			<td>备注：</td>
			<td>
				<textarea name="remarks" type="text" rows ="4" class="jq-validatebox" ></textarea>
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
