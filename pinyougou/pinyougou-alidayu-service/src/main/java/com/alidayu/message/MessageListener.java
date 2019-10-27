package com.alidayu.message;
/**
 * @Author xiongjinchen
 * @Date 2019/10/2 11:11
 * @Version 1.0
 */

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/2 11:11
 */
@Component
public class MessageListener {
    @Autowired
    private JmsMessageSender messageSender;

    /**
     * 接收消息
     * @param dataMap
     * @throws ClientException
     */
    @JmsListener(destination = "message")
    public void sendMessage(Map<String,String> dataMap) throws ClientException {
        SendSmsResponse smsResponse=messageSender.sendSms(dataMap.get("signName"),
                dataMap.get("templateCode"),
                dataMap.get("phoneNumbers"),
                dataMap.get("param"));
        System.out.println("接收到的消息是："+smsResponse.getMessage());
    }

}
