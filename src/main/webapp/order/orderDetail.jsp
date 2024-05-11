<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单详情</title>
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
</head>
<body>
	<%@include file="../header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<c:if test="${!empty orderDetail}">
				<div class="col-md-10 col-md-offset-1">
					<h3>订单信息</h3>
					<table class="table table-condensed table-hover">
						<tbody>
							<tr>
								<td>订单编号：${fn:escapeXml(orderDetail.orderCode)}</td>
								<td>创建时间：<fmt:formatDate value="${orderDetail.orderDate}"
										pattern="yyyy-MM-dd" /></td>
							</tr>
							<tr>
								<td>订单状态：<span id="orderStatus"> <c:if
											test="${orderDetail.orderStatus==1}">
								等待付款						
							</c:if> <c:if test="${orderDetail.orderStatus==3}">
								订单取消，交易关闭							
							</c:if> <c:if test="${orderDetail.orderStatus==2}">
								交易成功
							</c:if>
								</span>
								</td>
								<td>送货地址：${fn:escapeXml(orderDetail.orderAddress)}</td>
							</tr>
						</tbody>
					</table>

				</div>
				<div class="col-md-10 col-md-offset-1">
					<div class="panel panel-info">
						<div class="panel-heading">商品列表</div>
						<div class="panel-body">
							<table class="table table-hover table-condensed">
								<thead>
									<tr>
										<th>#</th>
										<th>商品图片</th>
										<th>商品名称</th>
										<th>商品单价</th>
										<th>数量</th>
										<th>小计</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="totalAmount" value="0" />
									<c:set var="postalfee" value="${paramValues.goodsPostalfee[0]}" />
									<c:forEach items="${orderDetail.odetails}" var="o"
										varStatus="vs">

										<tr ${vs.count%2==0?'class=\"info\"':''}>
											<td>${vs.count}</td>
											<td><img
												src="${pageContext.request.contextPath}${fn:escapeXml(o.odetailPic)}"
												width="30" height="30"></td>
											<td>${fn:escapeXml(o.odetailName)}&nbsp;&nbsp;</td>
											<td>单价<span class="glyphicon glyphicon-yen"
												aria-hidden="true"></span>${o.odetailPrice}
											</td>
											<td>${o.odetailNum}</td>
											<td><span class="glyphicon glyphicon-yen"
												aria-hidden="true"></span> ${o.odetailNum*o.odetailPrice}</td>
											<c:set var="totalAmount"
												value="${totalAmount+o.odetailNum*o.odetailPrice}" />
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
					<table class="table table-condensed">
						<tr>
							<td class="text-right">运费：<span
								class="glyphicon glyphicon-yen" aria-hidden="true"></span>${orderDetail.orderPostalfee}</td>
						</tr>
						<tr>
							<td class="text-right">订单总金额（含运费）：<span
								class="glyphicon glyphicon-yen" aria-hidden="true"></span>${totalAmount+orderDetail.orderPostalfee}</td>
						</tr>
					</table>
					<div class="col-md-12 text-right">
						<c:if test="${orderDetail.orderStatus==1}">
							<button class="btn btn-primary" type="button"
								onclick="cancelOrder('${pageContext.request.contextPath}','${orderDetail.orderId}','确定要取消这个订单吗？');">取消订单</button>
							<button class="btn btn-primary" type="button"
								onclick="javascript:location.href='${pageContext.request.contextPath}/order/payOrders.jsp?orderId=${orderDetail.orderId}'">去付款</button>
						</c:if>
						<button class="btn btn-primary" id="back" type="button"
							onclick="javascript:location.href='${pageContext.request.contextPath}/order/getMyOrders.action'">返回订单列表</button>
					</div>
				</div>
			</c:if>
		</div>
	</div>
	<%@include file="../footer.jsp"%>
</body>
</html>