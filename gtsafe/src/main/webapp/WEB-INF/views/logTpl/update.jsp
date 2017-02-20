<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true" id="update_tpl_content">
	<div data-options="region:'north'" class="pb10 pt10 bdn">
		<span class="">模板名称</span>
		<input type="text" class="pct91 ml10" id="update_tpl_name">
	</div>

	<div data-options="region:'south',border:false" style="height: 120px;text-align: center;">
		<form  id="update_tpl" method="post">
			<input type="hidden" name="idStrings" id="update_item_list">
			<input type="hidden" name="name" id="update_tpl_name_form">
			<textarea name="remark" rows="3" class="mt10" style="width: 94%;" placeholder="请输入备注"></textarea>
			<div class="tplForm-action">
				<button type="submit" class="btn btn-success mt10" id="update_tpl_btn">确定</button>
				<button class="btn btn-default J_close mt10" type="button">取消</button>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false">
		<table fit="true" class="jq-treegrid J_grid" data-options="{
		url:'logTpl/cateList?templateId=${templateId}',
		method:'post',
		treeField:'name',
		pagination: false,
		checkOnSelect: false,
		columns: [[
		{field:'name',title:'触点项',width:440, formatter: updateCheckboxFmt},
		{field:'isSelect',title:'是否必选',width:200},
		{field:'typeName',title:'触点项类型',width:200}
		]],
		onLoadSuccess:loadGrid
	}">
		</table>
	</div>
</div>


<script>
	//获取选中的结点,并注入到表单中
	function getSelected(){ 
	    var idList = [];  
	     $(".J_tpl_update_checkbox:checked").each(function(){
	        var id = $(this).attr("id"); 
	        if (id != "" && typeof id !== "undefined") {
	        	idList.push(id);	
	        }
	     })
	    $("#update_item_list").val(idList);
	}
	
	function updateCheckboxFmt(v, r) {
		var checked = "";
		if (r.status == 1) {
			checked = "checked";
			setTimeout(function() {
				$("#update_tpl_content [node-id="+r.id+"]");
			},50);
		}
		if (r.pid == 0) {
			return v;
		} else {
			if (r.isleaf == true) {
				return "<input class='J_tpl_update_checkbox' type='checkbox' "+checked+" id="+r.id+" />"+"<label for="+r.id+">&nbsp;&nbsp;"+v+"</label>";
			} else {
				return "<input class='J_tpl_update_checkbox' type='checkbox' "+checked+" data-type='root' />"+"<label>&nbsp;&nbsp;"+v+"</label>";
			}	
		}
	}

	// 表单提交
	$("#update_tpl").form({
		onSubmit: function() {
			getSelected();
			var tpl_name = $("#update_tpl_name").val();
			if (tpl_name == "") {
				$.alert("模板名称不能为空！");
				return false;
			} else {
				$("#update_tpl_name_form").val(tpl_name);
			}
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
            $("#update_tpl_btn").removeAttr('disabled').text('确定');
        }
	});
	function loadGrid() {
		//去掉结点前面小图标
		$("#update_tpl_content .tree-file").removeClass("tree-file");

		$(".J_tpl_update_checkbox").parents(".datagrid-cell").addClass("datagrid-cell-check").css({"text-align":"left","padding":"0 4px"});
		$(".J_tpl_update_checkbox").on("click", function() {
			var type = $(this).data("type");
			if (type == "root") {
				var $node = $(this).parents(".datagrid-row");
				var $subNode = $node.next();
				var isCked = $(this).prop("checked");
				if (isCked == true) {
					$subNode.find("tr .J_tpl_update_checkbox").prop("checked", true);
				} else {
					$subNode.find("tr .J_tpl_update_checkbox").prop("checked", false);
				}
			} else {
				var $node = $(this).parentsUntil(".treegrid-tr-tree");
				var $parentNode = $(this).parents(".treegrid-tr-tree").prev(".datagrid-row");
				var len = $node.find("tr").length;
				var ckedLen = $node.find("tr input:checked").length;
				var $self = $(this);
				if (len == ckedLen) {
					$parentNode.find(".J_tpl_update_checkbox").prop("checked", true);
					console.log($parentNode)
				} else {
					$parentNode.find(".J_tpl_update_checkbox").prop("checked", false);
				}
			}
		});
		
		$("[node-id]").each(function() {
			var id = $(this).attr("node-id");
			if (id.indexOf("pid") > -1) {
				$(this).css({
					background: "#fff",
					color: "#333"
				});
			}
		});

	}
	// 兼容不支持placeholder的浏览器
	if (isIE8 || isIE9) { // ie7&ie8
        $('input[placeholder]:not(.placeholder-no-fix), textarea[placeholder]:not(.placeholder-no-fix)').each(function () {
            var input = $(this);
            if(input.val()=='' && input.attr("placeholder") != '') {
                input.addClass("placeholder").val(input.attr('placeholder'));
            }

            input.focus(function () {
                if (input.val() == input.attr('placeholder')) {
                    input.val('');
                }
            });
            input.blur(function () {                         
                if (input.val() == '' || input.val() == input.attr('placeholder')) {
                    input.val(input.attr('placeholder'));
                }
            });
        });
    }
</script>