<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include/include-head.jsp"%>
<body>
<%@include file="include/include-nav.jsp"%>
<div class="container-fluid">
    <div class="row">
        <%@include file="include/include-sidebar.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">修改</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <form role="form" method="post" action="admin/to/edit/update.html">
                        <p>${requestScope.exception.message}</p>
                        <input type="hidden" value="${param.pageNum}" name="pageNum"/>
                        <input type="hidden" value="${param.keyword}" name="keyword"/>
                        <input type="hidden" value="${param.id}" name="id"/>
                        <div class="form-group">
                            <label for="exampleInputPassword1">登录账号</label>
                            <input type="text" class="form-control" id="exampleInputPassword1" value="${editAdmin.loginAcct}" name="loginAcct">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword2">用户名称</label>
                            <input type="text" class="form-control" id="exampleInputPassword2" value="${editAdmin.userName}" name="userName">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">邮箱地址</label>
                            <input type="email" class="form-control" id="exampleInputEmail1" value="${editAdmin.email}" name="email">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-edit"></i> 修改</button>
                        <button type="reset" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
