<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<form id="add_notice" method='post' action="notice/saveNotice">
	<fieldset>
		<table class="form-table">
			<tr>
				<td>布告栏内容：</td>
				<td>
					<textarea class="J_notice_content" name="content" style="width: 500px;height: 200px;">${notice.content}</textarea>
				</td>
			</tr>
			<tr>
                <td>是否显示：</td>
                <td>
                    <label><input name="isShow" type="radio" value="1" ${notice.isShow==1?'checked=""':''} ${notice.isShow==null?'checked=""':''}>&nbsp;是</label>&nbsp;&nbsp;
                    <label><input name="isShow" type="radio" value="0" ${notice.isShow==0?'checked=""':''}>&nbsp;否</label>
                </td>
            </tr>
            <tr>
                <td>
                    <input class="jq-validatebox" type="hidden" name="orderId" value="1" />            
                </td>
            </tr>
			<tr class="tl">
				<td></td>
				<td class="form-actions">
					<button type="submit" class="btn btn-success" id="notice_info_btn">保存</button>
				</td>
			</tr>
		</table>
	</fieldset>
</form>
<script>
	// 表单提交
	$("#add_notice").form({
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
			$("#notice_info_btn").removeAttr('disabled').text('保存');
		}
	});
	/* 初始化编辑器 */
	 KindEditor.create(".J_notice_content",{
		items : [
				'source','|','fontsize','|', 'forecolor',  'bold', 'italic', 'underline','lineheight',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright'],
         afterChange : function() {
			    this.sync();
			},
			afterBlur:function(){
			    this.sync();
			}
     });
</script>