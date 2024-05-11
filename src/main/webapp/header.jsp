<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>随意购商城</title>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/bootstrap/css/default.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script
	src="${pageContext.request.contextPath}/css/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-12 ">
				<ol class="breadcrumb text-right" id="info">
					<c:if test="${sessionScope['_LOGIN_USER_']==null}">
						<li id="li1"><span>游客您好，欢迎来到随意购商城！</span> <a
							href="#loginFormModal" data-toggle="modal">[登录]</a>&nbsp;<a
							href="#regFormModal" data-toggle="modal">[新用户注册]</a></li>
						<li><a href="#" onclick="alert('请先登录')">购物车 <span
								class="badge" id="cartBadge">${fn:length(sessionScope.goodslist)}</span></a></li>
					</c:if>
					<c:if test="${sessionScope['_LOGIN_USER_']!=null}">
						<li id="li1"><span>${sessionScope['_LOGIN_USER_'].userName }
								您好，欢迎来到随意购商城！</span></li>
						<li><a href="#"
							onclick="showCart('${pageContext.request.contextPath}','${goodslist}')">购物车
								<span class="badge" id="cartBadge">${fn:length(sessionScope.goodslist)}</span>
						</a></li>
						<li><a
							href="${pageContext.request.contextPath}/order/getMyOrders.action">我的订单</a></li>
						<li><a
							href='${pageContext.request.contextPath}/usercenter/index.action'>个人中心</a></li>
						<li><a href="#"
							onclick="logout('${pageContext.request.contextPath}')">退出登录</a></li>
					</c:if>
				</ol>
				<%@include file="common/loginFormModal.jsp"%>
				<%@include file="common/regFormModal.jsp"%>



				<nav class="navbar navbar-default" role="navigation">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse"
							data-target="#bs-example-navbar-collapse-1">
							<span class="sr-only">Toggle navigation</span><span
								class="icon-bar"></span><span class="icon-bar"></span><span
								class="icon-bar"></span>
						</button>
						<a class="navbar-brand"
							href="${pageContext.request.contextPath}/index.action"><span
							class="logo">随意购</span> 商城</a>
					</div>

					<div class="collapse navbar-collapse"
						id="bs-example-navbar-collapse-1">
						<ul class="nav navbar-nav">
							<li class="active" id="first_page"><a
								href="${pageContext.request.contextPath}/index.action">首页</a></li>
							<li id="sale_goods"><a
								href="${pageContext.request.contextPath}/goods/saleGoods.action">热销商品</a></li>
							<li id="new_goods"><a
								href="${pageContext.request.contextPath}/goods/newGoods.action">新到商品</a></li>
							<li class="dropdown" id="goods_list"><a href="#"
								class="dropdown-toggle" data-toggle="dropdown">商品分类<strong
									class="caret"></strong></a>
								<ul class="dropdown-menu">
									<c:forEach items="${catelist}" var="c" varStatus="vs">
										<c:forEach items="${c.childlist}" var="cl" varStatus="i">
											<li><a
												href="${pageContext.request.contextPath}/goods/goodsCate.action?childid=${cl.childid}">${cl.childname}</a>
											</li>
										</c:forEach>
										<li class="divider"></li>
									</c:forEach>
								</ul></li>
						</ul>
					</div>
				</nav>
</body>
<script type="text/javascript">
	function delCart(index, baseurl) {
		if (confirm('确定要删除这个商品吗？')) {
			$.post(baseurl + "/goods/deleteCart.action", {
				index : index
			}, function(result) {
				if (result.success) {
					$('#cartModal').modal('hide');
					$("#msgTitle").html("删除成功");
					$("#msgBody").html("已将购物车中该商品删除");
					$("#msgModal").modal();
				}
			}, 'json');
			$("#cartBadge").html(parseInt($("#cartBadge").html()) - 1);
		}
	}
	function clearCart(baseurl) {
		$.post(baseurl + "/goods/clearCart.action", {}, function(result) {
			if (result.success) {
				$('#cartModal').modal('hide');
				$("#msgTitle").html("清除成功");
				$("#msgBody").html("已清空购物车");
				$("#msgModal").modal();
			}
		}, 'json');
		$("#cartBadge").html(0);
	}
	function changeCart(index, baseurl) {
		var newnum = $("#num" + index).val();
		$.post(baseurl + "/goods/changeCart.action", {
			goodsSales : newnum,
			index : index
		}, function(result) {
			if (result.success) {
				$("#totalAmount").html(result.totalAmount);
				$("#totalprice" + index).html(result.totalPrice);
			}
		}, 'json');
		$("#cartBadge").html(0);

	}
</script>
</html>