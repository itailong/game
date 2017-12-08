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
	        <h3 class="panel-title">钻石返利</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="search-form">
				<div class="col-sm-4 my-from-group">
					<label class="control-label">开始时间</label>
					<select id="js-gameType" name="gameType" class="form-control" data-value="${gameRoomQuery.gameType}">
						<option value="">请选择</option>
						<option value="yjmj">沅江麻将</option>
						<option value="ddz">斗地主</option>
						<option value="pdk">跑得快</option>
					</select>
				</div>
				<div class="col-sm-4">
					<button class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;&nbsp;查询</button>
				</div>
			</form>
	    </div>
		<table class="table table-bordered my-table">
			<tr>
				<th>序号</th>
				<th>房间编号</th>
				<th>低分</th>
				<th>局数</th>
				<th>人数</th>
				<th>服务器编号</th>
				<th>游戏名称</th>
				<th>创建时间</th>
				<th>创建者编号</th>
				<th>是否替人开房</th>
			</tr>
			<c:forEach items="${list}" var="element" varStatus="status" >
			<tr>
				<td>${status.index + 1 + pageInfo.start}</td>
				<td>${element.id}</td>
				<td>${element.score}</td>
				<td>${element.roundCount}</td>
				<td>${element.currentPerson}/${element.maxPerson}</td>
				<td>${element.serverId}</td>
				<td>${element.gameName}</td>
				<td><fmt:formatDate value="${element.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${element.creatorId}</td>
				<td>${element.instead == 1 ? '是' : '否'}</td>
			</tr>
			</c:forEach>
		</table>
	</div>
	<page:page form="#search-form"/>
</div>
</body>
<script type="text/javascript" src="${cp}/libs/jquery/js/jquery.js" ></script>
<script type="text/javascript">
	var val = $("#js-gameType").data("value");
	$("#js-gameType").val(val);
</script>
</html>
