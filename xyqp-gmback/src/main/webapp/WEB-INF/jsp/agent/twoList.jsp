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
<title>代理商列表</title>
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
	        <h3 class="panel-title">代理商列表</h3>
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
			<div class="my-tool-bar">
				<sys:permission url="/agent/addDown">
					<button id="btn-add" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;&nbsp;添加</button>
				</sys:permission>
			</div>
	    </div>
		<table class="table table-bordered my-table">
			<tr>
				<th>序号</th>
				<th>代理商编号</th>
				<th>真实姓名</th>
				<th>电话</th>
				<th>总收益</th>
				<th>创建时间</th>
			</tr>
			<c:forEach items="${list}" var="element" varStatus="status" >
			<tr>
				<td>${status.index + 1 + pageInfo.start}</td>
				<td>${element.id}</td>
				<td>${fn:escapeXml(element.realName)}</td>
				<td>${element.telephone}</td>
				<td>${element.totalIncome}</td>
				<td><fmt:formatDate value="${element.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
			</tr>
			</c:forEach>
		</table>
	</div>
	<page:page form="#search-form"/>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript">
$("#btn-add").click(function(){
	location.href = "addDown";
});
</script>
</html>
