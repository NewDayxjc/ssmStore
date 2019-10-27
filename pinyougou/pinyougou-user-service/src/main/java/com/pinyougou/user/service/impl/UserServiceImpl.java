package com.pinyougou.user.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.UserMapper;
import com.pinyougou.model.User;
import com.pinyougou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination destination;
    @Value("${sign_name}")
    private String signName;
    @Value("${template_code}")
    private String templateCode;


    /**
     * 生成验证码
     * @param phone
     */
    @Override
    public void createCode(final String phone)  throws Exception{
        //生成随机码
         String code=String.valueOf((int) (Math.random()*1000000));

        //存到redis中
        redisTemplate.boundHashOps("MobileInfo").put(phone,code);
        //将数据转成JSON格式  TODO 为什么
        Map<String,String> dataMap=new HashMap<String, String>();
        dataMap.put("code",code);
         final String json = JSON.toJSONString(dataMap);
        sendMessage(phone, json);
    }

    @Override
    public int findByUserName(String username) {
        User user=new User();
        user.setUsername(username);
        return userMapper.selectCount(user);
    }

    /**
     * 验证码效验
     * 判断少数条件
     * @param phone
     * @param code
     * @return
     */
    @Override
    public Boolean check(String phone, String code) {
        String rawCode = (String) redisTemplate.boundHashOps("MobileInfo").get(phone);
        //判断少数条件
        //验证码为空
        if(rawCode==null){
            return false;
        }
        //验证码不匹配
        if(!code.equals(rawCode)){
            return false;
        }
        return true;
    }

    @Override
    public int findByPhone(String phone) {
        User user =new User();
        user.setPhone(phone);
        return userMapper.selectCount(user);
    }

    public void sendMessage(final String phone, final String json) throws Exception{
        //调用activeMQ发送短信
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                MapMessage message = session.createMapMessage();
                message.setString("signName",signName);
                message.setString("templateCode",templateCode);
                message.setString("phoneNumbers",phone);
                message.setString("param",json);
                return message;
            }
        });
    }

    @Override
    public User getUserInfoByUserName(String username) {
        User user =new User();
        user.setUsername(username);

        return userMapper.selectOne(user);
    }

    /***
     * 增加User信息
     * @param user
     * @return
     */
    @Override
    public int add(User user) {
        Date now=new Date();
        user.setCreated(now);
        user.setUpdated(now);
        //用md5进行加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        int account= userMapper.insertSelective(user);
        if(account>0){
            redisTemplate.boundHashOps("MobileInfo").delete(user.getPhone());
        }
        return account;
    }




}

