<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改密码</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">修改密码</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="add-form" method="post">
	    	<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">原密码</label>
					<input type="password" class="form-control" name="oldPassword" maxlength="16" placeholder="请输入原密码">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">密码</label>
					<input type="password" class="form-control" name="password" id="input-password" maxlength="16" placeholder="请输入密码">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">确认密码</label>
					<input type="password" class="form-control" name="passwords" maxlength="16" placeholder="请输入密码">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="my-btn-bar">
					<div class="col-sm-2 col-sm-offset-4">
						<button id="btn-ok" type="button" class="btn btn-danger">确&nbsp;&nbsp;定</button>
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

$("#add-form").validate({
	rules: {
		oldPassword: {
			required: true,
			minlength: 6
		},
		password: {
			required: true,
			minlength: 6
		},
		passwords: {
			required: true,
			equalTo: "#input-password"
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

$("#btn-ok").click(function(){
	if ($("#add-form").valid()) {
		seajs.use(["request", "dialog"], function(request, dialog){
			request.ajax({
				type: "post",
				data: $("#add-form").serialize(),
				success: function(data){
					dialog.showMessage(data, function(){
						top.location.href = request.base + "/system/index"
					});
				}
			});
		});
	}
});
</script>
</html>
