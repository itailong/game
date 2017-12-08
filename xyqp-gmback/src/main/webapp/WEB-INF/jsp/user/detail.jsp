<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  uri ="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>转账</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
<style type="text/css">
#ewm-small {
	margin-left: 30px;
}
#ewm-download {
	position: absolute;
	left: 300px;
	top: 40px;
}
#ewm-big {
	position: absolute;
	left: 50%;
	top: 10px;
	margin-left: -150px;
	display: none;
}
</style>
</head>
<body>
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
		<div class="panel panel-default">
		    <div class="panel-heading">
		        <h3 class="panel-title">用户信息</h3>
		    </div>
		    <div class="panel-body">
		    	<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">账号</label>
					<span class="my-value">${user.id}</span>
				</div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">星钻数量</label>
					<span class="my-value">${user.diamond}</span>
				</div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">金币</label>
					<span class="my-value">${user.gold}</span>
				</div>
		    </div>
		</div>
		
		<c:if test="${agent != null}">
		<div class="panel panel-default">
		    <div class="panel-heading">
		        <h3 class="panel-title">代理商信息</h3>
		    </div>
		    <input type="hidden" id="agent-id" value="${agent.id}" />
		    <div class="panel-body">
		    	<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">邀请码</label>
					<span class="my-value">${agent.id}</span>
				</div>
				<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">推广链接</label>
					<span class="my-value">http://weixin.starwingame.com/user/bind?id=${agent.id}</span>
				</div>
				<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">推广二维码</label>
					<canvas id="ewm-small"></canvas>
					<a id="ewm-download">下载</a>
				</div>
				<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">手机</label>
					<span class="my-value">${agent.telephone}</span>
				</div>
				<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">真实姓名</label>
					<span class="my-value">${agent.realName}</span>
				</div>
				<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">账号余额</label>
					<span class="my-value">${agent.money}</span>
				</div>
				<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">总收益</label>
					<span class="my-value">${agent.totalIncome}</span>
				</div>
				<div class="col-sm-7 col-sm-offset-3 my-from-group">
					<label class="control-label">联系地址</label>
					<span class="my-value">${agent.address}</span>
				</div>
				<div class="my-btn-bar">
					<div class="col-sm-2 col-sm-offset-5">
						<button id="btn-update" type="button" class="btn btn-danger">修&nbsp;&nbsp;改</button>
					</div>
				</div>
		    </div>
		</div>
		</c:if>
	</div>
</div>
<canvas id="ewm-big"></canvas>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript" src="${cp}/libs/qrious/qrious.js" ></script>
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript">
seajs.config({
	alias: {
		"dialog": seajs.data.base + "dialog/js/dialog",
		"request": seajs.data.base + "request/js/request"
	}
});

(function(){
	var id = $("#agent-id").val();
	new QRious({
		element: document.getElementById('ewm-small'),
		value: "http://weixin.starwingame.com/user/bind?id=" + id,
		size: 100,
		level: "Q",
	});
	var ewmBig = new QRious({
		element: document.getElementById('ewm-big'),
		value: "http://weixin.starwingame.com/user/bind?id=" + id,
		size: 300,
		level: "Q",
	});
	$("#ewm-download").attr("href", ewmBig.toDataURL());
	$("#ewm-download").attr("download", "推广二维码.png");
})();

$("#ewm-small").mouseenter(function(){
	$("#ewm-big").show();
});

$("#ewm-small").mouseleave(function(){
	$("#ewm-big").hide();
});

$("#btn-back").click(function(){
	history.back();
});
$("#btn-update").click(function(){
	var id = $("#agent-id").val();
	location.href = "../agent/update?id=" + id;
});
</script>
</html>
