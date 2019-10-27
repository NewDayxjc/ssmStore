
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>casclient</title>
</head>
<body>
登录的用户是：<%=request.getRemoteUser()%><br>
    28005
<a href="http://localhost:28083/cas/logout?service=http://www.baidu.com">退出</a>
</body>
</html>
