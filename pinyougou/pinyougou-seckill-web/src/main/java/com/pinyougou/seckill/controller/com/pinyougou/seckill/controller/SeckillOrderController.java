package com.pinyougou.seckill.controller.com.pinyougou.seckill.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.http.Result;
import com.pinyougou.model.SeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pinyougou.seckill.service.SeckillOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/7 14:53
 */
@RestController
@RequestMapping(value = "/seckill/order" )
public class SeckillOrderController {
    @Reference
    private SeckillOrderService seckillOrderService;
    @RequestMapping(value ="/add")
    public Result add(Long id){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            if("anonymousUser".equals(name)){
                return new Result(false,"403");
            }
            seckillOrderService.add(name,id);
            return  new Result(true,"抢单成功");
        } catch (Exception e) {
            return  new Result(false,"下单失败");
        }
    }
//    @RequestMapping(value = "")
//    public Result closeOrder(String ){
//
//    }


}
