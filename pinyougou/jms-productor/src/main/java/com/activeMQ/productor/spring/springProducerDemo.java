package com.activeMQ.productor.spring;

import com.activeMQ.productor.domain.User;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;
import javax.xml.soap.Text;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/30 21:58
 */
@Component
public class springProducerDemo {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination destination;

    /**
     * 发送文本消息
     * @param text
     */
    public void sendTextMessage(final String text){
        //发送消息方
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();

                message.setText(text);
                return message;
            }
        });

    }

    /**
     * 发送mapMessage类型
     */
    public void sendMapMessage(final Map<String,Object> dataMap){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage =  session.createMapMessage();
                mapMessage.setObject("userInfo",dataMap);

                return mapMessage;
            }
        });

    }

    /**
     * 发送ObjectMessage类型消息
     */
    public  void sendObjectMessage(final User user){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage();
                objectMessage.setObject(user);
                return objectMessage;
            }
        });
    }
}
