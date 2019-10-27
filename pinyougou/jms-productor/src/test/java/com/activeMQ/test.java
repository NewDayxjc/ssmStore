package com.activeMQ;

import com.activeMQ.productor.domain.User;
import com.activeMQ.productor.spring.springProducerDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/30 22:13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-MQ.xml")
public class test {

    @Autowired
    private springProducerDemo  springProducer;
    @Test
    public void test(){
        springProducer.sendTextMessage("你好吗？1000年后的远方");
    }

    @Test
    public void mapMessageTest(){
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("username","晓红");
        map.put("age","18");
        map.put("address","黑夜深林");
        springProducer.sendMapMessage(map);
    }
    @Test
    public void objectMessageTest(){
        User user=new User(12,"晓月",new Date());
        springProducer.sendObjectMessage(user);
    }
}
