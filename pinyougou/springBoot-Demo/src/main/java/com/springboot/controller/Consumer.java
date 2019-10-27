package com.springboot.controller;
/**
 * @Author xiongjinchen
 * @Date 2019/10/2 8:09
 * @Version 1.0
 */

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/2 8:09
 */
@Component
public class Consumer {
    /**
     * JmsListener :消息监听
     * destination：队列地址
     * @param text
     */
    @JmsListener(destination = "text")
    public void readMessage(String text){
        System.out.println("读到的消息："+text);
    }
    @JmsListener(destination = "map_text")
    public void readMapMessage(Map map){
        System.out.println("读到的map消息是"+map);
    }
}
