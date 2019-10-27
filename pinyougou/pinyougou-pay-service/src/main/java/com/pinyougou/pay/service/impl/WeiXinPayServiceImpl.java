package com.pinyougou.pay.service.impl;
/**
 * @Author xiongjinchen
 * @Date 2019/10/6 14:44
 * @Version 1.0
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description:
 * @date :2019/10/6 14:44
 */
@Service
public class WeiXinPayServiceImpl implements WeiXinPayService {
    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;//商户账号
    @Value("${partnerkey}")
    private String partnerkey;//商户秘钥
    @Value("${notifyurl}")
    private String notifyurl;  //回调地址

    public Map createNative(String out_trade_no, String total_fee) {
        try {
            //封装参数
            Map<String,String> dataMap=new HashMap<String, String>();
            dataMap.put("appid",appid);//应用ID，微信公众账号或开放平台APP的唯一标识
            dataMap.put("mch_id",partner);//商户号
            dataMap.put("nonce_str", WXPayUtil.generateNonceStr()); //随机字符串

            dataMap.put("body","微信支付测试");
            dataMap.put("out_trade_no",out_trade_no); //商户生成的订单号
            dataMap.put("total_fee",total_fee);
            dataMap.put("spbill_create_ip","192.168.25.1");
            dataMap.put("notify_url",notifyurl);
            dataMap.put("trade_type","NATIVE");

            String parameter = WXPayUtil.generateSignature(dataMap, partnerkey);
            dataMap.put("sign",parameter);//TODO 签名
            //将map转成xml
            String mapToXml = WXPayUtil.mapToXml(dataMap);
//            System.out.println("签名"+parameter);
            //执行请求
            HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(mapToXml);

            httpClient.post();

            String content = httpClient.getContent();
            System.out.println(content);

            //将xml转成map
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            String code_url = map.get("code_url");


            //获取参数  传给前台
            Map<String,String> ternMap=new HashMap<String, String>();
            ternMap.put("code_url",code_url);
            ternMap.put("outTradeNo",out_trade_no);
            ternMap.put("total_fee",total_fee);
            return ternMap;
//            System.out.println(code_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map queryNative(String out_trade_no) {
        try {
            //构建请求参数
            Map<String,String> dataMap=new HashMap<String, String>();
            dataMap.put("appid",appid);
            dataMap.put("mch_id",partner);
            dataMap.put("out_trade_no",out_trade_no);
            dataMap.put("nonce_str",WXPayUtil.generateNonceStr());
            //获取签名
            String sign = WXPayUtil.generateSignature(dataMap, partnerkey);
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

//            Map<String,String>  newMap=new HashMap<String, String>();
//            newMap.put("trade_state",trade_state);
            System.out.println("交易状态是："+trade_state);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map removeOrder(String out_trade_no) throws Exception {
        try {
            //构建请求参数
            Map<String,String> dataMap=new HashMap<String, String>();
            dataMap.put("appid",appid);
            dataMap.put("mch_id",partner);
            dataMap.put("out_trade_no",out_trade_no);
            dataMap.put("nonce_str",WXPayUtil.generateNonceStr());
            //获取签名
            String sign = WXPayUtil.generateSignature(dataMap, partnerkey);
            dataMap.put("sign",sign);
            //调用微信接口
            HttpClient httpClient=new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
            httpClient.setHttps(true);
            String parameter = WXPayUtil.mapToXml(dataMap);
            httpClient.setXmlParam(parameter);
            httpClient.post();
            String content = httpClient.getContent();
            //支付状态
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            return  map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
