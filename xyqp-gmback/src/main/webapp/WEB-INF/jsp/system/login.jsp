<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登录</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/system/login.css" />
</head>
<body>
<div class="content">
	<div class="login-panel">
		<form id="login-form" action="login" method="post">
			<p class="login-title">登录系统</p>
			<img src="${cp}/img/logo-image.png" class="login-logo">
			<div class="login-input">
				<label class="glyphicon glyphicon-user"></label>
				<input type="text" name="account" maxlength="16" placeholder="请输入您的账号">
			</div>
			<div class="login-input">
				<label class="glyphicon glyphicon-lock"></label>
				<input type="password" name="password" maxlength="16" placeholder="请输入您的密码">
			</div>
			<div>
				<input type="text" id="code-input" name="verifyCode" maxlength="6" placeholder="验证码"/>
				<img id="code-img" src="${cp}/system/imageVerifyCode" />
			</div>
			<div class="login-msg">${error}</div>
			<button class="btn btn-login">登录</button>
		</form>
	</div>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript" src="${cp}/libs/bootstrap/js/bootstrap.js" ></script>
<script>
$("#login-form").submit(function(){
	var form = $(this);
	var name = form.find("[name=account]").val();
	if (!name) {
		$(".login-msg").html("用户名不能为空！");
		return false;
	}
	var password = form.find("[name=password]").val();
	if (!password) {
		$(".login-msg").html("密码不能为空！");
		return false;
	}
	var code = $("#code-input").val();
	if (!code) {
		$(".login-msg").html("验证码不能为空！");
		return false;
	}
});

$("#login-form [name=password]").keydown(function(ev){
	if (ev.keyCode == 13) {
		$("#login-form").submit();
	}
});

$("#code-img").click(function(){
	this.src = "../system/imageVerifyCode?r=" + Math.random();
});
</script>
</html>
