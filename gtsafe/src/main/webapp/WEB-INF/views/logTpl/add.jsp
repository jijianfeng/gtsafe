<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true" id="tpl_content">
	<div data-options="region:'north'" class="pb10 pt10 bdn">
		<span class="">模板名称</span>
		<input type="text" class="pct91 ml10" id="tpl_name">
	</div>

	<div data-options="region:'south',border:false" style="height: 120px;text-align: center;">
		<form action="logTpl/add" id="add_tpl" method="post">
			<input type="hidden" name="idStrings" id="item_list">
			<input type="hidden" name="name" id="tpl_name_form">
			<textarea name="remark" rows="3" class="mt10" style="width: 94%;" placeholder="请输入备注"></textarea>
			<div class="tplForm-action">
				<button type="submit" class="btn btn-success mt10" id="tpl_btn">确定</button>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false">
		<table fit="true" class="jq-treegrid J_grid" data-options="{
		url:'logTpl/cateList',
		method:'post',
		treeField:'name',
		pagination: false,
		checkOnSelect: false,
		pagination: false,
		columns: [[
		{field:'name',title:'触点项',width:440, formatter: checkboxFmt},
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
	     $(".J_checkbox:checked").each(function(){
	        var id = $(this).attr("id");
	        
	        if (id != "" && typeof id !== "undefined") {
	        	idList.push(id);	
	        }
	     })
	    $("#item_list").val(idList);
	}
	
	function checkboxFmt(v, r) {
		var checked = "";
		if (r.pid == 0) {
			return v;
		} else {
			if (r.isleaf == true) {
				return "<input class='J_checkbox' type='checkbox' "+checked+" id="+r.id+" />"+"<label for="+r.id+">&nbsp;&nbsp;"+v+"</label>";
			} else {
				return "<input class='J_checkbox' type='checkbox' "+checked+" data-type='root' />"+"<label>&nbsp;&nbsp;"+v+"</label>";
			}	
		}
	}

	// 表单提交
	$("#add_tpl").form({
		onSubmit: function() {
			getSelected();
			var tpl_name = $("#tpl_name").val();
			if (tpl_name == "") {
				$.alert("模板名称不能为空！");
				return false;
			} else {
				$("#tpl_name_form").val(tpl_name);
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
            $("#tpl_btn").removeAttr('disabled').text('确定');
        }
	});
	function loadGrid() {
		//去掉结点前面小图标
		$("#tpl_content .tree-file").removeClass("tree-file");
		$(".J_checkbox").parents(".datagrid-cell").addClass("datagrid-cell-check").css({"text-align":"left","padding":"0 4px"});
		
		$(".J_checkbox").on("click", function() {
			var type = $(this).data("type");
			if (type == "root") {
				var $node = $(this).parents(".datagrid-row");
				var $subNode = $node.next();
				var isCked = $(this).prop("checked");
				if (isCked == true) {
					$subNode.find("tr .J_checkbox").prop("checked", true);
				} else {
					$subNode.find("tr .J_checkbox").prop("checked", false);
				}
			} else {
				var $node = $(this).parentsUntil(".treegrid-tr-tree");
				var $parentNode = $(this).parents(".treegrid-tr-tree").prev(".datagrid-row");
				var len = $node.find("tr").length;
				var ckedLen = $node.find("tr input:checked").length;
				var $self = $(this);
				if (len == ckedLen) {
					$parentNode.find(".J_checkbox").prop("checked", true);
				} else {
					$parentNode.find(".J_checkbox").prop("checked", false);
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