<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>提示</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">提示</h3>
	    </div>
	    <div class="panel-body">
	    	<div class="col-sm-6 col-sm-offset-1">
	    		<h3>${msg}</h3>
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
