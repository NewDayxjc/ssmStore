package com.alidayu.controller;
/**
 * @Author xiongjinchen
 * @Date 2019/10/2 11:23
 * @Version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/2 11:23
 */
@RestController
public class MessageController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @RequestMapping("/sendMapMessage")
    public String messageProvider(Map<String,String> dateMap){
        dateMap.put("signName","鸿途招聘");
        dateMap.put("templateCode","SMS_151925499");
        dateMap.put("phoneNumbers","17379218491");
        dateMap.put("param","{\"code\":\""+(int)(Math.random()*10000)+"\"}");
        jmsMessagingTemplate.convertAndSend("message_list",dateMap);
        return "success";
    }
}
