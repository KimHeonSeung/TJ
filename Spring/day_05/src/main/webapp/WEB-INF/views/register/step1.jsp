<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>ȸ������</title>
</head>
<body>
<h2>���</h2>
<p>��� ����</p>
<form action="<%= request.getContextPath() %>/register/step2" method="post">
	<label>
		<input type="checkbox" name="agree" value="true">��� ����
	</label>
	<div>�̸� : <input type="text" name="name"></div>
	<div>���� : <input type="text" name="age"></div>
	<input type="submit" value="�����ܰ�" />
</form>


</body>
</html>