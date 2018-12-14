package com.test.restassured;

import io.restassured.response.Response;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;

public class InterfaceTest2 {

    //多个test方法可以同时进行
    @Test
    public void getTest() {
        //获取URL的返回信息
        //get("http://10.25.90.209:8010/api/dashboard/getalldashboarddir").prettyPeek();

        //第一种情况
        //http://10.25.90.209:8010/api/permissionOperation/getPermissionInfoOfUser?userId=228
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", 228);
        //given().params(map).get("http://10.25.90.209:8010/api/permissionOperation/getPermissionInfoOfUser").prettyPeek();

        //第二种情况
        //http://10.25.90.209:8010/dashboard/10
//		Map<String,Object> map1 = new HashMap<String,Object>();
//		map1.put("dashboard", "dashboard");
//		map1.put("dashboardId", 10);
        //get("http://10.25.90.209:8010/{dashboard}/{dashboardId}",map1).prettyPeek();
        //第二种方式
        //get("http://10.25.90.209:8010/{dashboard}/{dashboardId}","dashboard",10).prettyPeek();
    }

    @Test
    public void postTest() {

        //第一种方式
        //given().param("id", 203).param("roleId", 4).param("comment","大数据研发中").param("tel","18829348888")
        //.post("http:/10.25.90.209:8010/api/user/update/").prettyPeek();

        //第二种方式(建议使用)
        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("id", 203);
        map2.put("roleId",4);
        map2.put("comment", "大数据研发中");
        map2.put("tel","18829348888");
        //given().params(map2).post("http://10.25.90.209:8010/api/user/update/").prettyPeek();

        //第三种方式  参数以body体形式进行传值（只适用于POST）
        //given().body("{\"id\": 203}").post("http://10.25.90.209:8010/api/user/update/").prettyPeek();

        //包含文件的请求
        File file = new File("d:/a.txt");
        //given().body(file).post("http://10.25.90.209:8010/api/user/update/").prettyPeek();

        //校验header、cookie判断接口是否是正常的请求
        //接口需要登录以后带着登录信息请求,从cookie中取
        //将登陆信息写入cookie传给后台
        //given().cookie("userName","123456").get("http://10.25.90.209:8010/api/user/update/");
        //given().header("userName","xxxxxx").get("http://10.25.90.209:8010/api/user/update/");

        //接口请求时对中文处理，进行	url编码，存在特殊字符情况很重要，
        //可能存在属性传递异常的情况
        //需要与服务端达成一致，因为服务端还需要解码
        //given().urlEncodingEnabled(true).param("us	erName","张三").get("http://10.25.90.209:8010/api/permissionOperation/getPermissionInfoOfUser");

        //接口上传文件，传递文件流
        //given().multiPart(file).post("http://10.25.90.209:8010/api/user/update/");

    }

    /*
     * jsonpath数据解析        还有xmlpath
     */
    @Test
    public void jsonpathTest() {
        Response response = get("https://testerhome.com/api/v3/topics.json");
        List<Object> list = response.jsonPath().getList("topics");//查看接口文档得到key值
        System.out.println(list.size());
        //通过这种方式可以验证接口返回的接送数据是否正常  for循环
        System.out.println(response.jsonPath().getString("topics[0].id"));
        List<String> titleList = response.jsonPath().getList("topics.title");
        System.out.println("===" + titleList);


    }

}