<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<form id="add_contactAttr" action ="contact/addAttr" method='post'>
	<ul class="f-cbli">
	<input type="hidden" name="id" value="${id}"/>
		<li class="mr20"><span class="f12">内容：</span> <input
			class="jq-validatebox" name="name" type="text"
			data-options="required:true"></li>
		<li><span class="f12">重要度：</span> <input class="jq-numberspinner" min="-1000"
			name="important" value="0" style="width:60px;" type="text"
			data-options="min: 0"></li>
		<li><span class="f12">分值：</span> <input class="jq-numberspinner" min="-1000"
			name="score" value="0" style="width:60px;" type="text"
			data-options="min: 0"></li>
		<li><span class="f12">应对措施：</span> <input class="jq-validatebox"
			name="measures" type="text" style="width: 340px"></li>
		<li class="m0">
			<button type="submit" class="btn btn-success btn-sm"
				id="addAttr">确定</button>
		</li>
	</ul>
</form>
<script type="text/javascript">
$("#add_contactAttr").form({
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
            if(data.status==1){
            	$.alert(data.info, function() {
                	$("#addAttr").removeAttr('disabled').text('确定');
                    reloadGrid("#contact_attr_grid");
                });
            	ViewDialog.dialog('destroy');
            }else{
	            $.alert(data.info, function() {
	            	$("#addAttr").removeAttr('disabled').text('确定');
	                reloadGrid("#contact_attr_grid");
	            });
            }
        }
    });
</script>