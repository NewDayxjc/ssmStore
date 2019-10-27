package com.redis.test;
/**
 * @Author xiongjinchen
 * @Date 2019/9/23 20:02
 * @Version 1.0
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/23 20:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-redis.xml")
public class SetTest {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加数据测试
     * 一个键不可存储相同的值
     */
    @Test
    public void testSetAdd(){
        redisTemplate.boundSetOps("name").add("小红");
        redisTemplate.boundSetOps("name").add("小红");
        redisTemplate.boundSetOps("name").add("小黑");
    }

    /**
     * 批量查询数据
     */
    @Test
    public void testSetGet(){
        Set name1 = redisTemplate.boundSetOps("name").members();
        for (Object set:
             name1) {
            System.out.println(set);
        }
    }

    /**
     * 删除数据测试
     */
    @Test
    public void testSetDel(){
        redisTemplate.boundSetOps("name").remove("小红");

    }

}
