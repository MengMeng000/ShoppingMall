<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>支付订单</title>
</head>
<body>
	<%@include file="../header.jsp"%>
	<%@include file="userorderLeftNav.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-4"></div>
					<div class="col-md-8">
						<div class="cart py-container">

							<!--主内容-->
							<div style="width: 100%; height: 100%;">
								<div style="margin: 10px 0 160px 60px">
									<img src="../images/goods/pay1.png" width="350" height="350"">
									<h3>恭喜您，提交订单成功！</h3>
									<p style="margin: 40px 10px 0 0">● 支付方式：货到付款</p>
									<p style="margin: 40px 10px 0 0">● 支付金额：￥86.00元</p>
									<p>
										<c:if test="${!empty orderId}">
											<a
												href="${pageContext.request.contextPath}/order/payOrder.action?orderId=${orderId}">支付成功</a>
										</c:if>
										<c:if test="${!empty param.orderId}">
											<a
												href="${pageContext.request.contextPath}/order/payOrder.action?orderId=${param.orderId}">支付成功</a>
										</c:if>
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../footer.jsp"%>
</body>
</html>