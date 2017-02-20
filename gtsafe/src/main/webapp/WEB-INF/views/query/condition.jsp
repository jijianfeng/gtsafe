<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<form id="add_queryCondition" method='post'>
	<div id="load" class="messager-button">
	<c:forEach items="${status}" var="status" >
		<ul class="mt5">
				<li><span class="f12">${status.fieldName}</span> <select
					id="" name="${status.oldValue}" class="jq-combobox">
						<c:forEach items="${status.value}" var="val">
							<option value="${val.text}">${val.text}</option>
						</c:forEach>
				</select> <input type="hidden" name="oldValue" value="${status.oldValue}" /></li>
		</ul>
		</c:forEach>
	</div>
	<div class="messager-button">
		<button class="btn btn-success" type="submit">
			<i class="icons icon-white icons-ok" ></i>确定
		</button>
		<button class="btn btn-default J_close" type="button">
			<i class="icons icons-remove" ></i>取消
		</button>
	</div>
</form>
<script>
$("#add_queryCondition").form({
    onSubmit: function() {
       var isValid = $(this).form('validate');
       
       if (isValid){
          $(this).find("button[type='submit']").attr('disabled','disabled').text('查询中');
       } else {
           return false;
       }
    },
    success:function(data){
    	var data = new Function("return" + data)();
        if (data.status == 1) {
        	var t_html = $(".form-actions").append('<span class="txt-success" id="submit_tip">'+data.info+'</span>');
            setTimeout(function(){
                $("button[type='submit']").removeAttr('disabled').text('确定');
                $("#submit_tip").remove();
                // 关闭弹出窗口
                ViewDialog.dialog('destroy');
                // 刷新数据表格
                $("#result").datagrid({
					columns: [data.data.title],
					data: data.data.list
				})
            },800);
        } else {
        	 $.alert(data.info);
             $("#add_queryCondition").find("button[type='submit']").removeAttr('disabled').text('确定');
        }
    }
});
</script>
