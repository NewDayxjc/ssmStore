package com.pinyougou.manager.service;
/**
 * @Author xiongjinchen
 * @Date 2019/10/1 13:29
 * @Version 1.0
 */

import com.pinyougou.mq.MessageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/1 13:29
 */
@Component
public class MessageSender {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination destination;


//    public MessageSender() {
//    }
//
//    public MessageSender(MessageInfo messageInfo) {
//        this.messageInfo = messageInfo;
//    }

    public void sendObjectMessage(final MessageInfo messageInfo){
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage();
                objectMessage.setObject(messageInfo);
                return objectMessage;  //TODO  取消dubbo依赖再进行测试
            }
        });
    }



}
