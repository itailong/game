<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  uri ="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改提现信息</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
<style type="text/css">
#verifyCode {
	width: 100px;
}
#btn-verify {
	margin-top: 4px;
	margin-left: 20px;
}
#span-verify {
	display: none;
}
</style>
</head>
<body>
<input type="hidden" id="telephone" value="${agent.telephone}" />
<input type="hidden" id="agentId" value="${agent.id}" />
<input type="hidden" id="agentMoney" value="${agent.money}" />
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
		<div class="panel panel-default">
		    <div class="panel-heading">
		        <h3 class="panel-title">修改提现信息</h3>
		    </div>
		    <div class="panel-body">
		    	<form id="add-form" method="post">
					<div class="col-sm-5 col-sm-offset-3 my-from-group">
						<label class="control-label">开户银行</label>
						<select id="select-bankName" name="bankName" class="form-control" data-bankName="${extractDeposit.bankName}">
							<option value="">请选择银行</option>
							<option value="招商银行">招商银行(推荐)</option>
							<option value="农业银行">农业银行</option>
							<option value="中国银行">中国银行</option>
							<option value="工商银行">工商银行</option>
							<option value="建设银行">建设银行</option>
							<option value="交通银行">交通银行</option>
							<option value="光大银行">光大银行</option>
							<option value="兴业银行">兴业银行</option>
							<option value="民生银行">民生银行</option>
							<option value="中信银行">中信银行</option>
							<option value="平安银行">平安银行</option>
						</select>
						<span class="my-required"></span>
					</div>
					<div class="col-sm-4 my-error"></div>
					<div class="col-sm-5 col-sm-offset-3 my-from-group">
						<label class="control-label">户名</label>
						<input type="text" class="form-control" name="receiverName" value="${extractDeposit.receiverName}" maxlength="10" placeholder="请输入收款人姓名">
						<span class="my-required"></span>
					</div>
					<div class="col-sm-4 my-error"></div>
					<div class="col-sm-5 col-sm-offset-3 my-from-group">
						<label class="control-label">账号</label>
						<input type="text" class="form-control" name="bankAccount" value="${extractDeposit.bankAccount}" maxlength="30" placeholder="请输入收款人账号">
						<span class="my-required"></span>
					</div>
					<div class="col-sm-4 my-error"></div>
					<div class="col-sm-5 col-sm-offset-3 my-from-group">
						<label class="control-label">验证码</label>
						<input type="text" class="form-control" id="verifyCode" name="verifyCode" maxlength="6" placeholder="验证码">
						<button id="btn-verify" type="button">获取验证码</button>
						<span id="span-verify">60s</span>
						<span class="my-required"></span>
					</div>
					<div class="col-sm-4 my-error"></div>
							<div class="my-btn-bar">
						<div class="col-sm-2 col-sm-offset-4">
							<button id="btn-add" class="btn btn-danger">确&nbsp;&nbsp;定</button>
						</div>
						<div class="col-sm-2">
							<button id="btn-back" type="button" class="btn btn-default">返&nbsp;&nbsp;回</button>
						</div>
					</div>
				</form>
		    </div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.validate.js" ></script>
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript">
seajs.config({
	alias: {
		"dialog": seajs.data.base + "dialog/js/dialog",
		"request": seajs.data.base + "request/js/request"
	}
});

$("#btn-back").click(function(){
	history.back();
});

(function(){
	var bankName = $("#select-bankName").data("bankname");
	$("#select-bankName").val(bankName);
})();

$("#btn-verify").click(function(){
	var telephone = $("#telephone").val();
	if (!/^1\d{10}$/.test(telephone)) {
		return;
	}
	sendverivyCode(telephone);
	$(this).hide();
	updateVersif(60);
	$("#span-verify").show();
});

function updateVersif(num) {
	$("#span-verify").html(num + "s");
	if (num < 0) {
		$("#btn-verify").show();
		$("#span-verify").hide();
		return;
	}
	setTimeout(function(){
		updateVersif(num - 1);
	}, 1000);
}

function sendverivyCode(telephone) {
	var telephone = $("#telephone").val();
	seajs.use(["request", "dialog"], function(request, dialog){
		request.ajax({
			url: request.base + "/system/obtainVerifyCode",
			method: "POST",
			data: {
				telephone: telephone
			},
			success: function(msg) {
				dialog.showMessage(msg);
			}
		});
	});
}

$("#add-form").validate({
	rules: {
		bankName: {
			required: true
		},
		receiverName: {
			required: true,
			minlength: 2
		},
		bankAccount: {
			required: true,
			minlength: 10
		},
		verifyCode: {
			required: true,
			minlength: 4
		}
	},
	errorPlacement: function(error, element) {
		element.parent().next(".my-error").append(error);
	},
	errorElement: "div"
});
</script>
</html>
