function generatorMenu() {
    $.ajax({
        url: "menu/get/whole/tree.json",
        type: "post",
        success:function (response) {
            // 获取数据失败
            if(response.result == "FAILED"){
                layer.msg(response.message);
                return;
            }

            // 获取数据成功（使用zTree渲染页面）
            // 1.配置zTree
            var setting = {
                view: {
                    // 图标配置
                    addDiyDom : addDiyDom,
                    // 鼠标经过显示按钮组
                    addHoverDom: addHoverDom,
                    // 鼠标脱离隐藏按钮组
                    removeHoverDom:removeHoverDom
                },
                data: {
                    key: {
                        url: "crowd"
                    }
                }
            }
            // 2.获取数据
            var nodes = response.data;
            // 3.调用zTree渲染页面
            $.fn.zTree.init($("#treeDemo"), setting, nodes);
        },
        error:function (response) {
            layer.msg("服务器异常，请稍后访问！")
        }
    });
}

function addDiyDom(treeId,treeNode) {
    // treeId为所有显示菜单的父节点（treeDemo）
    // treeNode为所有节点的数据信息
    // 1.获取当前菜单节点的图标id id为tId属性 + _ico
    var icoId = treeNode.tId + "_ico";
    // 2.移除当前菜单节点图标的所有样式
    $("#"+icoId).removeClass();
    // 3.加入当前菜单节点图标需要的样式，样式存取在treeNode.icon属性中
    $("#"+icoId).addClass(treeNode.icon);
}

// 鼠标经过显示span
function addHoverDom(treeId,treeNode) {
    // 1.控制<span></span>是否显示，在超链接标记(treeDemo_3_a)之后加入span
    var aId = treeNode.tId + "_a";
    var btnGroupId = treeNode.tId + "_btnGop";
    if($("#"+btnGroupId).length != 0){
        // 如果有当前按钮组，退出即可
        return;
    }
    /*
    * 2.明确具体按钮的添加规则
    *   级别0（根节点）：添加
    *   级别1（子节点）：添加子节点，修改，如果当前节点没有子节点可以删除
    *   级别2（叶子节点）：修改，删除
    * */
    // 3.准备按钮的HTML标签
    var editBtn = "<a id="+treeNode.id+" class=\"btn btn-info dropdown-toggle btn-xs editBtn\" style=\"margin-left:10px;padding-top:0px;\" href=\"javascript:void(0)\">" +
        "&nbsp;&nbsp;" +
        "<i class=\"fa fa-fw fa-edit rbg\" title=\"更新\"></i>+" +
        "</a>";
    var delBtn = "<a id="+treeNode.id+" class=\"btn btn-info dropdown-toggle btn-xs delBtn\" style=\"margin-left:10px;padding-top:0px;\" href=\"javascript:void(0)\">" +
        "&nbsp;&nbsp;" +
        "<i class=\"fa fa-fw fa-times rbg \" title='删除'></i>" +
        "</a>";
    var addBtn = "<a id="+treeNode.id+" class=\"btn btn-info dropdown-toggle btn-xs addBtn\" style=\"margin-left:10px;padding-top:0px;\" href=\"javascript:void(0)\">" +
        "&nbsp;&nbsp;" +
        "<i class=\"fa fa-fw fa-plus rbg\" title='添加'></i>" +
        "</a>";
    var tLevel = treeNode.level;
    var spanHtml = "";
    if(tLevel == 0){
        spanHtml = addBtn;
    }
    if(tLevel == 1){
        spanHtml = editBtn + addBtn;
        // var ulLen = $("#"+treeNode.tId+" "+".level2").length;
        var ulLen = treeNode.children.length;
        if(ulLen == 0){
            // 如果当前菜单没有有子节点增加删除按钮，有则无需添加
            spanHtml += delBtn;
        }
    }
    if(tLevel == 2){
        spanHtml = editBtn + delBtn;
    }
    // 4.根据按钮规则添加到span中
    $("#"+aId).after("<span id='"+btnGroupId+"'>"+spanHtml+"</span>");
}

// 鼠标脱离隐藏span
function removeHoverDom(treeId,treeNode) {
    var btnGroupId = treeNode.tId + "_btnGop";
    //$("#"+spanId).html("");
    $("#"+btnGroupId).remove();
}