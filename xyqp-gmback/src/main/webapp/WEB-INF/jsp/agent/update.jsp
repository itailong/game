<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>完善代理商资料</title>
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
	padding-left: 20px;
	line-height: 34px;
}
</style>
</head>
<body>
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">完善代理商资料</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="add-form" method="post">
	    		<input type="hidden" name="id" value="${agent.id}" />
	    		<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">手机</label>
					<input type="text" class="form-control" id="input-telephone" name="telephone" value="${agent.telephone}" maxlength="11" placeholder="请输入您的手机号码">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">真实姓名</label>
					<input type="text" class="form-control" name="realName" value="${agent.realName}" maxlength="10" placeholder="请输入您的真实姓名">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">地址</label>
					<input type="text" class="form-control" name="address" value="${agent.address}" maxlength="50" placeholder="请输入您的居住地址">
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
						<button id="btn-ok" type="submit" class="btn btn-danger">确&nbsp;&nbsp;定</button>
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

(function(){
	var telephone = $("#input-telephone").val();
	if (telephone) {
		$("#input-telephone").prop("disabled", "disabled");
	}
})();

$.validator.addMethod("telephone", function(value, element, param){
	if (!value){
		return false;
	}
	return this.optional(element) || /^1\d{10}$/.test(value);
});

$("#add-form").validate({
	rules: {
		telephone: {
			required: true,
			telephone: true
		},
		realName: {
			required: true,
			minlength: 2
		},
		address: {
			required: true,
			minlength: 10
		},
		verifyCode: {
			required: true,
			minlength: 4
		}
	},
	messages: {
		"telephone": {
			telephone: "请输入正确的电话号码！"
		}
	},
	errorPlacement: function(error, element) {
		element.parent().next(".my-error").append(error);
	},
	errorElement: "div"
});

$("#btn-back").click(function(){
	history.back();
});

$("#btn-verify").click(function(){
	var telephone = $("#input-telephone").val();
	if (!/^1\d{10}$/.test(telephone)) {
		seajs.use("dialog", function(dialog){
			dialog.showMessage("请输入正确的手机号码！");
		});
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
</script>
</html>
