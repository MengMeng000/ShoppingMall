<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>左侧个人中心下拉列表e</title>
</head>
<body>
	<div class="list-group">
		<ul class="nav nav-pills nav-stacked" id="left_cate">
			<li><a
				href="${pageContext.request.contextPath}/usercenter/getPersonalInfo.action">修改个人信息</a></li>
			<li><a
				href="${pageContext.request.contextPath}/usercenter/getPersonalPassword.action">修改密码</a></li>
			<li><a
				href="${pageContext.request.contextPath}/address/getMyAddress.action">管理收货地址</a></li>
		</ul>
	</div>
</body>
</html>