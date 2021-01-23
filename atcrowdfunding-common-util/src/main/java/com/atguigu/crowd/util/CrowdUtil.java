package com.atguigu.crowd.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CrowdUtil {

    // MD5加密
    public static String md5(String source){
        // 判断是否为空
        if(source == null || source.length() == 0){
            throw new RuntimeException();
        }
        try {
            String algorithm ="md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 获取字符串对应的字节数组,执行加密
            byte[] out = messageDigest.digest(source.getBytes());
            BigInteger bigInteger = new BigInteger(1,out);
            // 按照16进制转换为字符串
            String encoded = bigInteger.toString(16).toUpperCase();
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 判断是否为异步请求
    public static boolean judgeRequestType(HttpServletRequest request){
        String accept = request.getHeader("Accept");
        String x_Request = request.getHeader("X-Requested-With");

        /*
        * 判断是否为异步请求的依据：如果请求头的Accept属性中包含：application/json或者
        *   X-Requested-With的值为XMLHttpRequest
        * */
        if((accept != null && accept.contains("application/json"))
                    ||
                (x_Request != null && x_Request.equals("XMLHttpRequest"))
        ){
            // 异步请求
            return true;
        }
        // 非异步请求
        return false;
    }

    // 生成随机验证码
    public static String makeVerificationCode(){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = 0;i < 6;i++){
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            String bucketName,
            String objectName,
            InputStream inputStream
    ){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        accessKeyId = "<yourAccessKeyId>";
        accessKeySecret = "<yourAccessKeySecret>";
        bucketName = "<yourBucketName>";
        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        objectName = "<yourObjectName>";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        return new ResultEntity<>();
    }

    // 获取文件扩展名
    public static String getFileExtensionName(String fileName){
        int startIndex = fileName.lastIndexOf('.');
        return fileName.substring(startIndex,fileName.length());
    }

    // 修改图片绝对地址为http请求地址
    public static String getPictrueHttpAdress(String prefix,String absoluteAddres){
        // D:\JavaIDE\code\Java\atcrowdfunding-member-parent\atcrowdfunding-member-authentication-consumer\target\classes\static\crowd-project-picture\headerpicture\2021-01-20\b32dd902dfdc4476bd1749162bcce6d5.png
        String[] addressArr = absoluteAddres.split("\\\\");
        Integer index = null;
        for(int i = 0;i < addressArr.length;i++){
            if(addressArr[i].equals("crowd-project-picture")){
                index = i;
            }
        }
        if(index == null){
            throw new RuntimeException("绝地地址不合法：" + absoluteAddres);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        for(int i = index;i < addressArr.length;i++){
            stringBuilder.append(addressArr[i]);
            stringBuilder.append('/');
        }
        stringBuilder.substring(0,stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
