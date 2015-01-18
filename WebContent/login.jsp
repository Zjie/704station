<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>704所信息系统登录</title>
</head>
<body>
	<form
		method="post"
		action="/j_spring_security_check"
		style="width: 400px; text-align: left;">
		<fieldset>
			<legend>登陆</legend>
			用户： <input type="text" name="j_username" style="width: 150px;" value="" />
			<%
				String flag = (String)request.getParameter("flag") ;
				if (flag != null && flag.equals("1")) {
					out.print("验证失败，请重试！");
				}
			%>
			<br />
			密码： <input type="password" name="j_password" style="width: 150px;" /><br />
			<!--input type="checkbox" name="_spring_security_remember_me" />两周之内不必登陆<br /-->
			<input type="submit" value="登陆" /> 
			<input type="reset" value="重置" />
		</fieldset>
	</form>
</body>
</html>