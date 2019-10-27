package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.http.Result;
import com.pinyougou.model.Cart;
import com.pinyougou.model.Order;
import com.pinyougou.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pinyougou.cart.service.CartService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/4 11:21
 */
@RestController
@RequestMapping(value = "/cart")
public class CartController {
    @Reference
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    /**
     * 将商品添加到购物车
     * @param itemId
     * @param num
     * allowCredentials = "true":表示是否允许跨域请求向该工程发送Cookie数据
     */
    @RequestMapping(value = "/add")
    @CrossOrigin(origins = "http://localhost:18088",allowCredentials = "true")
    public Result add(Long itemId, Integer num){
        try {
            //解决跨域问题
//            response.setHeader("Access-Control-Allow-Origin", "http://localhost:18088");//表示允许哪个域名跨域请求通过
//            response.setHeader("Access-Control-Allow-Credentials", "true");//表示是否允许跨域请求向该工程发送Cookie数据

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(username);
            List<Cart> carts = list();


            if (carts == null) {
                carts = new ArrayList<Cart>();
            }

            if (username.equals("anonymousUser")) {
                carts = cartService.add(carts, itemId, num);
                String jsonCarts = JSON.toJSONString(carts);  //购物车数据是JSON格式，需要转成String类型
                CookieUtil.setCookie(request, response, "CartList", jsonCarts, 3600 * 24, "UTF-8");

                return new Result(true, "商品成功添加到购物车");
            } else {
                carts=cartService.add(carts,itemId,num);
                cartService.saveRedis(carts, username);

                return new Result(true, "成功添加到个人购物车");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new Result(false,"添加失败");
    }

    /**
     * 获取cookie的数据
     * @return
     */
    @RequestMapping(value = "/list")
    public List<Cart>  list(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String cookieName = CookieUtil.getCookieValue(request, "CartList", "UTF-8");
        List<Cart> cookieCarts = JSON.parseArray(cookieName, Cart.class);
        if(cookieCarts==null){
            cookieCarts=new ArrayList<Cart>();
        }
        if("anonymousUser".equals(username)){
            return cookieCarts;
        }else{
            //从redis获取数据
                List<Cart> byRedis = cartService.findByRedis(username);
            if(cookieCarts!=null &cookieCarts.size()>0) {
                //合并
                List<Cart> carts = cartService.mergeCartList(byRedis, cookieCarts);
                //清除cookie
                CookieUtil.deleteCookie(request, response, "CartList");
                //重新存进redis
                cartService.saveRedis(carts, username);
            }
                return byRedis;

        }

    }
}
