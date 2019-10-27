package com.springboot.controller;
/**
 * @Author xiongjinchen
 * @Date 2019/10/2 8:09
 * @Version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/2 8:09
 */
@RestController
public class QueueController {
    /**
     *  JmsMessagingTemplate 用来发送消息
     */
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @RequestMapping("/send")
    public void send(String text){
        jmsMessagingTemplate.convertAndSend("text",text);
    }
    @RequestMapping("/sendMap")
    public void sendMap(){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("username","暮霭");
        map.put("phone","45468");
        jmsMessagingTemplate.convertAndSend("map_text",map);
    }
}
