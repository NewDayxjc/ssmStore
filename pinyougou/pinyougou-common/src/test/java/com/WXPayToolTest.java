package com;
/**
 * @Author xiongjinchen
 * @Date 2019/10/6 7:37
 * @Version 1.0
 */

import com.github.wxpay.sdk.WXPayUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/6 7:37
 */
public class WXPayToolTest {
    public static void main(String[] args) throws Exception{
        String str = WXPayUtil.generateNonceStr();
        System.out.println(str);
        //map  to  xml
        Map<String,String> map=new HashMap<String, String>();
        map.put("username","班生门");
        map.put("age","13");
        map.put("address","中南海");
        String xml = WXPayUtil.mapToXml(map);
        System.out.println("map转xml"+xml);
        //xml  to map
        Map<String, String> xmlToMap = WXPayUtil.xmlToMap(xml);
        System.out.println("xml to map" + xmlToMap);
        //map 转xml 带签名
        String abcdefg = WXPayUtil.generateSignature(map, "ABCDEFG");
        System.out.println("map转xml带签名"+abcdefg);

    }

}
