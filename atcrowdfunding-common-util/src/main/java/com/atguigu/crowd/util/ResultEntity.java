package com.atguigu.crowd.util;

import java.io.File;

public class ResultEntity<T> {

    private static final String SUCCESS = "SUCCESS";
    private static final String FAILED = "FAILED";

    private String result;
    private String message;
    private T data;

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    /*
    * 返回结果成功，不带数据
    * */
    public static <Type>ResultEntity<Type> successWithOutData(){
        return new ResultEntity<Type>(SUCCESS,null,null);
    }

    /*
    * 返回结果成功，携带数据
    * */
    public static <Type>ResultEntity<Type> successWithData(Type data){
        return new ResultEntity<Type>(SUCCESS,null,data);
    }

    /*
    * 返回结果失败，不携带数据
    * */
    public static <Type>ResultEntity<Type> failed(String message){
        return new ResultEntity<Type>(FAILED,message,null);
    }

    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static String getFAILED() {
        return FAILED;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
