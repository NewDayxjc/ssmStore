package com.springboot.controller;
/**
 * @Author xiongjinchen
 * @Date 2019/10/2 7:36
 * @Version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/2 7:36
 */
@RestController

public class TestController {
    @Autowired
    private Environment environment;
    @RequestMapping(value = "/hello")
    public String  hello(){
        //获取配置文件信息
        System.out.println("Nation How are you"+environment.getProperty("url"));
        return "Nation How are you "+environment.getProperty("url")+"热署";
    }
}
