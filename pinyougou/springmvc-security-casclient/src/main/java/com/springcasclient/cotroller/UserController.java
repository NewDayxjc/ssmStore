package com.springcasclient.cotroller;
/**
 * @Author xiongjinchen
 * @Date 2019/10/3 19:38
 * @Version 1.0
 */

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/3 19:38
 */
@Controller
public class UserController {
    @RequestMapping(value = "/findLoginUser")
    public String findLoginUser(Model model){
        //获取用户登录名字
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("name",username);
        return "/index.jsp";

    }
}
