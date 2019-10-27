package com;
/**
 * @Author xiongjinchen
 * @Date 2019/10/6 8:34
 * @Version 1.0
 */

import com.pinyougou.util.HttpClient;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/6 8:34
 * 使用java访问网址
 */
public class HTTPClientTest {
    public static void main(String[] args) throws Exception {
        Map<String,String> map=new HashMap<String, String>();
        map.put("username","鉴于");
        map.put("phone","4561233");
        //参数：传入url
        HttpClient httpClient=new HttpClient("http://www.baidu.com");
        //是否启用https
        httpClient.setHttps(false);
        //xml
        httpClient.setXmlParam("");
        httpClient.post();
        String content = httpClient.getContent();
        System.out.println(content);
    }
}
