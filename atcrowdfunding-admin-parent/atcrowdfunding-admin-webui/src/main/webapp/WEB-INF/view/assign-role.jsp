<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include/include-head.jsp"%>
<script type="text/javascript">
    $(function () {
        // 权限分配单击事件
        $("#toRightAssign").click(function () {
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");
        })
        $("#toLeftAssign").click(function () {
            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        })
        // 修正表单提交bug
        $("#savaAdminAssignRoleBtn").click(function () {
            $("select:eq(1)>option").prop("selected","selected");
            /*
            * return false;     阻止表单提交
            * */
        })
    })
</script>
<body>
<%@include file="include/include-nav.jsp"%>
<div class="container-fluid">
    <div class="row">
        <%@include file="include/include-sidebar.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form role="form" class="form-inline" action="assign/do/role/assign.html">
                        <input type="hidden" name="adminId" value="${param.adminId}">
<%--                        由于当前页面请求的请求作用域对象中没有放入pageInfo信息，所有不能通过pageInfo来提取pageNum,
                        只能使用截取参数的方式获取pageNum。
--%>
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label>未分配角色列表</label><br>
                            <select class="form-control" multiple="" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssginRole}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightAssign" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftAssign" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label>已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assginRole}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <input id="savaAdminAssignRoleBtn" type="submit" value="保存">
                    </form>
                </div>
            </div>
        </div>

    </div>
</div>
</body>
</html>
