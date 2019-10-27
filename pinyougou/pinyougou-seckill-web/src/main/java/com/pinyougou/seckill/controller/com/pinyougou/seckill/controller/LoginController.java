package com.pinyougou.seckill.controller.com.pinyougou.seckill.controller;
/**
 * @Author xiongjinchen
 * @Date 2019/10/9 9:57
 * @Version 1.0
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/9 9:57
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @RequestMapping(value = "/loading")
    public String login(HttpServletRequest request){
        String url = request.getHeader("referer");
        if(StringUtils.isNotBlank(url)){
            return "redirect:"+url;
        }
        return "redirect:/seckill-index.html";
    }
}
