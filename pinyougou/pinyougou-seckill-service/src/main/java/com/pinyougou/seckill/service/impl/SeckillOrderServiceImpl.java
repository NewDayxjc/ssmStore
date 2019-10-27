package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.mapper.SeckillOrderMapper;
import com.pinyougou.model.SeckillGoods;
import com.pinyougou.model.SeckillOrder;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description:
 * @date :2019/10/7 22:54
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;


    public void add(String name, Long id) {

        //获取对应的秒杀商品
//        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps("goodsList").get(id);
        //获取对应的队列
        Object seckillGoods =  redisTemplate.boundListOps("seckillGoodId_" + id).rightPop();

        if(seckillGoods==null){
            throw new RuntimeException("商品售罄");
        }

        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SeckillGoods goods = (SeckillGoods) redisTemplate.boundHashOps("goodsList").get(id);

//        System.out.println("抢购的用户是："+name+",库存有"+goods.getStockCount());
        //生成订单
        SeckillOrder order=new SeckillOrder();
        order.setId(idWorker.nextId());
        order.setSeckillId(id);
        order.setSellerId(goods.getSellerId());
        order.setUserId(name);
        order.setStatus("0");
        order.setMoney(goods.getCostPrice());
        order.setCreateTime(new Date());

        //获取订单信息,并将其放入redis中
        redisTemplate.boundHashOps("goodsOrder").put(name,order);

        //如果订单提交，库存-1
        goods.setStockCount(goods.getStockCount()-1);
        //如果库存<=0,清空秒杀商品redis数据，并将数据同步到数据库 更改销量
        if(goods.getStockCount()<=0){
            seckillGoodsMapper.updateByPrimaryKeySelective(goods);
            redisTemplate.boundHashOps("goodsList").delete(id);
        }else{
            //修改redis中的库存状态
            redisTemplate.boundHashOps("goodsList").put(id,goods);
        }
    }

    /**
     * 根据用户名获取秒杀订单信息
     * @param username
     * @return
     */
    public SeckillOrder getOrderByusername(String username) {
        return (SeckillOrder) redisTemplate.boundHashOps("goodsOrder").get(username);
    }

    public void insertOrder(String username, String transaction_id) {
        try {
            //获取订单信息
            SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("goodsOrder").get(username);
            if(seckillOrder!=null) {
                //将订单数据存进数据库
                //更新订单状态
                seckillOrder.setStatus("2");
                seckillOrderMapper.insertSelective(seckillOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOrderStatus(String username, String transaction_id) {
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("goodsOrder").get(username);
        seckillGoods.setStatus("2");
        seckillGoodsMapper.insertSelective(seckillGoods);
    }

    public void removeOrder(String username) {
        //查询用户订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("goodsOrder").get(username);
        if(seckillOrder!=null){
            //数据回滚
//        SeckillGoods seckillGoods = seckillGoodsMapper.selectByPrimaryKey(username);
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("goodsList").get(seckillOrder.getSeckillId());
            if(seckillGoods==null){
                seckillGoods=seckillGoodsMapper.selectByPrimaryKey(username);
            }
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
            //添加进redis中
            redisTemplate.boundHashOps("goodsList").put(seckillGoods.getId(),seckillGoods);

            redisTemplate.boundHashOps("goodsOrder").delete(username);
        }

    }
}
