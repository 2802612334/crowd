<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include/include-head.jsp"%>
    <link rel="stylesheet" href="css/pagination.css" />

    <script type="text/javascript" src="jquery/jquery.pagination.js"></script>
    <link rel="stylesheet" href="ztree/zTreeStyle.css" type="text/css"/>
    <script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="crowd/my-role.js"></script>
    <script type="text/javascript">
        $(function () {
            // 定义全局属性，初始化分页数据
            window.pageNum = 1;
            window.pageSize = 7;
            window.keyword = "";
            window.roleIdArr = [];
            // 获取分页数据
            generatePage();
            // 模糊查询
            $("#searchBtn").click(function () {
                window.keyword = $("#searchInput").val();
                // 发送查询请求
                generatePage();
            });
            // 角色添加模态框
            $("#roleAddBtn").click(function () {
                // 1.清除上次输入内容
                $("#addModal :text").val("");
                // 2.展示模态框
                $("#addModal").modal("show");
            });
            // 角色添加
            $("#roleSaveBtn").click(function () {
                // 获取输入文本名称
                var roleName = $("#addModal :text").val().trim();
                $.ajax({
                    url: "role/save.json",
                    data:{
                        "name": roleName
                    },
                    type:"post",
                    dataType:"json",
                    success:function (response) {
                        // 添加失败
                        if(response.result == "FAILED"){
                            layer.msg(response.message);
                            return;
                        }

                        // 添加成功
                        // 1.关闭模态框
                        $("#addModal").modal("hide");
                        // 2.返回到最后一页
                        window.pageNum = 99999;
                        generatePage();
                        // 3.显示成功信息
                        layer.msg("添加成功");
                    },
                    error:function (response) {
                        layer.msg("服务器异常，请稍后访问！")
                    }
                })
            })
            /*
            * 角色更新，使用on函数为后代元素绑定事件
            * */
            $("#pageBody").on("click",".edit-btn",function () {
                // 1.打开模态框
                $("#editModal").modal("show");
                // 2.回显用户数据
                window.roleId = $(this).parent("td").parent("tr").attr("roleid");
                window.roleName = $(this).parent("td").siblings("td:eq(2)").text();
                $("#editModal :text").val(window.roleName);
            })
            $("#roleEditBtn").click(function () {
                // 获取用户输入的信息
                var roleNameInput = $("#editModal :text").val();
                if(roleNameInput == window.roleName){
                    // 用户名没有修改，提示用户重新输入
                    layer.msg("请输入修改后的角色名");
                    return;
                }
                $.ajax({
                    url:"role/edit.json",
                    dataType: "json",
                    data:{
                        "id": window.roleId,
                        "name": roleNameInput
                    },
                    type: "post",
                    success:function (response) {
                        // 更新失败
                        if(response.result == "FAILED"){
                            layer.msg(response.message);
                            return;
                        }

                        // 更新成功
                        // 1.关闭模态框
                        $("#editModal").modal("hide");
                        // 2.跳转到当前页
                        generatePage();
                        // 3.显示更新成功
                        layer.msg("更新成功");
                    },
                    error:function (response) {
                        layer.msg("服务器异常，请稍后访问！")
                    }
                })
            });
            // 单选删除
            $("#pageBody").on("click",".del-btn",function () {
                // 获取当前用户信息
                var id = $(this).parent("td").parent("tr").attr("roleid");
                var name = $(this).parent("td").siblings("td:eq(2)").text();
                var roleArray = [{
                    id:id,
                    name:name
                }];
                openModalConfirm(roleArray)
            });
            // 多选删除
            $("#roleDelBtn").click(function () {
                if($(".checkGroup:checked").length == 0){
                    layer.msg("请先选择删除的记录");
                    return;
                }
                var roleArray = [];
                var id = null;
                var name = null;
                $(".checkGroup:checked").each(function () {
                    id = $(this).parent("td").parent("tr").attr("roleid");
                    name = $(this).parent("td").next("td").text();
                    roleArray.push({id:id,name:name})
                });
                openModalConfirm(roleArray)
            });
            // 删除按钮
            $("#modalRoleDelBtn").click(function () {
                // 将角色id数组转换为JSON字符串
                var roleIdArrStr = JSON.stringify(window.roleIdArr);
                $.ajax({
                    url:"role/remove/role/id.json",
                    data:roleIdArrStr,
                    // 告诉服务器，浏览器传入的数据为JSON格式
                    contentType:"application/json;charset=utf-8",
                    dataType:"json",
                    type:"post",
                    success:function (response) {
                        // 更新失败
                        if(response.result == "FAILED"){
                            layer.msg(response.message);
                            return;
                        }
                        // 更新成功
                        // 1.关闭模态框
                        $("#delModal").modal("hide");
                        // 2.跳转到当前页
                        generatePage();
                        // 3.显示更新成功
                        layer.msg("删除成功");
                    },
                    error:function (response) {
                        layer.msg("服务器异常，请稍后访问！")
                    }
                })
            });
            // 全选/全不选
            $("#allCheck").click(function () {
                var flag = $(this).prop("checked");
                $(".checkGroup").prop("checked",flag)
            });
            $("#pageBody").on("click",".checkGroup",function () {
                var checkedLen = $(".checkGroup:checked").length;
                var allCheckedLen = $(".checkGroup").length;
                var flag = (checkedLen == allCheckedLen);
                $("#allCheck").prop("checked",flag);
            })
            // 权限分配
            $("#pageBody").on("click",".check-btn",function () {
                window.roleId = $(this).parent("td").parent("tr").attr("roleid");
                // 显示权限分配模态框
                $("#assignModal").modal("show");
                // 填充Auth的树形结构
                fillAuthTree();
            })
            // 角色分配权限，提交表单
            $("#saveAssignBtn").click(function () {
                // 存放所有被选中的authId
                var authIdList = [];
                // 获取所有被选中的节点
                var checkNodes = $.fn.zTree.getZTreeObj("authTreeDemo").getCheckedNodes();
                // 遍历所有被选中的节点
                for(var i = 0;i < checkNodes.length;i++){
                    var authId = checkNodes[i].id;
                    authIdList.push(authId);
                }

                // 发送ajax请求提交数据
                var requestBody = {
                    "authIdArray": authIdList,
                    // 因为后端接收该对象为 Map<String,List<Integer>> ,value对应为一个数组，所以该参数要加[]，变为json数组。
                    // 如果不加[]，前端参数和后端handler参数不一致，就会产生400错误（请求错误：请求地址，请求参数）。
                    "roleId": [window.roleId]
                }

                requestBody = JSON.stringify(requestBody);

                $.ajax({
                    "url": "assign/do/role/assign/auth.json",
                    "type": "post",
                    "data": requestBody,
                    "dataType": "json",
                    "contentType":"application/json;charset=UTF-8",
                    "success": function (response) {
                        layer.msg("权限修改成功！");
                        $("#assignModal").modal("hide");
                    },
                    "error": function () {
                        layer.msg("服务器异常！请稍后访问！")
                    }
                })
            })
        })
    </script>
<body>
<%@include file="include/include-nav.jsp"%>
<div class="container-fluid">
    <div class="row">
        <%@include file="include/include-sidebar.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="searchInput" class="form-control has-success" type="text" placeholder="请输入查询条件"/>
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;" id="roleDelBtn"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" class="btn btn-primary" style="float:right;" id="roleAddBtn"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="allCheck" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="pageBody">

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                        <%--引入角色添加模态框--%>
                        <%@include file="modal/modal-role-add.jsp"%>
                        <%--引入角色更新模态框--%>
                        <%@include file="modal/modal-role-edit.jsp"%>
                        <%--引入删除confirm模态框--%>
                        <%@include file="modal/modal-role-confirm.jsp"%>
<%--                        引入权限分配模态款--%>
                        <%@include file="modal/modal-role-assign-auth.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

