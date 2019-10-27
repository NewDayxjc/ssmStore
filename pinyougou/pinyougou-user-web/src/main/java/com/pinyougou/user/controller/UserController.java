package com.pinyougou.user.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.http.Result;
import com.pinyougou.model.User;
import com.pinyougou.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping(value = "/login/name")
    public Map<String,String> getUserName(){
        Map<String,String> dataMap=new HashMap<String, String>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        dataMap.put("username",username);
        return dataMap;
    }
    /**
     * 生成验证码
     * @param phone
     * @return
     */
    @RequestMapping(value = "/createCode")
    public Result createCode(String phone){
        try {
            userService.createCode(phone);
            return new Result(true,"验证码发送成功");
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false,"验证码发送失败");
    }

    /***
     * 增加User数据
     * @param user
     * 响应数据：success
     *                  true:成功  false：失败
     *           message
     *                  响应的消息
     *
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Result add(@RequestBody User user,String code){
        try {
          Boolean flag=  userService.check(user.getPhone(),code);
            if(!flag){
                return new Result(false,"输入验证码不正确");
            }
            int userResult=userService.findByUserName(user.getUsername());
            if(userResult>0){
                return new Result(false,"用户名已被注册");
            }

            int phoneResult=userService.findByPhone(user.getPhone());
            if(phoneResult>0){
                return new Result(false,"手机号已被注册");
            }
            //执行增加
            int acount = userService.add(user);

            if(acount>0){
                //增加成功
               return new Result(true,"增加成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"增加失败");
    }

}
