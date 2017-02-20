<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>
<div class="jq-layout rel" data-options="fit:true">
	<div class="main-header" data-options="region:'north'" style="border: 1px solid #d4d4d4;">
		<ul class="f-cbli">
			<li>
				<button class="btn btn-success btn-sm J_add" type="button">新增触点项</button>
			</li>
			<li class="fr">
				<button id="contact_import" class="btn btn-success btn-sm" type="button">导入excel</button>
			</li>
			<li class="fr m0">
				<span class="f12">分类筛选：</span>
				<input class="jq-combotree" type="text" data-options="{
                    url:'contact/cateContents?level=2',
                    method: 'post',
                    onChange: changeCate,
                    onLoadSuccess: function() {
                    	GridElement.datagrid({
                    		url:'contact/list'
                    	})
                    }
                    }" />
			</li>	
				
		</ul>
	</div>
	<div data-options="region:'center',border:false" style="padding-top:8px;">
		<table class="jq-datagrid J_grid" fit="true" id="contact_grid" title="触点项列表" 
			data-options="
			method: 'post',
			toolbar: [{
				iconCls: 'icons icons-plus-sign',
					handler: function() {
						PanelContent.find('.J_add').click();
					}
				},'-',{
					iconCls:'icons icons-refresh',
					handler: function(){
						reloadGrid();
					}
			}],
			columns: [[
				{field:'name',title:'触点项名称',width: 250},
				{field:'number',title:'编号',width: 50},
				{field:'cateName',title:'所属分类',width: 100},
				{field:'typeName',title:'类型',width:80},
				{field:'attr',title:'触点',width:100, formatter: attrFmt},
				{field:'orderId',title:'排序',width:60},
				{field:'addTime',title:'更新时间',width:140},
				{field:'id',title:'操作',width:80,formatter: handleGrid}
			]],
			onLoadSuccess:loadGrid
			">
		</table>
	</div>	
</div>
<script>
	PanelContent.find('.J_add').on("click", function() {
		openW("contact/save", {
			formAction:"contact/save",
			title: "新增触点项"
		});
	});
	$("#contact_import").on("click", function() {
		openW("contact/contactImport", {title:'导入Excel'}, function() {
			excelInt({
				action: "contact/contactImport",
				href: "${__static__}/data/test.xlsx" //样本地址
			});
		})
	});
	function loadGrid() {
		popHandle().on("click", function() {
			var type = $(this).data("type");
			var index = $(this).data("index");
			var data = GridElement.datagrid("getRows")[index];
			var id = data.id;
			var typeName = data.type == "选择"?1:0;
			if (type == "edit") {
				openW("contact/edit", {
					formAction:"contact/edit",
					title: "编辑触点项",
					id: id
				}, function() {
					$("#add").form("load", {
						contactCateId: data.contactCateId,
						name: data.name,
						type: data.type,
						isSelect: data.isSelect,
						orderId: data.orderId,
						number: data.number,
						oldPidId: data.contactCateId,
						upNumber:data.upNumber
					});
				})
			} else if (type == "del") {
				updateItem("contact/del?id="+id,{
					param: {contactCateId:data.contactCateId}
				});
			}
		});
		$(".J_contactAddAttr").on("click", function() {
			var id = $(this).attr("data-id");
			var name = decodeURIComponent($(this).attr("data-name"));
			openW("contact/attr?id="+id, {
				title: '【'+name + "】触点明细",
				width: 1000,
				height: 400
			});

		})
	}
	function handleGrid(v,r,i) {
		var html = '<a href="javascript:;" class="icons icons-edit J_pop" title="编辑" data-type="edit" data-index='+i+'></a>';
			html += '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="icons icons-minus-sign J_pop" title="删除" data-type="del" data-index='+i+'></a>';
		return html;
	}

	// 筛选
	function changeCate(id) {
		GridElement.datagrid({
			url:"contact/list?id="+id
		})
	}
	function attrFmt(v, r) {
		var  encodeName =  encodeURIComponent(r.name);// 处理空格等特殊字符
		var html;
		if (v == "/") {
			return "/";
		} else {
			var t = v == 0?"(新增)":"(明细)";
			html = '<a class="txt-info J_contactAddAttr" href="javascript:;" data-id='+r.id+' data-name='+encodeName+'>'+t+'</a>';
			return v + html;
		}
	}
</script>