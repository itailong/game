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
<title>模块列表</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">模块列表</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="search-form">
				<div class="col-sm-4 my-from-group">
					<label class="control-label">编号</label>
					<input type="text" name="id" class="form-control" value="${fn:escapeXml(moduleGroupQuery.id)}" placeholder="请输入编号">
				</div>
				<div class="col-sm-4 my-from-group">
					<label class="control-label">名称</label>
					<input type="text" name="name" class="form-control" value="${fn:escapeXml(moduleGroupQuery.name)}" placeholder="请输入名称">
				</div>
				<div class="col-sm-4">
					<button class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;&nbsp;查询</button>
				</div>
			</form>
			<div class="my-tool-bar">
				<sys:permission url="/module/add">
					<button id="btn-add" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;&nbsp;添加</button>
				</sys:permission>
			</div>
	    </div>
		<table class="table table-bordered my-table">
			<tr>
				<th>序号</th>
				<th>编号</th>
				<th>名称</th>
				<th>路径</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${list}" var="element" varStatus="status" >
			<tr>
				<td>${status.index + 1 + pageInfo.start}</td>
				<td>${element.id}</td>
				<td>${element.name}</td>
				<td>${element.url}</td>
				<td>
					<sys:permission url="/module/delete">
						<span class="js-delete my-action glyphicon glyphicon-trash" data-id="${element.id}" title="删除"></span>
					</sys:permission>
					<sys:permission url="/module/update">
						<span class="js-update my-action glyphicon glyphicon-edit" data-id="${element.id}" title="修改"></span>
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
<script type="text/javascript" src="${cp}/sea/sea.js" ></script>
<script type="text/javascript">
seajs.config({
	alias: {
		"dialog": seajs.data.base + "dialog/js/dialog",
		"request": seajs.data.base + "request/js/request"
	}
});

$(".js-delete").click(function(){
	var id = $(this).data("id");
	seajs.use(["dialog", "request"], function(dialog, request){
		dialog.showConfirm("你确定删除吗？", function(){
			request.ajax({
				url: "delete",
				data: {id: id},
				type: "post",
				success: function(data){
					dialog.showMessage(data, function(){
						location.reload(false);
					});
				}
			});
		});
	});
});

$(".js-update").click(function(){
	var id = $(this).data("id");
	location.href = "update?id=" + id;
});

$("#btn-add").click(function(){
	location.href = "add";
});
</script>
</html>
