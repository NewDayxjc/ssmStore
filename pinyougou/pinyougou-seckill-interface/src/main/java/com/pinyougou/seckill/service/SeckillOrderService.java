package com.pinyougou.seckill.service;

import com.pinyougou.model.SeckillOrder;

import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/7 22:50
 */
public interface SeckillOrderService {
    /**
     * 添加订单
     * @param name
     * @param id
     */
     void add(String name,Long id);

    /**
     * 获取秒杀订单信息
     * @param username
     */
    SeckillOrder getOrderByusername(String username);

    void insertOrder(String username, String transaction_id);


    void updateOrderStatus(String username, String transaction_id);

    void removeOrder(String username);
}
