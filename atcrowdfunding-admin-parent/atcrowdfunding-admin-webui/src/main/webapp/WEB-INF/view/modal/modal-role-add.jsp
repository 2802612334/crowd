<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="modal fade" tabindex="-1" role="dialog" id="addModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">角色添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-signin" role="form">
                <div class="form-group has-success has-feedback">
                    <input type="text" name="roleName" class="form-control" id="inputSuccess4" placeholder="请输入角色名称" autofocus>
                 </div>
                </form>
        </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="roleSaveBtn">保 存</button>
            </div>
        </div>
    </div>
</div>