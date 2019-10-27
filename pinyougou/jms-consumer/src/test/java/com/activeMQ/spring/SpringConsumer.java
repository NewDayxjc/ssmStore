package com.activeMQ.spring;
/**
 * @Author xiongjinchen
 * @Date 2019/9/30 22:54
 * @Version 1.0
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/30 22:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-MQ.xml")
public class SpringConsumer {
    //该类在其实体类中已经注入到spring容器中，故这里不再需要注入
//    private  SpringMessageListener springMessageListener;
    @Test
    public void text() throws IOException {
        //这里不让监听器停下来
        System.in.read();
    }
}
