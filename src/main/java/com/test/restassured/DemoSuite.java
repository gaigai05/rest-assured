package com.test.restassured;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//多个case有执行顺序时 RunWith、Suite实现
//有业务关系的接口

//RunWith以Suite.class模式运行
//@RunWith(Suite.class)
//@Suite.SuiteClasses({
//        //按照以下类顺序执行case
//        InterfaceTest1.class,
//        InterfaceTest.class
//})
//@SpringBootApplication
//public class DemoSuite {
//
//}
