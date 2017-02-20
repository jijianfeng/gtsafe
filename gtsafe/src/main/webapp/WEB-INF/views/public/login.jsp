<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://zlzkj.com/tags" prefix="z"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>登录</title>
<link href="${__static__}/app/admin.css" rel="stylesheet" type="text/css" />
<link href="${__static__}/app/style.css" rel="stylesheet" type="text/css" />
<script src="${__static__}/jquery/jquery.js" type="text/javascript"></script>
<script src="${__static__}/jquery/jquery.form.js" type="text/javascript"></script>

</head>
<body class="bg-gray">
	<div id="header" style="min-width: 1080px;">
	</div>
	<div class="container fix" style="width: 1080px;">
		<div class="fl mt80 w700 h400 bde">
			<img src="${__static__}/app/images/publicity.jpg" alt="宣传图">
		</div>
		<div class="fl ml30 bdd mt80 bg-white">
			<form id="login_form" class="form-horizontal mr20 pt50 pr50 pb132" method="post">
				<div class="control-group">
					<h3 class="tc">用户登录</h3>
				</div>
				<div class="control-group">
					<label class="control-label" for="inputEmail">账号</label>
					<div class="controls">
						<input type="text" id="inputAccount" name="account" placeholder="账号" value="">
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="inputPassword">密码</label>
					<div class="controls">
						<input type="password" id="inputPassword" name="password" placeholder="密码" value="">
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<button type="submit" class="btn btn-info">登录</button>
						&nbsp;&nbsp;<span id="login_tip"></span>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div id="footer" class="bottom mt50">
		<span class="copyright">&copy;<script>document.write(new Date().getFullYear());</script>
			杭州施恩建设工程咨询有限公司 安全标准化管理软件研发课题组 版权所有</span>
	</div>
	<script>
		$(function(){
			$("#inputAccount").focus();
		});
		//登录
		var $login_name = $("#login_form input[name='account']");
        var $password = $("#login_form input[name='password']");
        var $msgTip = $("#login_tip");
		$("#login_form").ajaxForm({
			beforeSubmit:function(){
				if ($login_name.val() == '') {
					$msgTip.addClass('txt-warning').text('账号不能为空！');
					return false;
				}
				if ($password.val() == '') {
					$msgTip.addClass('txt-warning').text('密码不能为空！');
					return false;
				}
				$msgTip.removeClass('red');
				$msgTip.addClass('txt-success').text('登录中...');    
	        
	      },
	      success:function(data){
	      		//var data = new Function('return'+data)();
				if (data.status == 1){
					$msgTip.addClass('txt-success').text('登录成功,正在跳转...');
					setTimeout(function(){
						$("#login_loading").text('');
						location.href = "${z:u('/')}";
						//location.href = "{:u('index')}";
					},1500);
				} else {
					$msgTip.addClass('txt-warning').text(data.info);
				}
	      },
	      dataType:"json"
	    });
	</script>
</body>
</html>