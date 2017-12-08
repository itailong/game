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
</head>
<body>
<div class="my-content">
	<input type="hidden" id="input-self-diamond" value="${user.diamond}"/>
	<div class="col-sm-8 col-sm-offset-2">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">充值</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="add-form" method="post">
	    		<input type="hidden" name="id" value="${userId}" />
	    		<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">星钻余额</label>
					<span class="my-value">${user.diamond}</span>
				</div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">星钻数量</label>
					<input type="text" class="form-control" name="diamond" maxlength="10" placeholder="请输入星钻数量">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="my-btn-bar">
					<div class="col-sm-2 col-sm-offset-4">
						<button type="button" id="btn-add" class="btn btn-danger">确&nbsp;&nbsp;定</button>
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

$.validator.addMethod("balance", function(value, element, param){
	if (!value){
		return false;
	}
	var input = $("#input-self-diamond");
	var balance = parseInt(input.val());
	var flag = balance >= parseInt(value);
	return this.optional(element) || flag;
});

$("#add-form").validate({
	rules: {
		diamond: {
			digits: true,
			required: true,
			balance: true
		}
	},
	messages: {
		diamond:  {
			balance: "余额不足！"
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

$("#btn-add").click(function(){
	if($("#add-form").valid()) {
		seajs.use(["request", "dialog"], function(request, dialog){
			request.ajax({
				url: "pay",
				type: "post",
				data: $("#add-form").serialize(),
				success: function(data) {
					dialog.showMessage(data, function(){
						location.href = "list";
					});
				}
			});
		});
	}
});
</script>
</html>
