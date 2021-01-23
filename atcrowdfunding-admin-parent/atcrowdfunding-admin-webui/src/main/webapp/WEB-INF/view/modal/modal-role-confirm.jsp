<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="modal fade" tabindex="-1" role="dialog" id="delModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">角色删除</h4>
            </div>
            <div class="modal-body">
                <p>确认要删除以下记录：</p>
                <div id="confirmMsg" style="text-align: center;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="modalRoleDelBtn">删 除</button>
            </div>
        </div>
    </div>
</div>
