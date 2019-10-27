package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.http.Result;
import com.pinyougou.model.Order;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/5 15:47
 */
@RequestMapping(value = "/order")
@RestController
public class OrderController {
    @Reference
    private OrderService orderService;

    @RequestMapping(value = "/add")
    public Result add(@RequestBody Order order){
        try {
            if (order != null) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                order.setUserId(username);
                //设置订单时间
                order.setCreateTime(new Date());
                //设置订单更新时间
                order.setUpdateTime(order.getCreateTime());
                //设置订单状态
                order.setStatus("1");
                int acount = orderService.add(order);
                if (acount > 0) {
                    return new Result(true, "订单提交成功");
                } else {
                    return new Result(false, "订单提交失败");
                }

            } else {
                return new Result(false, "订单数据为空");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new Result(false, "系统繁忙!订单提交失败");
    }

}
