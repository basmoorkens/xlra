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
		url("<%=request.getContextPath()%>/resources/images/extra_bg.png");
	background-size: cover;
	color: black;
	font-family: Verdana, Geneva, sans-serif;
	font-size: 18px;
}

#container {
	background-color: #DBDBDB;
	opacity: 0.8;
	margin-left: auto;
	margin-right: auto;
	margin-top: 5%;
	width: 75%;
	margin-top: 5%;
	min-height: 35em;
	-moz-border-radius: 28px;
	-webkit-border-radius: 28px;
	border-radius: 12px;
	border: 1px solid #17010c;
}

#loginTable {
	margin-top: 50px;
	margin-left: 10%;
	margin-right: 10%;
	width: 70%;
	border-style: solid;
	border-width: 3px;
	border-color: white;
}

#loginTable tr {
	margin-top: 10px;
}

#loginTable td {
	padding: 5px;
	padding-top: 15px;
}

#work {
	margin-left: 15%;
	margin-right: 15%;
}

.myButton {
	background-color: #d11174;
	-moz-border-radius: 12px;
	-webkit-border-radius: 12px;
	border-radius: 12px;
	border: 1px solid #17010c;
	display: inline-block;
	cursor: pointer;
	color: #ffffff;
	font-family: Arial;
	font-size: 17px;
	padding: 8px 52px;
	text-decoration: none;
	text-shadow: 0px -1px 0px #2f6627;
}

.myButton:hover {
	background-color: #f20e84;
}

.myButton:active {
	position: relative;
	top: 1px;
}

h1 {
	padding-left: 5em;
	margin-top: 0;
	padding-top: 50px;
	text-transform: uppercase;
	font-size: 1.5em;
	letter-spacing: -1px;
}

a {
	color: black;
	font-weight: bold;
	text-decoration: none;
}

a:visited {
	color: black;
	font-weight: bold;
	text-decoration: none;
}

a:hover {
	color: #d11174;
	font-weight: bold;
	text-decoration: none;
}

#content {
	padding-top: 30px;
}

.stronk {
	font-size: 20px;
	font-weight: bold;
}
</style>
</head>
<body>
	<div id="container">
		<div id="work">
			<img src="<%=request.getContextPath()%>/resources/images/Extra-Logo-neu.png"
				style="float: left;" />
			<h1>RATES APPLICATION</h1>

			<div id="content">
				Looks like the credentials you entered were invalid... Please try
				again or contact <a href="#">the administrator</a> to reset your
				password.
			</div>


			<form action="j_security_check" method="post" id="loginForm">
				<table id="loginTable">
					<tr>
						<td style="width: 50%;" align="right"><span class="stronk">USERNAME:</span></td>
						<td><input type="text" name="j_username" size="25"></td>
					</tr>
					<tr>
						<td align="right"><span class="stronk">PASSWORD:</span></td>
						<td><input type="password" size="25" name="j_password"></td>
					</tr>
					<tr>
						<td align="right"><a href="#" onclick="loginForm.submit();"
							class="myButton">Login</a></td>
						<td><a href="#" class="myButton">Forgot login?</a></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

</body>
</html>