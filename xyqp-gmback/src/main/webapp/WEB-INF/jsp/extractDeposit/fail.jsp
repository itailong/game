<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  uri ="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>提现失败</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<input type="hidden" id="telephone" value="${agent.telephone}" />
<input type="hidden" id="agentId" value="${agent.id}" />
<input type="hidden" id="agentMoney" value="${agent.money}" />
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
		<div class="panel panel-default">
		    <div class="panel-heading">
		        <h3 class="panel-title">提现失败</h3>
		    </div>
		    <div class="panel-body">
		    	<form id="add-form" method="post">
		    		<input type="hidden" name="id" value="${id}" />
					<div class="col-sm-5 col-sm-offset-3 my-from-group">
						<label class="control-label">失败原因</label>
						<input type="text" class="form-control" name="remark" maxlength="20" placeholder="请输入失败原因">
						<span class="my-required"></span>
					</div>
					<div class="col-sm-4 my-error"></div>
					<div class="my-btn-bar">
						<div class="col-sm-2 col-sm-offset-4">
							<button class="btn btn-danger">确&nbsp;&nbsp;定</button>
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

$("#add-form").validate({
	rules: {
		remark: {
			required: true
		},
	},
	errorPlacement: function(error, element) {
		element.parent().next(".my-error").append(error);
	},
	errorElement: "div"
});
</script>
</html>
