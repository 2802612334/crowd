<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include/include-head.jsp"%>
<%--引入ztree的样式，js--%>
<link rel="stylesheet" href="ztree/zTreeStyle.css" type="text/css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="crowd/my-menu.js"></script>
<script type="text/javascript">
    $(function () {
        // 获取页面数据（zTree）
        generatorMenu();
        // 为按钮组增加单击事件，由于是页面加载后，js生成的dom，所有使用on添加事件
        $("#treeDemo").on("click",".addBtn",function () {
            // 清空上次输入信息
            $("#menuAddModal [name=name]").val("");
            $("#menuAddModal [name=url]").val("");
            $("#menuAddModal [name=icon]:checked").prop("checked",false);
            window.pid = $(this).attr("id");
            $("#menuAddModal").modal("show");
        })

        $("#treeDemo").on("click",".editBtn",function () {
            window.id = $(this).attr("id");
            // 展示模态框
            $("#menuEditModal").modal("show");
            /*
            * 回显表单数据：
            *   1.获取zTreeObj对象
            *   2.根据id属性查询节点对象
            *   3.获取节点对象 zTreeObj.getNodeByParam(key,value);
            *       根据key和value匹配节点
            * */
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var key = "id";
            var value = window.id;
            var treeNode = zTreeObj.getNodeByParam(key,value);
            $("#menuEditModal [name=name]").val(treeNode.name);
            $("#menuEditModal [name=url]").val(treeNode.url);
            // 单选控件和多选空间选中的方式：采用数组，传入需要选中的value
            $("#menuEditModal [name=icon]").val([treeNode.icon]);
        });
        $("#treeDemo").on("click",".delBtn",function () {
            window.id = $(this).attr("id");
            $("#menuConfirmModal").modal("show");
            // 获取节点名称
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var key = "id";
            var value = window.id;
            var treeNode = zTreeObj.getNodeByParam(key,value);
            $("#removeNodeSpan").html("  <span class='"+treeNode.icon+"'></span>"+treeNode.name+"  ")
        });

        // 为模态框按钮绑定单击响应函数
        $("#menuSaveBtn").click(function () {
            // 获取表单数据
            var name = $("#menuAddModal [name=name]").val();
            var url = $("#menuAddModal [name=url]").val();
            var icon = $("#menuAddModal [name=icon]:checked").val();

            $.ajax({
                url: "menu/save.json",
                data: {
                  "pid": window.pid,
                  "name": name,
                  "url": url,
                  "icon": icon
                },
                type: "post",
                success: function (response) {
                    // 添加失败
                    if(response.result == "FAILED"){
                        layer.msg(response.message);
                        return;
                    }
                    // 添加成功
                    $("#menuAddModal").modal("hide");
                    // 重新加载树形结构
                    generatorMenu();
                    layer.msg("添加成功！");
                },
                error: function () {
                    layer.msg("服务器异常，请稍后访问！")
                }
            })
        });

        $("#menuEditBtn").click(function () {
            // 获取表单数据
            var name = $("#menuEditModal [name=name]").val();
            var url = $("#menuEditModal [name=url]").val();
            var icon = $("#menuEditModal [name=icon]:checked").val();
            // 发送ajax请求
            $.ajax({
                url: "menu/update.json",
                data: {
                    id: window.id,
                    name: name,
                    url: url,
                    icon: icon
                },
                type: "post",
                success: function (response) {
                    // 更新失败
                    if(response.result == "FAILED"){
                        layer.msg(response.message);
                        return;
                    }
                    // 更新成功
                    $("#menuEditModal").modal("hide");
                    // 重新加载树形结构
                    generatorMenu();
                    layer.msg("更新成功！");
                },
                error: function () {
                    layer.msg("服务器异常，请稍后访问！")
                }
            })
        })
        $("#confirmBtn").click(function () {
            $.ajax({
                url: "menu/remove.json",
                data: {
                    id: window.id
                },
                type: "post",
                success: function (response) {
                    // 更新失败
                    if(response.result == "FAILED"){
                        layer.msg(response.message);
                        return;
                    }
                    // 更新成功
                    $("#menuConfirmModal").modal("hide");
                    // 重新加载树形结构
                    generatorMenu();
                    layer.msg("删除成功！");
                },
                error: function () {
                    layer.msg("服务器异常，请稍后访问！")
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
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表 <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree" style="user-select: none;"></ul>
                </div>
            </div>
        </div>
    </div>
    <%--引入增加，删除，修改模态框--%>
    <%@include file="modal/modal-menu-add.jsp"%>
    <%@include file="modal/modal-menu-confirm.jsp"%>
    <%@include file="modal/modal-menu-edit.jsp"%>
</div>
</body>
</html>


