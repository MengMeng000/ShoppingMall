<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单列表</title>
</head>
<script>
	function cancelOrder(baseurl, orderId, msg) {
		if (confirm(msg)) {
			$.post(baseurl + "/order/cancelOrder.action", {
				orderId : orderId
			}, function(result) {
				if (result.success) {
					window.location.reload();
				} else {
					alert("订单取消失败");
				}
			}, 'json');
		}
	}
</script>
<body>

<%@include file="../header.jsp" %>

<div class="container-fluid">
	<div class="row">
	<div class="col-md-2">		
		<%@include file="userorderLeftNav.jsp" %>
	</div>
	
	<c:if test="${!empty orders}">
		
		<div class="col-md-10 ">
				<c:forEach items="${orders}" var="o" varStatus="vs">
				<div class="panel panel-info">
				<div class="panel-heading ">
					下单日期：<fmt:formatDate value="${o.orderDate}" pattern="yyyy-MM-dd"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 订单号：<a href="${pageContext.request.contextPath}/order/getOrderDetailById.action?orderId=${o.orderId}">${fn:escapeXml(o.orderCode)}</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					订单状态：    <span id="orderStatus">
							<c:if test="${o.orderStatus==1}">
								等待付款
								<div class="text-right">
								    <a href='#' onclick="cancelOrder('${pageContext.request.contextPath}','${o.orderId}','确定要取消这个订单吗？');">取消订单</a>
									<a href="${pageContext.request.contextPath}/order/payOrders.jsp?orderId=${o.orderId}">去付款</a>
								</div>
							</c:if>
							<c:if test="${o.orderStatus==3}">
								订单取消，交易关闭
								<div class="text-right">
									<a href="${pageContext.request.contextPath}/order/delOrder.action?orderId=${o.orderId}" onclick="return confirm('确定要删除这个订单吗？')">删除订单</a> 
								</div>
							</c:if>
							<c:if test="${o.orderStatus==2}">
								交易成功
								<div class="text-right">
									<a href="${pageContext.request.contextPath}/order/delOrder.action?orderId=${o.orderId}" onclick="return confirm('确定要删除这个订单吗？')">删除订单</a> 
								</div>
							</c:if>
						</span>
				</div>
				<div class="panel-body">		
				<table class="table table-hover table-condensed table-bordered">	
					<tbody>	
					<c:set var="orderDetails" value="${o.odetails }"/>
					<c:set var="totalAmount" value="0"/>
					<c:forEach items="${orderDetails}" var="od" varStatus="odvs">
					<tr>
						<td>
							<img src="${pageContext.request.contextPath}${fn:escapeXml(od.odetailPic)}" width="30" height="30">
						</td>
						<td>
							<a href="${pageContext.request.contextPath}/goods/goodsDetail.action?goods_id=${od.goodsId}">${fn:escapeXml(od.odetailName)}</a>&nbsp;&nbsp;
						</td>
						<td>
							<span class="glyphicon glyphicon-yen" aria-hidden="true"></span>${od.odetailPrice}				
						</td>
						<td>
							${od.odetailNum}
						</td>
						<td>
							<span class="glyphicon glyphicon-yen" aria-hidden="true"></span>${od.odetailPrice*od.odetailNum }
							<c:set var="totalAmount" value="${totalAmount+od.odetailPrice*od.odetailNum}"/>
						</td>
					</tr>
					</c:forEach>
					
					</tbody>
				</table>
				<table class="table table-condensed">
					<tr>
						<td>
						<button class="btn btn-info" id="back" type="button" onclick="javascript:location.href='${pageContext.request.contextPath}/order/getOrderDetailById.action?orderId=${o.orderId}'">查看订单详情</button>
						</td>
						<td class="text-right">总金额（含运费${o.orderPostalfee}元）：<span class="glyphicon glyphicon-yen" aria-hidden="true"></span>${totalAmount+o.orderPostalfee }</td>
					</tr>
				</table>
				</div>
				</div>
				</c:forEach>
				
		</div>
	</c:if>
	<c:if test="${empty orders}">
		<div class="alert alert-danger col-md-2 col-md-offset-1" role="alert">对不起，暂无此类订单信息</div>
	</c:if>
	</div>
</div>
<%@include file="../footer.jsp" %>
</body>
</html>