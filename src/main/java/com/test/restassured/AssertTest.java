package com.test.restassured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.*;

@SpringBootApplication
public class AssertTest {

    //beforeclass机制，固定方法动作初始化
    //执行每个case之前都会自动执行，整个脚本只会跑一次
    @BeforeClass
    public static void setup(){
        useRelaxedHTTPSValidation();
        //请求的域名提取出来，不用每个case都写
        RestAssured.baseURI = "https://testerhome.com";
        //设置全局代理，所有的网络请求都会走代理
        //RestAssured.proxy("127.0.0.1",8888);
    }

    @Test
    public void testHtml() {
        given()
                .queryParam("q", "appium")
                .when()
                .get("/search").prettyPeek()
                .then()
                //.time() 断言网络请求时间
                .statusCode(200)  //通常作为第一个断言
                //.body() 2种语法 1.指定path路径 2.匹配的方式
                .body("html.head.title",equalTo("appium · 搜索结果 · TesterHome"));
    }

    @Test
    //正规的断言  testful风格的http接口
    //jsonPath方式 获取复杂树形结构体的数据 父级.子级
    public void testJson(){
        Response response = given()
                .when().get("/api/v3/topics.json").prettyPeek()
                .then()
                .statusCode(200)
                //.body("topics.title",hasItems("wrk,ab,locust,Jmeter 压测结果比较"))
                //.body("topics.title[4]",equalTo("如果有一天空气也收费"))
                //topics下最后一个title
                .body("topics.id[-1]",equalTo(17205))
                //findAll 结果是数组
                //.body("topics.findAll{ topic->topic.id == 17204 }.title",hasItems("关于 Jmeter 压力测试的使用"))
                //.body("topics.find{ topic->topic.id == 17204 }.title",equalTo("关于 Jmeter 压力测试的使用"))
                .body("topics.title.size()",equalTo(23))
                .extract().response();
        System.out.println("输出状态行：" + response.statusLine());//HTTP/1.1 200 OK
    }

    @Test
    public void testJsonSingle(){
        given()
                .when().get("/api/v3/topics/17068.json").prettyPeek()
                .then()
                .statusCode(200).body("topic.title",equalTo("wrk,ab,locust,Jmeter 压测结果比较"));
    }

    //xmlPath
//    @Test
//    public void testXml(){
//        Response response = given()
//                .when().get("http://127.0.0.1:8000/hogwarts.xml").prettyPeek()
//                .then().statusCode(200)
//                .body("shopping.category.item.name[2]",equalTo("Paper"))
//                .body("shopping.category[1].item[1]",equalTo("pens"))
//                .body("shopping.category.size()",equalTo(3))
//                .body("shopping.category[1].item.size()",equalTo(2))
//                //type 是属性需要@
//                .body("shoping.category.find {it.@type == 'present'}.item.name",equalTo("Kathryn's Birthday"))
//                //** 只适用于xmlPath
//                //以'<'开始的是节点,不需要@
//                .body("**.find {it.price == 15}.name",equalTo("Kathryn's Birthday"))
//                //导出一定内容存成一个值
//                //.extract().path("shopping.category.item.name[2]");
//                .extract().response();
//        System.out.println(response.statusLine());
//    }

}