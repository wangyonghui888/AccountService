<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.lawrence.account.service.*" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%
	String contextPath = request.getContextPath();
	//String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + basePath + "/";
	
	ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext( getServletContext() );
	AccountService accountervice = (AccountService) context.getBean( "accountService" );
	String captchaKey = accountervice.generateCaptchaKey();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=contextPath %>/css/main-layout.css">
		<script type="text/javascript" src="<%=contextPath %>/js/main.js"></script>
		<title>Account Service - Register</title>
	</head>
	<body>
		<div id="container">
			<h2>注册</h2>
			<form action="RigisterServlet" method="post" id="registerForm">
				<table>
					<tr>
						<td>用户ID：</td><td><input type="text" id="uid"></td>
					</tr>
					<tr>
						<td>昵称：</td><td><input type="text" id="uname"></td>
					</tr>
					<tr>
						<td>关联邮箱:</td><td><input type="text" id="email"></td>
					</tr>
					<tr>
						<td>密码：</td><td><input type="password" id="pwd"></td>
					</tr>
					<tr>
						<td>确认密码：</td><td><input type="password" id="pwdConfirm"></td>
					</tr>
					<tr>
						<td><input type="button" value="注册" id="commit"></td><td><input type="button" value="取消"></td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>