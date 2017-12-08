<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  uri ="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="page" uri="http://www.starland.com/tools/page" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>人员列表</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">钻石消耗记录</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="search-form">
				<div class="col-sm-4 my-from-group">
					<label class="control-label">开始时间</label>
					<input type="text" name="startTime" class="form-control" value='<fmt:formatDate value="${diamondConsumeQuery.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss'})" placeholder="开始时间" >
				</div>
				<div class="col-sm-4 my-from-group">
					<label class="control-label">结束时间</label>
					<input type="text" name="endTime" class="form-control" value='<fmt:formatDate value="${diamondConsumeQuery.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss'})" placeholder="结束时间" >
				</div>
				<div class="col-sm-4 my-from-group">
					<label class="control-label">用户编号</label>
					<input type="number" name="userId" class="form-control" value="${fn:escapeXml(diamondConsumeQuery.userId)}" placeholder="请输入用户编号">
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
				<th>消耗钻石</th>
				<th>时间</th>
				<th>游戏名称</th>
				<th>局数</th>
			</tr>
			<c:forEach items="${list}" var="element" varStatus="status" >
			<tr>
				<td>${status.index + 1 + pageInfo.start}</td>
				<td>${element.userId}</td>
				<td>${fn:escapeXml(element.consume)}</td>
				<td><fmt:formatDate value="${element.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${element.gameName}</td>
				<td>${element.roundCount}</td>
			</tr>
			</c:forEach>
		</table>
	</div>
	<page:page form="#search-form"/>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript" src="${cp}/libs/My97DatePicker/WdatePicker.js"></script>
</html>
