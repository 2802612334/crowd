package com.atguigu.crowd.exception;

public class RoleNameExistException extends RuntimeException{
    public RoleNameExistException() {
        super();
    }

    public RoleNameExistException(String message) {
        super(message);
    }
}
