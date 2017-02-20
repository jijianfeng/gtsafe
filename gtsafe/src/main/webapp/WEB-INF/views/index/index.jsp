<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z" %>
<%@ page session="false" %>

<!doctype html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>GS安全生产动态警示软件系统</title>
	<link href="${__static__}/app/admin.css" rel="stylesheet" type="text/css"/>
	<link href="${__static__}/app/style.css" rel="stylesheet" type="text/css"/>
	<link href="${__static__}/mod/fancybox/jquery.fancybox.css" rel="stylesheet" />
	<script src="${__static__}/jquery/jquery.js" type="text/javascript"></script>
	<script src="${__static__}/ui/jqui.min.js" type="text/javascript"></script>
	<script src="${__static__}/mod/fancybox/jquery.fancybox.min.js" type="text/javascript"></script>
	
	<script>
		(function(){
			var ZLZ = window.ZLZ = {
				"ROOT"   : "${__root__}",
				"APP"    : "__APP__",
				"PUBLIC" : "__PUBLIC__",
				"URL" : "__URL__",
				"SELF":"__SELF__"
			};
		})();
	</script>
</head>
<body class="jq-layout zlz-style">
<div id="loading" style="position: absolute; z-index: 1000; top: 0px; left: 0px; width: 100%; height: 100%; background: #fff;">
	<img style="margin:20% 0 0 50%" src="${__static__}/app/images/loading.gif" alt="">
</div>
<!-- header -->
<div id="header" data-options="region:'north',border:false">
	<div class="f-correct login-info">
		<ul style="position:relative;">
			<li class="mg0">欢迎&nbsp;[&nbsp;${role.name}&nbsp;]:</li>
			<li>${name.name}</li>
			<li>
				<a id="edit_password" href="javascript:;" style='color: #15BAA8;'>[修改密码]</a>&nbsp;
				<a onclick="this.innerHTML='正在退出...';$.post('${z:u('public/logout')}',function(data){location.reload();},'json');" href="javascript:;" style='color: #15BAA8;'>[退出]</a>
			</li>
		</ul>
		
	</div>
</div>
<!-- /header -->
<!-- menuLeft -->
<div data-options="region:'west',split:true,title:'目录导航'" iconCls="icons icons-th-list" style="width:210px;">
	<div id="navLeft" class="jq-accordion" fit="true" border="false" animate="false">
		<c:forEach items="${node}" var="node1" varStatus="number1">
		<div title="${node1.alias}" iconCls="icons icons-th">
			<ul lines="true" fit="true" class="jq-tree navSub" data-index="${number1.index}">
				<c:forEach items="${node1.node}" var="node2" varStatus="number2">
				<li>
					<span>${node2.alias}</span>
					<ul>
						<c:forEach items="${node2.node}" var="node3" varStatus="number3">
						<li id="${node3.name}">${node3.alias}</li>
						</c:forEach>			
					</ul>
				</li>
				</c:forEach>
				
			</ul>
		</div>
		</c:forEach>
		<!-- <div title="公共信息管理" iconCls="icons icons-th">
			<ul lines="true" fit="true" class="jq-tree navSub" data-index="0">
				<li>
					<span>人员管理</span>
					<ul>
						<li id="member/role">角色管理</li>
						<li id="member/user">用户管理</li>
					</ul>
				</li>
				<li>
					<span>触点项管理</span>
					<ul>
						<li id="contact/cate">分类管理</li>
						<li id="contact/list">触点项管理</li>
					</ul>
				</li>
				<li>
					<span>布告栏管理</span>
					<ul>
						<li id="notice/notice">布告管理</li>
					</ul>
				</li>
				<li>
					<span>系统设置</span>
					<ul>
						<li id="system/info">基础信息设置</li>
						<li id="system/backup">数据库备份</li>
						<li id="system/log">操作日志管理</li>
						<li id="system/node">权限节点设置</li>
					</ul>
				</li>
			</ul>
		</div>
		
		<div title="安全日志管理" iconCls="icons icons-th">
			<ul lines="true" fit="true" class="jq-tree navSub" data-index="1">
				<li>
					<span>安全日志填报管理</span>
					<ul>
						<li id="safeLog/add">新增日志</li>
						<li id="safeLog/list">日志列表</li>
					</ul>
				</li>
				<li>
					<span>日志模板管理</span>
					<ul>
						<li id="logTpl/add">新增模板</li>
						<li id="logTpl/list">模板列表</li>
					</ul>
				</li>
			</ul>
		</div>
		<div title="统计查询" iconCls="icons icons-th">
			<ul lines="true" fit="true" class="jq-tree navSub" data-index="2">
				<li id="query/custom">自定义查询</li>
			</ul>
		</div>
		<div title="安全知识库管理" iconCls="icons icons-th">
			<ul lines="true" fit="true" class="jq-tree navSub" data-index="3">
				<li id="repository/lib">文档列表</li>
				<li id="repository/cate">分类管理</li>
			</ul>
		</div> -->
		
		
		<!-- <div title="系统管理" iconCls="icons icons-th">
			<ul lines="true" fit="true" class="jq-tree navSub" data-index="4">
				
			</ul>
		</div> -->
	</div>
