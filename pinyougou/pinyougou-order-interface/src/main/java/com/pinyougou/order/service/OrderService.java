package com.pinyougou.order.service;
/**
 * @Author xiongjinchen
 * @Date 2019/10/5 17:10
 * @Version 1.0
 */

import com.github.pagehelper.PageInfo;
import com.pinyougou.model.Order;
import com.pinyougou.model.PayLog;

import java.util.List;

/**
 * @author : xiongjinchen
 * @description:
 * @date :2019/10/5 17:10
 */
public interface OrderService {
    public void updateOrderAndPayLogStatus( String out_trade_no, String transaction_id);
    /**
     * 返回Order全部列表
     * @return
     */
    public List<Order> getAll();

    /***
     * 分页返回Order列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Order> getAll(Order order, int pageNum, int pageSize);

    /**
     * 查询用户日志
     * @param username
     * @return
     */
    PayLog  searchPayLogFromRedis(String username);

    /***
     * 增加Order信息
     * @param order
     * @return
     */
    int add(Order order);

    /***
     * 根据ID查询Order信息
     * @param id
     * @return
     */
    Order getOneById(Long id);

    /***
     * 根据ID修改Order信息
     * @param order
     * @return
     */
    int updateOrderById(Order order);

    /***
     * 根据ID批量删除Order信息
     * @param ids
     * @return
     */
    int deleteByIds(List<Long> ids);
}
