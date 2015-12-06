<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Extra logistics rates application</title>
<style>
body {
	background-image:
		url("<%=request.getContextPath()%>/images/extra_bg.png");
	color: white;
	font-family: Verdana, Geneva, sans-serif;
	font-size: 18px;
}

#container {
	background-color: grey;
	opacity: 0.8;
	margin-left: auto;
	margin-right: auto;
	margin-top: 5%;
	width: 80%;
	margin-top: 5%;
	min-height: 35em;
}

#loginTable {
	margin-top: 50px;
	border-style: solid;
	border-width: 3px;
	border-color: white;
	width: 100%;
}

#loginTable tr {
	margin-top: 5px;
}

#loginTable td {
	padding: 5px;
}

#work {
	margin-left: 15%;
	margin-right: 15%;
}

.myButton {
	margin-left: 30%;
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ff36ff
		), color-stop(1, #d07fe0));
	background: -moz-linear-gradient(top, #ff36ff 5%, #d07fe0 100%);
	background: -webkit-linear-gradient(top, #ff36ff 5%, #d07fe0 100%);
	background: -o-linear-gradient(top, #ff36ff 5%, #d07fe0 100%);
	background: -ms-linear-gradient(top, #ff36ff 5%, #d07fe0 100%);
	background: linear-gradient(to bottom, #ff36ff 5%, #d07fe0 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ff36ff',
		endColorstr='#d07fe0', GradientType=0);
	background-color: #ff36ff;
	-moz-border-radius: 28px;
	-webkit-border-radius: 28px;
	border-radius: 28px;
	border: 1px solid #e63df5;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: Arial;
	font-size: 17px;
	padding: 8px 62px;
	text-decoration: none;
	text-shadow: 0px 1px 0px #2f6627;
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ff36ff
		), color-stop(1, #d07fe0));
}

.myButton:hover {
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0.05, #d07fe0
		), color-stop(1, #ff36ff));
	background: -moz-linear-gradient(top, #d07fe0 5%, #ff36ff 100%);
	background: -webkit-linear-gradient(top, #d07fe0 5%, #ff36ff 100%);
	background: -o-linear-gradient(top, #d07fe0 5%, #ff36ff 100%);
	background: -ms-linear-gradient(top, #d07fe0 5%, #ff36ff 100%);
	background: linear-gradient(to bottom, #d07fe0 5%, #ff36ff 100%);
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#d07fe0',
		endColorstr='#ff36ff', GradientType=0);
	background-color: #d07fe0;
}

.myButton:active {
	position: relative;
	top: 1px;
}

h1 {
	padding-left: 5em;
	color: black;
	margin-top: 0;
	padding-top: 0;
}
</style>
</head>
<body>
	<div id="container">
		<div id="work">
			<img src="<%=request.getContextPath()%>/images/Extra-Logo-neu.png"
				style="float: left;" />
			<h1>rates applications</h1>
			Welcome to the Extra logistics rates application.<br /> You can use
			this page to login to the application. <br /> If you do not have a
			valid login to the application please contact kevin govaerts to
			request access.


			<form action="j_security_check" method="post" id="loginForm">
				<table id="loginTable">
					<tr>
						<td style="width: 50%;" align="right">Username:</td>
						<td><input type="text" name="j_username" size="25"></td>
					</tr>
					<tr>
						<td align="right">Password:</td>
						<td><input type="password" size="25" name="j_password"></td>
					</tr>
					<tr>
						<td colspan="2"><a href="#" onclick="loginForm.submit();"
							class="myButton">Login</a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

</body>
</html>