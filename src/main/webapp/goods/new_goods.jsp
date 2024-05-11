<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新到商品</title>
</head>
<body>
	<%@include file="../header.jsp"%>
	<div class="row">
		<c:if test="${!empty newGoods }">
			<c:forEach items="${newGoods}" var="g">
				<div class="col-md-3">
					<div class="thumbnail">
						<a
							href="${pageContext.request.contextPath}/goods/goodsDetail.action?activePage=new_goods&goods_id=${g.goodsId}">
							<img alt="${g.goodsName}"
							src="${pageContext.request.contextPath}${g.goodsPic}"
							style="display: block; width: 240px; height: 240px" />
						</a>
						<div class="caption text-center">
							<h4 class="substr color">${g.goodsName}</h4>
							<p>
								原价<span class="glyphicon glyphicon-yen" aria-hidden="true"></span>${g.goodsPrice}
								<span class="glyphicon glyphicon-hand-right" aria-hidden="true"></span>
							</p>
							<p>
								现售<span class="label label-pill label-info"><span
									class="glyphicon glyphicon-yen" aria-hidden="true"></span>${g.goodsDiscount}</span>
							</p>
							<p>共售出${g.goodsSales}件</p>
						</div>
					</div>
				</div>
			</c:forEach>
		</c:if>
	</div>
	<%@include file="../footer.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#new_goods").addClass('active');
			$("#first_page").removeClass('active');
		});
	</script>
</body>
</html>