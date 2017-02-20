<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'"
		style="border: 1px solid #d4d4d4;padding: 5px;">
			<ul class="f-cbli">
				<!-- <li class="mr20"><span class="f12">内容：</span> <input
					class="jq-validatebox" name="name" type="text"
					data-options="required:true"></li>
				<li><span class="f12">重要度：</span> <input
					class="jq-numberspinner" min="-1000" name="important" value="0"
					style="width:60px;" type="text" data-options="min: 0"></li>
				<li><span class="f12">分值：</span> <input
					class="jq-numberspinner" min="-1000" name="score" value="0" style="width:60px;"
					type="text" data-options="min: 0"></li>
				<li><span class="f12">应对措施：</span> <input
					class="jq-validatebox" name="measures" type="text"
					style="width: 340px"></li> -->
				<li class="m0">
					<button type="" class="btn btn-success btn-sm"
						id = "addContactAttr">新增</button>
				</li>
			</ul>
	</div>
	<div data-options="region:'center',border:false"
		style="padding-top:8px;">
		<table class="jq-datagrid J_grid" id="contact_attr_grid" fit="true"
			title="触点属性列表"
			data-options="
			url:'${__url__}',
			method : 'post',
			toolbar: [{
				iconCls:'icons icons-refresh',
				handler: function(){
					reloadGrid('#contact_attr_grid');
				}
			}],
			columns: [[
				{field:'name',title:'内容',width: 150},
				{field:'important',title:'重要度',width: 60},
				{field:'score',title:'分值',width:60},
				{field:'measures',title:'应对措施',width:300},
				{field:'addTime',title:'添加时间',width:140},
				{field:'id',title:'操作',width:80,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
			">
		</table>
	</div>
</div>
<script>
	$("#addContactAttr").on("click", function() {
		openW("contact/addAttr?id="+${id}, {
			title: "新增触点项",
			width: "1000"
		});
	});
	/* $("#add_contactAttrForm").form({
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
            $.alert(data.info, function() {
            	$("#addContactAttr").removeAttr('disabled').text('确定');
                reloadGrid("#contact_attr_grid");
            });
        }
    }); */
	function loadGrid() {
		$(".J_pop_contactAttr").on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = $("#contact_attr_grid").datagrid("getRows")[index];
			var id = $(this).attr("data-id");
			
			if (type == "edit") {
				openW("contact/editAttr", {
					title: "编辑属性项",
					width: "1000",
					id: id
				}, function() {
					$("#edit_contactAttr").form("load", {
						id:data.id,
						name: data.name,
						important: data.important,
						score: data.score,
						measures: data.measures
					});
				})
			}else if(type == "del"){
				updateItem("contact/delAttr",{
					param: {id:id},
					gridId: "#contact_attr_grid"
				});
			};
		})
	}
	function handleGrid(v,r,i) {
		var html = '<a href="javascript:;" class="icons icons-edit J_pop_contactAttr" title="编辑" data-type="edit" data-index='+i+'></a>';
		html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-minus-sign J_pop_contactAttr" title="删除" data-type="del" data-id='+r.id+'></a>';
		return html;
	}
</script>