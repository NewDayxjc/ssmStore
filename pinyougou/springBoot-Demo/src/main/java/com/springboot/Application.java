package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/2 7:33
 * 默认从当前引导类所在的包开始扫描:如：从com.springboot
 */

@SpringBootApplication
public class Application {
    //指定引导类,使其从引导类开始扫描
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
