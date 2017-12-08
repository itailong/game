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
<title>用户列表</title>
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
	        <h3 class="panel-title">用户列表</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="search-form">
				<div class="col-sm-3 my-from-group">
					<label class="control-label">编号</label>
					<input type="text" name="id" class="form-control" value="${fn:escapeXml(userQuery.id)}" placeholder="请输入编号">
				</div>
				<div class="col-sm-3 my-from-group">
					<label class="control-label">姓名</label>
					<input type="text" name="name" class="form-control" value="${fn:escapeXml(userQuery.name)}" placeholder="请输入名字">
				</div>
				<div class="col-sm-3 my-from-group">
					<label class="control-label">用户类型</label>
					<select id="js-userType" name="userType" class="form-control" data-value="${fn:escapeXml(userQuery.userType)}">
						<option value="">请选择</option>
						<option value="1">游客</option>
						<option value="2">微信用户</option>
						<option value="3">后台用户</option>
					</select>
				</div>
				<div class="col-sm-3">
					<button class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;&nbsp;查询</button>
				</div>
			</form>
			<div class="my-tool-bar">
				<sys:permission url="/user/add">
					<button id="btn-add" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span>&nbsp;&nbsp;&nbsp;添加</button>
				</sys:permission>
			</div>
	    </div>
		<table class="table table-bordered my-table">
			<tr>
				<th>序号</th>
				<th>用户编号</th>
				<th>姓名</th>
				<th>钻石</th>
				<th>金币</th>
				<th>创建时间</th>
				<th>用户类型</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${list}" var="element" varStatus="status" >
			<tr>
				<td>${status.index + 1 + pageInfo.start}</td>
				<td>${element.id}</td>
				<td>${fn:escapeXml(element.name)}</td>
				<td>${element.diamond}</td>
				<td>${element.gold}</td>
				<td><fmt:formatDate value="${element.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${element.userType == 1 ? '游客' : element.userType == 2 ? '微信用户' : element.userType == 3 ? '后台用户' : '未知'}</td>
				<td>
					<sys:permission url="/user/delete">
						<span class="js-delete my-action glyphicon glyphicon-trash" data-id="${element.id}" title="删除"></span>
					</sys:permission>
					<sys:permission url="/user/pay">
						<span class="js-pay my-action glyphicon glyphicon-usd" data-id="${element.id}" title="充值"></span>
					</sys:permission>
					<sys:permission url="/userRole/allot">
						<span class="js-allot my-action glyphicon glyphicon-cog" data-id="${element.id}" title="分配角色"></span>
					</sys:permission>
					<c:if test="${element.userType == 3}">
						<sys:permission url="/user/reset">
							<span class="js-reset my-action glyphicon glyphicon-repeat" data-id="${element.id}" title="重置密码"></span>
						</sys:permission>
					</c:if>
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

var val = $("#js-userType").data("value");
$("#js-userType").val(val);

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

$("#btn-add").click(function(){
	location.href = "add";
});

$(".js-pay").click(function(){
	var id = $(this).data("id");
	location.href = "pay?id=" + id;
});

$(".js-allot").click(function(){
	var id = $(this).data("id");
	location.href = "../userRole/allot?userId=" + id;
});

$(".js-reset").click(function(){
	var id = $(this).data("id");
	seajs.use(["dialog", "request"], function(dialog, request){
		dialog.showConfirm("你确定重置密码吗？", function(){
			request.ajax({
				url: "reset",
				data: {id: id},
				type: "post",
				success: function(data){
					dialog.showMessage(data);
				}
			});
		});
	});
});
</script>
</html>
