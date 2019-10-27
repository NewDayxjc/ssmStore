package com.activeMQ.consumer;
/**
 * @Author xiongjinchen
 * @Date 2019/9/30 18:10
 * @Version 1.0
 */

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/30 18:10
 * 点对点模式
 */
public class QueueConsumerdemo {
    //监听模式
    public static void main(String[] args) throws JMSException, IOException {
        //创建工厂对象
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.131:61616");
        //创建连接对象
        Connection connection = connectionFactory.createConnection();
        //开启
        connection.start();
        //创建回话对象session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建需要接收到队列地址
        Queue queue = session.createQueue("test");
        //创建消费
        MessageConsumer consumer = session.createConsumer(queue);
        //监听模式相当于一个子线程 相当于创建了一个MessageListener
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                while (true){
                    if(message!=null){
                        if(message instanceof TextMessage){
                            TextMessage textMessage= (TextMessage) message;
                            try {
                                //textMessage 获取全部信息
                                String text = textMessage.getText();
                                System.out.println("监听模式接收到的数据是"+text);
                                break;
                            } catch (JMSException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }

            }
        });

//  第一种方式      Thread.sleep(10000L);
        //第二种方式
        System.in.read();
        session.close();
        connection.close();
    }

    public static void main_bak(String[] args) throws JMSException {
        //创建工厂对象
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.131:61616");
        //创建连接对象
        Connection connection = connectionFactory.createConnection();
        //开启
        connection.start();
        //创建回话对象session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建需要接收到队列地址
        Queue queue = session.createQueue("test");
        //创建消费
        MessageConsumer consumer = session.createConsumer(queue);
        while(true) {
            Message message = consumer.receive(10000);
            if (message != null) {
                if (message instanceof TextMessage) {
                    //将message强转成TextMessage
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("接收到的消息是" + textMessage.getText());
                    break;
                }
            }
        }
        session.close();
        connection.close();
    }
}
