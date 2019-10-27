package com.redis.test;
/**
 * @Author xiongjinchen
 * @Date 2019/9/23 20:31
 * @Version 1.0
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/23 20:31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-redis.xml")
public class ListTest {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 左压栈 leftPush  左出栈：先进先出
     * 左压栈 右出栈：先进后出
     * 右压栈 右出栈：先进先出
     * 右压栈 左出栈：先进后出
     */
    @Test
    public void testLeftPush(){
        //单条数据添加
        redisTemplate.boundListOps("username").leftPush("你好");
        //多条数据添加
        redisTemplate.boundListOps("username").leftPushAll("晓峰","禅悦","禅学");

    }

    /**
     * 左出栈：取出一个便移除一个
     */
    @Test
    public void testLeftPop(){
        Object username = redisTemplate.boundListOps("pass").leftPop();
        System.out.println(username);
    }

    /**
     * 右进栈：取出一个便移除一个
     */
    @Test
    public void testRightPush(){
        redisTemplate.boundListOps("pass").rightPush("天地人");
        redisTemplate.boundListOps("pass").rightPushAll("莫回首","再回首");

    }

    /**
     * 右出栈：取出一个便移除一个
     */
    @Test
    public void testRightPop(){
        Object username = redisTemplate.boundListOps("pass").rightPop();
        System.out.println(username);
    }


}
