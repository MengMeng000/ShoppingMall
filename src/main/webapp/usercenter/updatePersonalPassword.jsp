<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>更新个人密码</title>
<script type="text/javascript">
	function check() {
		var newUserPass = $("#newUserPass").val();
		var newUserPassConfirm = $("#newUserPassConfirm").val();

		if (newUserPass != newUserPassConfirm) {
			alert("两次密码输入不一致");
			return false;
		} else {
			return true;
		}
	}
</script>
</head>
<body>
	<%@include file="../header.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2">
				<%@include file="userCenterLeftNav.jsp"%>
			</div>
			<div class="col-md-10">
				<form role="form" id="passForm" method="post"
					action="${pageContext.request.contextPath}/usercenter/updatePersonalPassword.action"
					onsubmit="return check()">

					<div class="form-group">
						<label for="userPass"> 旧密码 </label> <input class="form-control"
							name="userPass" id="userPass" type="password" required />
					</div>
					<div class="form-group">
						<label for="userPass"> 新密码 </label> <input class="form-control"
							name="newUserPass" id="newUserPass" type="password" required />
					</div>
					<div class="form-group">
						<label for="userPass1"> 密码确认 </label> <input class="form-control"
							name="newUserPassConfirm" id="newUserPassConfirm" type="password"
							required />
					</div>
					<div class="form-group">
						<button class="btn btn-primary" type="submit">确定</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<%@include file="../common/cartModal.jsp"%>
	<%@include file="../footer.jsp"%>
</body>
</html>