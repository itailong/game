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
<title>提现记录</title>
<link rel="stylesheet" href="${cp}/libs/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" href="${cp}/css/common/common.css" />
</head>
<body>
<div class="my-content">
	<div class="panel panel-default">
	    <div class="panel-heading">
	        <h3 class="panel-title">提现记录</h3>
	    </div>
	    <div class="panel-body">
	    	<form id="search-form">
	    		<div class="col-sm-3 my-from-group">
					<label class="control-label">订单编号</label>
					<input type="text" name="id" class="form-control" value="${extractDepositQuery.id}" placeholder="订单编号" >
				</div>
				<div class="col-sm-3 my-from-group">
					<label class="control-label">收款人</label>
					<input type="text" name="receiverName" class="form-control" value="${extractDepositQuery.receiverName}" placeholder="收款人" >
				</div>
				<div class="col-sm-3 my-from-group">
					<label class="control-label">银行卡号</label>
					<input type="text" name="bankAccount" class="form-control" value="${extractDepositQuery.bankAccount}" placeholder="银行卡号" >
				</div>
				<div class="col-sm-3 my-from-group">
					<label class="control-label">状态</label>
					<select id="select-status" name="status" class="form-control" data-status="${extractDepositQuery.status}">
						<option value="">请选择</option>
						<option value="1">提现中</option>
						<option value="2">提现成功</option>
						<option value="3">提现失败</option>
					</select>
				</div>
				<div class="col-sm-3 my-from-group">
					<label class="control-label">开始时间</label>
					<input type="text" name="startTime" class="form-control" value='<fmt:formatDate value="${extractDepositQuery.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss'})" placeholder="开始时间" >
				</div>
				<div class="col-sm-3 my-from-group">
					<label class="control-label">结束时间</label>
					<input type="text" name="endTime" class="form-control" value='<fmt:formatDate value="${extractDepositQuery.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' onClick="WdatePicker({el:this,dateFmt:'yyyy-MM-dd HH:mm:ss'})" placeholder="结束时间" >
				</div>
				<div class="col-sm-3">
					<button class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;&nbsp;&nbsp;查询</button>
				</div>
			</form>
	    </div>
		<table class="table table-bordered my-table">
			<tr>
				<th>序号</th>
				<th>订单编号</th>
				<th>收款人姓名</th>
				<th>银行卡号</th>
				<th>开户银行</th>
				<th>提现金额</th>
				<th>时间</th>
				<th>备注</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${list}" var="element" varStatus="status" >
			<tr>
				<td>${status.index + 1 + pageInfo.start}</td>
				<td>${element.id}</td>
				<td>${element.receiverName}</td>
				<td>${fn:escapeXml(element.bankAccount)}</td>
				<td>${element.bankName}</td>
				<td>${element.money}元</td>
				<td><fmt:formatDate value="${element.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${element.remark}</td>
				<td>${element.status == 1 ? '提现中' : element.status == 2 ? '提现成功' : element.status == 3 ? '提现失败' : '未知'}</td>
				<td>
					<c:if test="${element.status == 1}">
						<sys:permission url="/extractDeposit/success">
							<span class="js-ok my-action glyphicon glyphicon-ok" data-id="${element.id}" title="提现成功"></span>
						</sys:permission>
						<sys:permission url="/extractDeposit/fail">
							<span class="js-fail my-action glyphicon glyphicon-remove" data-id="${element.id}" title="提现失败"></span>
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
<script type="text/javascript" src="${cp}/libs/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
seajs.config({
	alias: {
		"dialog": seajs.data.base + "dialog/js/dialog",
		"request": seajs.data.base + "request/js/request"
	}
});
var status = $("#select-status").data("status");
$("#select-status").val(status);
$(".js-ok").click(function(){
	var id = $(this).data("id");
	seajs.use(["dialog", "request"], function(dialog, request){
		dialog.showConfirm("你确定提现成功吗？", function(){
			request.ajax({
				url: request.base + "/extractDeposit/success",
				type: "post",
				data: {
					id: id
				},
				success: function(msg) {
					dialog.showMessage(msg, function(){
						location.reload(false);
					});
				}
			});
		});
	});
});
$(".js-fail").click(function(){
	var id = $(this).data("id");
	location.href = "fail?id=" + id;
});
</script>
</html>
