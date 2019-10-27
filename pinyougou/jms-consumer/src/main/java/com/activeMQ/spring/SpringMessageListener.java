package com.activeMQ.spring;


import com.activeMQ.productor.domain.User;
import com.pinyougou.model.Item;
import com.pinyougou.mq.MessageInfo;
import org.apache.activemq.LocalTransactionEventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.List;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/30 22:40
 */
@Component

public class SpringMessageListener implements MessageListener {


    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String text = textMessage.getText();
                System.out.println("接收到的消息是:" + text);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;
            try {
                Map<String, String> userInfo = (Map<String, String>) mapMessage.getObject("userInfo");
                System.out.println("接收到的消息是:" + userInfo);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        if (message instanceof ObjectMessage) {

            try {
                //try-catch并强转
                ObjectMessage objectMessage = (ObjectMessage) message;
//                    MessageInfo messageInfo= (MessageInfo) objectMessage.getObject();
//                    List<Item>  items = (List<Item>) messageInfo.getContext();

                User user = (User) objectMessage.getObject();
                System.out.println("ObjectMessage接收到的消息是：" + user);

            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}


