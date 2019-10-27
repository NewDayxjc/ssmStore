package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("/name")
    public Map name(){
        //getAuthentication：获取认证对象    getContext：获取上下文对象
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("loginName",name);
        return map;
    }
}
