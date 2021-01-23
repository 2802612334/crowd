<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <base href="<%=basePath%>" />
    <title>首页</title>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
</head>
<body>
    <script>
        $(function () {
            $("#btn").click(function () {
                $.ajax({
                    url:"ajax/test.json",
                    type:"post",
                    success:function (data) {
                        window.alert(data.result)
                    }
                })
            })
            // $("#alertBtn").click(function () {
            //     layer.msg("hello world")
            // })
        })
    </script>
    <a href="ssm/test.html">test SSM</a>
    <button id="btn">发送Ajax请求</button>
    <button id="alertBtn">点我弹框</button>
</body>
</html>
