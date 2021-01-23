<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="modal fade" tabindex="-1" role="dialog" id="assignModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">角色权限分配</h4>
            </div>
            <div class="modal-body">
                <ul id="authTreeDemo" class="ztree">

                </ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="saveAssignBtn">好的，我设置好了！执行分配！</button>
            </div>
        </div>
    </div>
</div>
