function generatePage() {
    // 获取pageInfo数据
    var pageInfo = getPageInfoRemote();

    // 填充表格
    fillTableBody(pageInfo);

    // 生成分页导航条
    generateNavigator(pageInfo);
}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "data": {
            pageNum: pageNum,
            pageSize: pageSize,
            keyword: keyword
        },
        "type": "post",
        /*
        * 设置为同步请求，等数据返回执行后面的代码
        * */
        "async": false
    })
    // 如果状态码不为200，说明请求失败
    if(ajaxResult.status != 200){
        layer.msg("服务器异常,请稍后访问！")
        return null;
    }
    var responseJSON = ajaxResult.responseJSON;
    // 返回结果失败，提示用户
    if(responseJSON.result == "FAILED"){
        layer.msg(responseJSON.message)
        return null;
    }
    var pageInfo = responseJSON.data;
    return pageInfo;
}

// 填充表格
function fillTableBody(pageInfo) {
    // 清空上次表格信息，防止信息追加
    $("#pageBody").empty();
    $("#Pagination").empty();
    $("#allCheck").prop("checked",false);

    // 判断pageInfo是否有效
    if(pageInfo == null || pageInfo == undefined ||
        pageInfo.list == null || pageInfo.list == undefined || pageInfo.list.length == 0
    ){
        $("#pageBody").append("<tr></tr>").append("<td colspan='4' align='center'>抱歉！没有查询到您搜索的数据</td>");
        return null;
    }
    var pageList = pageInfo.list;
    for(var i = 0;i < pageList.length;i++){
        var idTd = "<td>"+(i+1)+"</td>";
        var checkTd = "<td><input class='checkGroup' type='checkbox'></td>";
        var nameTd = "<td>"+pageList[i].name+"</td>";
        var btnTd = "<td>" +
            "<button type=\"button\" class=\"btn btn-success btn-xs check-btn\"><i class=\" glyphicon glyphicon-check\"></i></button>\n" +
            "<button type=\"button\" class=\"btn btn-primary btn-xs edit-btn\"><i class=\" glyphicon glyphicon-pencil\"></i></button>\n" +
            "<button type=\"button\" class=\"btn btn-danger btn-xs del-btn\"><i class=\" glyphicon glyphicon-remove\"></i></button>\n" +
            "</td>";
        var pageTr = "<tr roleid='"+pageList[i].id+"'>"+idTd+checkTd+nameTd+btnTd+"</tr>";
        $("#pageBody").append(pageTr);
    }
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {
    // 获取总记录数
    var pageTotal = pageInfo.total;

    // 分页信息
    var properties = {
        num_edge_entries: 2, //边缘页数
        num_display_entries: 4, //主体页数
        current_page:pageInfo.pageNum-1,        // 当前页
        callback: pageselectCallback,           // 单击页码使用的回调函数
        items_per_page:pageInfo.pageSize,    // 每页显示pageSize项
        prev_text:"上一页",
        next_text:"下一页"
    }
    // 创建分页（点击回调函数）
    $("#Pagination").pagination(pageTotal,properties)
}

// 翻页时的回调函数
function pageselectCallback(pageIndex,jQuery) {
    // 更新当前页的全局变量
    window.pageNum = pageIndex + 1;
    // Pagination的默认分页从0开始
    /*
    * 不会出现递归调用：因为只有用户点击按钮才会调用此函数
    * */
    generatePage();
    return false;
}

// 删除角色，打开模态框
function openModalConfirm(roleArray) {
    // 清空上次提示信息
    $("#confirmMsg").text("");
    window.roleIdArr = [];
    // 获取删除角色id到全局数组
    for(var i = 0;i < roleArray.length;i++){
        roleIdArr.push(roleArray[i].id);
        $("#confirmMsg").append(roleArray[i].name+"<br/>");
    }
    // 展示模态框
    $("#delModal").modal("show");
}

// 填充Auth的树形结构
function fillAuthTree() {
    // 1.后端获取权限数据
    var ajaxReturn = $.ajax({
        "url": "assign/get/all/auth.json",
        "method": "post",
        "dataType": "json",
        "async": false
    });
    if(ajaxReturn.status != 200){
        layer.msg("服务器异常！"+"状态码为："+ajaxReturn.status)
        return;
    }
    // 2.生成树形结构
    // 节点数据
    var authList = ajaxReturn.responseJSON.data;
    // 树形设置
    var setting = {
        "data": {
            "simpleData": {
                // 开启简单的Json功能（返回数据中没有children）
                "enable": true,

                // 使用categoryId属性关联父节点，不用默认的pid了
                "pIdKey": "categoryId"
            },
            "key": {
                // 使用"title"属性显示节点名称，不用默认的name作为属性名了
                "name": "title"
            }
        },
        "check": {
            // 在每个节点前面显示一个checkbox
            "enable": true
        }
    }
    // 树形渲染
    $.fn.zTree.init($("#authTreeDemo"),setting,authList);
    // 默认使节点打开
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    zTreeObj.expandAll(true);

    // 3.回显当前用户权限
    ajaxReturn = $.ajax({
        "url": "assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "dataType": "json",
        "async": false,
        "data": {
            "roleId": window.roleId
        }
    })

    var authIdList = ajaxReturn.responseJSON.data;
    for(var i = 0;i < authIdList.length;i++){
        var authId = authIdList[i];
        // 获取被选中的节点对象
        var treeNode = zTreeObj.getNodeByParam("id",authId);

        // checked 设置为 true 表示节点勾选
        var checked = true;
        // checkTypeFlag 设置为 false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;
        // 执行
        zTreeObj.checkNode(treeNode,checked,checkTypeFlag);
    }
}