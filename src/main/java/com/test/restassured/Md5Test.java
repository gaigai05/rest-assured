package com.test.restassured;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Md5Test {

    public static void setKey(){
        //测试环境 可由开发提供一个万能验证码
        //但是上线需要获取真实的验证码
        //开发配合token永不失效
        System.out.println("testing");
    }
}