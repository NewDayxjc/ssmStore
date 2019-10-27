package com;

import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.util.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/6 10:51
 */
public class WXPayTest {
    /**
     * 查询支付状态
     */
    public static void queryNative() throws Exception {
        //构建请求参数
        Map<String,String> dataMap=new HashMap<String, String>();
        dataMap.put("appid","wx8397f8696b538317");
        dataMap.put("mch_id","1473426802");
        dataMap.put("out_trade_no","123456789454891");
        dataMap.put("nonce_str",WXPayUtil.generateNonceStr());
        //获取签名
        String sign = WXPayUtil.generateSignature(dataMap, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb");
        dataMap.put("sign",sign);
        //调用微信接口
        HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        httpClient.setHttps(true);
        String parameter = WXPayUtil.mapToXml(dataMap);
        httpClient.setXmlParam(parameter);
        httpClient.post();
        String content = httpClient.getContent();
        System.out.println(content);

        //支付状态
        Map<String, String> map = WXPayUtil.xmlToMap(content);
        String trade_state = map.get("trade_state");
        System.out.println("交易状态是："+trade_state);

    }    /**
     * 创建二维码收钱url
     * @throws Exception
     */
    public static void createNative() throws Exception {
        Map<String,String> dataMap=new HashMap<String, String>();
        dataMap.put("appid","wx8397f8696b538317");//应用ID，微信公众账号或开放平台APP的唯一标识
        dataMap.put("mch_id","1473426802");//商户号
        dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串

        dataMap.put("body","微信支付测试");
        dataMap.put("out_trade_no","123456789454891"); //商户生成的订单号
        dataMap.put("total_fee","1");
        dataMap.put("spbill_create_ip","192.168.25.1");
        dataMap.put("notify_url","http://www.itcast.cn");
        dataMap.put("trade_type","NATIVE");

        String parameter = WXPayUtil.generateSignature(dataMap, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb");
        dataMap.put("sign",parameter);//TODO 签名
        //将map转成xml
        String mapToXml = WXPayUtil.mapToXml(dataMap);
        System.out.println("签名"+parameter);
        HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        httpClient.setHttps(true);
        httpClient.setXmlParam(mapToXml);

        httpClient.post();

        String content = httpClient.getContent();
        System.out.println(content);

        //将xml转成map
        Map<String, String> map = WXPayUtil.xmlToMap(content);
        String code_url = map.get("code_url");
        System.out.println(code_url);

    }

    public static void main(String[] args) throws Exception {
//        createNative();
        queryNative();
    }
}
