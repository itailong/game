<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  uri ="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="page" uri="http://www.starland.com/tools/page" %>
<%@ taglib uri="http://www.starland.com/system" prefix="sys" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>人员列表</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
<style type="text/css">
.open-image {
	height: 40px;
}
</style>
</head>
<body>
<div class="my-content">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">人员列表</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="search-form">
				<div class="col-sm-4 my-from-group">
					<label class="control-label">编号</label>
					<input type="text" name="id" class="form-control" value="${fn:escapeXml(userQuery.id)}" placeholder="请输入编号">
				</div>
				<div class="col-sm-4 my-from-group">
					<label class="control-label">姓名</label>
					<input type="text" name="name" class="form-control" value="${fn:escapeXml(userQuery.name)}" placeholder="请输入名字">
				</div>
				<div class="col-sm-4">
					<button class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;&nbsp;查询</button>
				</div>
			</form>
	    </div>
		<table class="table table-bordered my-table">
			<tr>
				<th>序号</th>
				<th>用户编号</th>
				<th>姓名</th>
				<th>钻石</th>
				<th>金币</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${list}" var="element" varStatus="status" >
			<tr>
				<td>${status.index + 1 + pageInfo.start}</td>
				<td>${element.id}</td>
				<td>${fn:escapeXml(element.name)}</td>
				<td>${element.diamond}</td>
				<td>${element.gold}</td>
				<td>
					<sys:permission url="/user/give">
						<span class="js-give my-action glyphicon glyphicon-heart-empty" data-id="${element.id}" title="赠送钻石"></span>
					</sys:permission>
				</td>
			</tr>
			</c:forEach>
		</table>
	</div>
	<page:page form="#search-form"/>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript">
$(".js-give").click(function(){
	var id = $(this).data("id");
	location.href = "give?id=" + id;
});
</script>
</html>
