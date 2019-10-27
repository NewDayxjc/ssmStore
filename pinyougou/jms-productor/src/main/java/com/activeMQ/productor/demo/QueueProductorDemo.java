package com.activeMQ.productor.demo; /**
 * @Author xiongjinchen
 * @Date 2019/9/30 16:12
 * @Version 1.0
 */

import org.apache.activemq.ActiveMQConnectionFactory;
import org.omg.CORBA.INVALID_ACTIVITY;

import javax.jms.*;
import java.util.Random;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/30 16:12
 * 点对点模式
 */
public class QueueProductorDemo {
    public static void main(String[] args) throws JMSException {
        //创建会话工厂对象ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.131:61616");
        //创建连接对象connection
        Connection connection = connectionFactory.createConnection();
        //开启
        connection.start();
        //获得回话对象
        /**
         * 第1个参数 是否使用事务
         * 第2个参数 消息的确认模式
         * · AUTO_ACKNOWLEDGE = 1  自动确认
         * · CLIENT_ACKNOWLEDGE = 2 客户端应答 客户端手动确认  a:不会有重复数据
         * · DUPS_OK_ACKNOWLEDGE = 3 客户端应答自动批量确认   b:容易产生重复数据
         * · SESSION_TRANSACTED = 0 事务提交并确认
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建消息
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText("你好，我是activeMQ"+ Math.random()*10000);
        //指定消息发送地址和路径
        Queue test = session.createQueue("test");
        //创建消息发送对象
        MessageProducer producer = session.createProducer(test);
        //发送消息
        producer.send(textMessage);
        //关闭资源
        session.close();
        connection.close();

    }
}
