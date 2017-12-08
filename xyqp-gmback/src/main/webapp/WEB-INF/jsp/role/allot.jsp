<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  uri ="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>分配权限</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="col-sm-8 col-sm-offset-2">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">分配权限</h3>
	    </div>
	    <div class="panel-body">
			<form method="post">
				<input type="hidden" name="id" value="${fn:escapeXml(roleId)}"/>
				<c:forEach items="${multipleChoice.options}" var="element" varStatus="status">
				<div class="col-sm-2">
					<div class="checkbox">
					<label>
						<input type="checkbox" name="options[${status.index}].id" value="${element.id}" ${element.selected == true ? 'checked' : ''}/>${fn:escapeXml(element.name)}
					</label>
					</div>
				</div>
				</c:forEach>
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
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript">
$("#btn-back").click(function(){
	history.back();
});
</script>
</html>
