<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.starland.com/system" prefix="sys" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>星羽游戏管理后台</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/system/index.css" />
</head>
<body>
<div class="header">
	<img class="logo-text" src="${cp}/img/logo-text.png" />
	<div class="header-bar">
		<div id="btn-logout" class="header-btn">退出</div>
		<div id="btn-update-password" class="header-btn">修改密码</div>
		<div id="btn-detail" class="header-name">您好，${user.id}</div>
	</div>
</div>
<div class="menu">
<sys:permissionGroup>
	<div class="menu-item menu-active">
		<div class="menu-head">
			<div class="menu-icon glyphicon glyphicon-cog"></div>
			<div class="menu-title">系统管理</div>
			<div class="menu-down glyphicon glyphicon-menu-left"></div>
		</div>
		<div class="menu-content">
		<sys:permission url="/user/list">
			<div class="menu-node" data-src="${cp}/user/list">用户列表</div>
		</sys:permission>
		<sys:permission url="/role/list">
			<div class="menu-node" data-src="${cp}/role/list">角色列表</div>
		</sys:permission>
		<sys:permission url="/module/list">
			<div class="menu-node" data-src="${cp}/module/list">模块列表</div>
		</sys:permission>
		</div>
	</div>
</sys:permissionGroup>
<sys:permissionGroup>
	<div class="menu-item">
		<div class="menu-head">
			<div class="menu-icon glyphicon glyphicon-plane"></div>
			<div class="menu-title">游戏信息</div>
			<div class="menu-down glyphicon glyphicon-menu-left"></div>
		</div>
		<div class="menu-content">
		<sys:permission url="/gameRoom/list">
			<div class="menu-node" data-src="${cp}/gameRoom/list">房间列表</div>
		</sys:permission>
		<sys:permission url="/diamondConsume/list">
			<div class="menu-node" data-src="${cp}/diamondConsume/list">钻石消耗记录</div>
		</sys:permission>
		<sys:permission url="/goldConsume/list">
			<div class="menu-node" data-src="${cp}/goldConsume/list">金币消耗记录</div>
		</sys:permission>
		</div>
	</div>
</sys:permissionGroup>
<sys:permissionGroup>
	<div class="menu-item">
		<div class="menu-head">
			<div class="menu-icon glyphicon glyphicon-user"></div>
			<div class="menu-title">代理商管理</div>
			<div class="menu-down glyphicon glyphicon-menu-left"></div>
		</div>
		<div class="menu-content">
		<sys:permission url="/agent/list">
			<div class="menu-node" data-src="${cp}/agent/list">代理商列表</div>
		</sys:permission>
		<sys:permission url="/extractDeposit/list">
			<div class="menu-node" data-src="${cp}/extractDeposit/list">提现列表</div>
		</sys:permission>
		</div>
	</div>
</sys:permissionGroup>
<sys:permissionGroup>
	<div class="menu-item">
		<div class="menu-head">
			<div class="menu-icon glyphicon glyphicon-link"></div>
			<div class="menu-title">我的推广</div>
			<div class="menu-down glyphicon glyphicon-menu-left"></div>
		</div>
		<div class="menu-content">
		<sys:permission url="/user/mylist">
			<div class="menu-node" data-src="${cp}/user/mylist">推广用户</div>
		</sys:permission>
		<sys:permission url="/agent/twoList">
			<div class="menu-node" data-src="${cp}/agent/twoList">二级代理</div>
		</sys:permission>
		<sys:permission url="/agent/threeList">
			<div class="menu-node" data-src="${cp}/agent/threeList">三级代理</div>
		</sys:permission>
		</div>
	</div>
</sys:permissionGroup>
<sys:permissionGroup>
	<div class="menu-item">
		<div class="menu-head">
			<div class="menu-icon glyphicon glyphicon-usd"></div>
			<div class="menu-title">返利查询</div>
			<div class="menu-down glyphicon glyphicon-menu-left"></div>
		</div>
		<div class="menu-content">
		<sys:permission url="/diamondIncome/list">
			<div class="menu-node" data-src="${cp}/diamondIncome/list">钻石返利</div>
		</sys:permission>
		<sys:permission url="/goldIncome/list">
			<div class="menu-node" data-src="${cp}/goldIncome/list">金币返利</div>
		</sys:permission>
		<sys:permission url="/extractDeposit/add">
			<div class="menu-node" data-src="${cp}/extractDeposit/add">提现</div>
		</sys:permission>
		<sys:permission url="/extractDeposit/mylist">
			<div class="menu-node" data-src="${cp}/extractDeposit/mylist">提现记录</div>
		</sys:permission>
		</div>
	</div>
</sys:permissionGroup>
</div>
<div class="content">
	<iframe class="main-iframe" src="${cp}/user/mydetail"></iframe>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript">
$(".menu-active .menu-content").show();

$(".menu-head").click(function(){
	var item = $(this).parent();
	if (item.hasClass("menu-active")) {
		item.removeClass("menu-active");
		item.find(".menu-content").slideUp(200);
	} else {
		$(".menu-active .menu-content").slideUp(200);
		$(".menu-active").removeClass("menu-active");
		item.addClass("menu-active");
		item.find(".menu-content").slideDown(200);
	}
});

$(".menu-node").click(function(){
	var src = $(this).data("src");
	$(".main-iframe").attr("src", src);
});

seajs.config({
	alias: {
		"dialog": seajs.data.base + "dialog/js/dialog",
		"request": seajs.data.base + "request/js/request"
	}
});

$("#btn-logout").click(function(){
	seajs.use(["dialog", "request"], function(dialog, request){
		dialog.showConfirm("你确定退出登录吗？", function(){
			request.submit({
				url: "logout"
			});
		});
	});
});

$("#btn-update-password").click(function(){
	seajs.use(["request"], function(request){
		$(".main-iframe").attr("src", request.base + "/system/updatePassword");
	});
});

$("#btn-detail").click(function(){
	var id = $(this).data("id");
	seajs.use(["request"], function(request){
		$(".main-iframe").attr("src", request.base + "/user/mydetail");
	});
});
</script>
</html>
