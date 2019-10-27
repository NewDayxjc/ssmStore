package com.redis.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author xiongjinchen
 * @Date 2019/9/23 21:01
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-redis.xml")
public class HashTest {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加数据测试
     * boundHashOps 有两层键
     *              namespace
     *                  id:key
     *                  同一键不能存相同的值，会覆盖
     */
    @Test
    public void testHashPut(){
        redisTemplate.boundHashOps("nameSpace").put("1","归来");
        redisTemplate.boundHashOps("nameSpace").put("1","幻魂");
        redisTemplate.boundHashOps("nameSpace").put("2","海王");
        redisTemplate.boundHashOps("nameSpace").put("3","往复");

    }

    /**
     * 测试获取数据
     */
    @Test
    public void testHashGet(){
        Object nameSpace = redisTemplate.boundHashOps("ItemCat").get("");
        System.out.println(nameSpace);
    }

    /**
     * 获取所有数据
     */
    @Test
    public void testHashGetAll(){
        List nameSpace1 = redisTemplate.boundHashOps("nameSpace").values();
        for (Object obj:
             nameSpace1) {
            System.out.println(obj);
        }
    }
    /**
     * 删除同namespace的单个值
     */
    @Test
    public void testDele(){
        Object namespace=redisTemplate.boundHashOps("nameSpace").delete("1");
        System.out.println(namespace);
    }

}
