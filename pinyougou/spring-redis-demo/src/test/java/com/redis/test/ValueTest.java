package com.redis.test;
/**
 * @Author xiongjinchen
 * @Date 2019/9/23 18:33
 * @Version 1.0
 */

import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/23 18:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-redis.xml")
public class ValueTest {
    @Autowired
    private RedisTemplate  redisTemplate;

    /**
     * 增加数据测试
     */
    @Test
    public void testSetValue(){
        redisTemplate.boundValueOps("username").set("消化");
    }
    /**
     * 查询数据测试
     */
    @Test
    public void testAdd(){
        Object username = redisTemplate.boundValueOps("username").get();
        System.out.println(username);
    }

    /**
     * 删除数据测试
     */
    @Test
    public void testRemove(){
        redisTemplate.delete("username");
    }
}