</div>
<!-- /menuLeft -->
<!-- content -->
<div id="content" data-options="region:'center'">
	<div id="content_tabs" fit="true" border="false" tools="#tab-tools">
		<!-- <div title="首页" closable="false" cache="false" href="home"></div> -->
	</div>
</div>
<!-- /content -->
<!-- footer -->
<div id="footer" data-options="region:'south',border:false,split:true">
<span class="copyright">&copy;<script>document.write(new Date().getFullYear());</script> 杭州施恩建设工程咨询有限公司 安全标准化管理软件研发课题组 版权所有</span>
</div>
<!-- /footer -->
<!-- tabTool -->
<div id="tab-tools" style="display:none;">
	<a href="javascript:void(0)" class="jq-linkbutton" plain="true" id="tabToolsOne" iconCls="icon-window-max" onclick="changeScreen()">全屏</a>
</div>
<!-- /tabTool -->
<!-- tabMenu -->
<div id="contextMenu" class="jq-menu" style="width:120px;display:none;">
	<div id="menuRefresh" data-options='iconCls:"icons icons-refresh"'>刷新当前</div>
	<div class="menu-sep"></div>
	<div id="menuCloseThis" data-options='iconCls:"icons icons-remove"'>关闭当前</div>
	<div id="menuCloseOther" data-options='iconCls:"icons icons-remove-sign"'>关闭其它</div>
	<div id="menuCloseAll" data-options='iconCls:"icons icons-remove-sign"'>关闭所有</div>
</div>
<!-- /tabMenu -->
<script src="${__static__}/ui/jqui_extend.js" type="text/javascript"></script>
<script src="${__static__}/jquery/jquery.hashchange.min.js" type="text/javascript"></script>
<script src="${__static__}/app/admin.js" type="text/javascript"></script>
<script type="text/javascript" src="${__static__}/addons/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${__static__}/addons/kindeditor/kindeditor.min.js"></script>
<script src="${__static__}/jquery/jquery.vticker.js" type="text/javascript"></script>
<!--[if lt IE 8]>
		<script src="${__static__}/patch/json2.js"></script>
	<![endif]-->
	<script>
		var domain = location.hash;
		if (!domain) {
			location.href = "#"+'member/role';
		}
		$("#edit_password").on("click", function() {
			openW("member/editPassword", {
				title : "修改密码"
			});
		});
		function timercheck(){
			$.ajax({
	    		url: "${z:u('public/timercheck')}",
	    		type: 'GET',
	    		dataType: 'JSON',
	    		success: function(data){
	    			if (data.status == 0){
	    				alert(data.info);
	    			}else {
	    				alert("验证超级狗错误");
	    			}
	    			location.reload();
	    		}
    		});
			setTimeout(timercheck, 5*1000);
		}
		//要测试别的功能关闭一下
		timercheck();
		
		
	</script>	
</body>
</html>
