<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>添加成功</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">添加成功</h3>
	    </div>
	    <div class="panel-body">
	    	<div class="col-sm-6 col-sm-offset-1">
	    		<p>登录地址：gm.starwingame.com</p>
	    		<p>账号：${user.id}</p>
	    		<p>密码：${user.password}</p>
	    		<p>请及时修改密码。</p>
	    	</div>
			<div class="my-btn-bar">
				<div class="col-sm-2 col-sm-offset-5">
					<button id="btn-back" type="button" class="btn btn-default">返&nbsp;&nbsp;回</button>
				</div>
			</div>
	    </div>
	</div>
	</div>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript">
$("#btn-back").click(function(){
	history.back();
});
</script>
</html>
