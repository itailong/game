<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>添加模块</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">添加模块</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="add-form" method="post">
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">名称</label>
					<input type="text" class="form-control" name="name" maxlength="10" placeholder="请输入名字">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="col-sm-5 col-sm-offset-3 my-from-group">
					<label class="control-label">路径</label>
					<input type="text" class="form-control" name="url" maxlength="100" placeholder="请输入路径">
					<span class="my-required"></span>
				</div>
				<div class="col-sm-4 my-error"></div>
				<div class="my-btn-bar">
					<div class="col-sm-2 col-sm-offset-4">
						<button class="btn btn-danger">添&nbsp;&nbsp;加</button>
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
$("#add-form").validate({
	rules: {
		name: {
			required: true,
			minlength: 2
		},
		url: {
			required: true
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
</script>
</html>
