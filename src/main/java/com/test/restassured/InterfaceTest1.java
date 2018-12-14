package com.test.restassured;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootApplication
public class InterfaceTest1 {

    //检验HTML页面是否正确的方法
    @Test
    public void testHtml(){
        //https请求时需要，降低浏览器的安全机制https的校验
        useRelaxedHTTPSValidation();
        //测试用例书写规范
        //given 代表请求的输入数据，填充body ——输入数据
        //when  执行的动作，触发条件        ——测试步骤
        //then  输出                     ——对结果做断言
        //prettyPeek                    ——打印基本输出
        given()
                .queryParam("q", "appium")
                .when()
                .get("https://testerhome.com/search").prettyPeek()
                .then()
                //.time() 断言网络请求时间
                .statusCode(200)  //通常作为第一个断言
                //.body() 2种语法 1.指定path路径 2.匹配的方式
                .body("html.head.title",equalTo("appium · 搜索结果 · TesterHome"));
    }

    @Test
    public void test_jsonPath() {
        Response response = get("http://172.20.95.70:8010/api/chart/list/6");
        Assert.assertEquals(200,response.getStatusCode());
        String str = response.asString();
        JsonPath jPath = new JsonPath(str);
        Assert.assertEquals(0, jPath.getInt("code"));
        Assert.assertEquals("未命名图表", jPath.get("data[1].title"));
    }

}