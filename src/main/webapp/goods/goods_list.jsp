<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品列表</title>
<%@include file="../header.jsp"%>
</head>
<body>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#left_cate").affix({
				offset : {
					top : 125,
					bottom : function() {
						return (this.bottom = $('.bottom').outerHeight(true));
					}
				}
			});
		});
	</script>
	<div class="row" style="border: 0px solid red">
		<div class="col-md-2">
			<div class="row">
				<ul class="list-group" id="left_cate">
					<c:forEach items="${catelist}" var="c" varStatus="vs">
						<c:forEach items="${c.childlist}" var="cl" varStatus="i">
							<c:choose>
								<c:when test="${cl.childid==childid}">
									<a
										href="${pageContext.request.contextPath}/goods/goodsCate.action?activePage=goods_list&childid=${cl.childid}"
										class="list-group-item active">${cl.childname}</a>
								</c:when>
								<c:otherwise>
									<a
										href="${pageContext.request.contextPath}/goods/goodsCate.action?activePage=goods_list&childid=${cl.childid}"
										class="list-group-item">${cl.childname}</a>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="col-md-10">
			<div class="row">
				<c:if test="${!empty goods }">
					<c:forEach items="${goods}" var="g">
						<div class="col-md-4">
							<div class="thumbnail">
								<a
									href="${pageContext.request.contextPath}/goods/goodsDetail.action?activePage=goods_list&goods_id=${g.goodsId}">
									<img alt="${g.goodsName}"
									src="${pageContext.request.contextPath}${g.goodsPic}"
									style="display: block; width: 280px; height: 280px" />
								</a>
								<div class="caption text-center">
									<label> ${g.goodsName} </label>
									<p>
										原价<span class="glyphicon glyphicon-yen" aria-hidden="true"></span>${g.goodsPrice}
										<span class="glyphicon glyphicon-hand-right"
											aria-hidden="true"></span>
									</p>
									<p>
										现售<span class="label label-pill label-info"><span
											class="glyphicon glyphicon-yen" aria-hidden="true"></span>${g.goodsDiscount}</span>
									</p>
									<p>共售出${g.goodsSales}件</p>
									<p>
										<a class="btn btn-primary"
											href="${pageContext.request.contextPath}/goods/goodsDetail.action?activePage=goods_list&goods_id=${g.goodsId}">查看详情</a>

										<a class="btn" href="add()">加入购物车</a>
									</p>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>

				<c:if test="${empty goods}">
					<div class="alert alert-danger col-md-2" role="alert">对不起，暂无此类商品</div>
				</c:if>
			</div>
		</div>
	</div>
	<%@include file="../footer.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#goods_list").addClass('active');
			$("#first_page").removeClass('active');
		});
	</script>
</body>
</html>