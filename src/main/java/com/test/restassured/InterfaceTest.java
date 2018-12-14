package com.test.restassured;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.*;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.Matchers.*;

public class InterfaceTest {

    //BeforeClass必须有static修饰
    //BeforeClass一个类只执行一次
    @BeforeClass
    public static void setup(){
        useRelaxedHTTPSValidation();
        RestAssured.baseURI = "https://testerhome.com";
        //RestAssured.proxy("172.17.6.22",8888);
    }

    //Before在每个test执行前都会被执行一次
    //比如登录时的cookie值
    @Before
    public void before(){

    }

    @Test
    public void testJsonGlobal(){
        //单独case设置代理
        given()//.proxy("172.17.6.22",8888)
                .when().get("/api/v3/topics.json").prettyPeek()
                .then()
                .statusCode(200)
                .body("topics.title",hasItems("好消息，社区专栏新功能来啦！"));
    }

    @Test
    public void testJsonPost(){
        HashMap<String,Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id",6040);
        jsonMap.put("title","通过代理安装appium");
        jsonMap.put("name","gaigai");

        HashMap<String,Object> root = new HashMap<String, Object>();
        root.put("topic",jsonMap);

        given().contentType(ContentType.JSON)
                .body(root)
                .when().post("http://www.baidu.com").prettyPeek()
                .then()
                //时间断言 不适合连接代理耗费时间  接口的性能断言
                //模拟第三方接口超时或响应较慢时，本服务器有无性能问题
                //断言第三方接口的响应时间
                .time(lessThan(1000L));//小于1000ms
    }

    //多个接口之间的业务关联
    //登录token带入到所有接口请求中(以取name为例)
    //testAssured 导出数据
    @Test
    public void multiApi(){
        String name = given().get("https://testerhome.com/api/v3/topics/17068.json").prettyPeek()
                .then().statusCode(200)
                .extract().path("topic.user.name");
        System.out.println(name);

        //将变量name带到其他接口中
        given().queryParam("q",name)
                .when().get("/search")
                .then().statusCode(200)
                .body(containsString(name));
    }

    //需要通过代理
    @Test
    public void multiApiMultiData() {
        Response response = given().get("https://testerhome.com/api/v3/topics/6040.json").prettyPeek()
                .then().statusCode(200)
                .extract().response();

        String name = response.path("topic.user.name");
        Integer uid = response.path("topic.user.id");

        System.out.println(name);
        System.out.println(uid);
        given().queryParam("q", name)
                .cookie("name", name)  //写到cookie中
                .cookie("uid", uid)
                .when().get("/search")
                .then().statusCode(200)
                .body(containsString(name));
    }

    //spec机制
    //复用通用的东西
    //创建自己的断言对象，不用每次接口都写同样的断言（状态码、时间、业务返回码）
    @Test
    public void testSpec(){
        //封装一个经典的结果断言
        ResponseSpecification rs = new ResponseSpecBuilder().build();
        rs.statusCode(200);
        rs.time(lessThan(1500L));
        rs.body(not(containsString("error")));
        given().get("https://testerhome.com/api/v3/topics/6040.json")
                .then().spec(rs);
    }

    //每次执行test后
    // 比如清除此次的用户信息
    @After
    public void after(){

    }

    @AfterClass
    public static void afterClass(){

    }

}