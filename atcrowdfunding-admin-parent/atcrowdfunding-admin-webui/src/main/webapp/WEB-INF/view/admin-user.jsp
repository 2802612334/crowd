<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <%@include file="include/include-head.jsp"%>
    <link rel="stylesheet" href="css/pagination.css" />
    <script type="text/javascript" src="jquery/jquery.pagination.js"></script>
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color:#C00;}
    </style>

    <script type="text/javascript">
        $(function () {
            initPagination();
            // 删除用户
            $("table .btn-danger").click(function () {
                if(confirm("确定删除"+$(this).attr("loginAcct")+"吗？")){
                    // 获取当前用户id
                    var id = $(this).attr("loginId");
                    // 获取模糊查询keyword
                    var keyword = "${param.keyword}";
                    // 获取当前页
                    var pageNum = ${requestScope.pageInfo.pageNum};
                    window.location.href = "admin/to/remove/"+id+"/"+pageNum+"/"+keyword+".html"
                }
            })

            // 用户编辑
            $("table .btn-primary").click(function () {
                var id = $(this).attr("loginId");
                window.location.href = "admin/to/edit/page.html?id="+id+"&pageNum="+${requestScope.pageInfo.pageNum};
            })

            // 权限修改
            $("table .btn-success").click(function () {
                var id = $(this).attr("loginId");
                var pageNum = ${requestScope.pageInfo.pageNum}
                var keyword = ${param.keyword}""
                window.location.href = "assign/to/assign/role/page.html?adminId="+id+"&pageNum="+pageNum+"&keyword="+keyword
            })

        })
        function initPagination() {
            // 获取总记录数
            var pageTotal = ${requestScope.pageInfo.total}
            var properties = {
                num_edge_entries: 2, //边缘页数
                num_display_entries: 4, //主体页数
                current_page:${requestScope.pageInfo.pageNum-1},        // 当前页
                callback: pageselectCallback,           // 单击页码使用的回调函数
                items_per_page:${requestScope.pageInfo.pageSize},    // 每页显示pageSize项
                prev_text:"上一页",
                next_text:"下一页"
            }
            // 创建分页（点击回调函数）
            $("#Pagination").pagination(pageTotal,properties)
        }
        function pageselectCallback(pageIndex,jQuery) {
            pageIndex = pageIndex + 1;
            // Pagination的默认分页从0开始
            window.location.href = "admin/to/user/page.html?"+"pageNum="+ pageIndex+"&keyword=${param.keyword}"
            return false;
        }
    </script>
<body>
    <%@include file="include/include-nav.jsp"%>
<div class="container-fluid">
    <div class="row">
        <%@include file="include/include-sidebar.jsp"%>
    </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;" action="admin/to/user/page.html" method="post">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input class="form-control has-success" type="text" placeholder="请输入查询条件" name="keyword">
                            </div>
                        </div>
                        <button type="submit" class="btn btn-warning" id="selectBtn"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button id="addBtn"
                            type="button" class="btn btn-primary" style="float:right;"
                            onclick="window.location.href='admin/to/add/page.html?pageNum=${requestScope.pageInfo.pageNum}&keyword=${param.keyword}'">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:if test="${requestScope.pageInfo.list != null}">
                                    <%
                                        int i = 0;
                                    %>
                                    <c:forEach items="${requestScope.pageInfo.list}" var="admin">
                                        <%
                                            i++;
                                        %>
                                        <tr>
                                            <td><%=i%></td>
                                            <td><input type="checkbox"></td>
                                            <td>${admin.loginAcct}</td>
                                            <td>${admin.userName}</td>
                                            <td>${admin.email}</td>
                                            <td>
                                                <button type="button" class="btn btn-success btn-xs" loginId="${admin.id}"><i class=" glyphicon glyphicon-check"></i></button>
                                                <button type="button" class="btn btn-primary btn-xs" loginId="${admin.id}"><i class=" glyphicon glyphicon-pencil"></i></button>
                                                <button type="button" class="btn btn-danger btn-xs" loginId="${admin.id}" loginAcct="${admin.loginAcct}"><i class=" glyphicon glyphicon-remove"></i></button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${requestScope.pageInfo.list == null}">
                                    <tr>
                                        <td colspan="2">抱歉！没有查询到相关的数据！</td>
                                    </tr>
                                </c:if>
                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
