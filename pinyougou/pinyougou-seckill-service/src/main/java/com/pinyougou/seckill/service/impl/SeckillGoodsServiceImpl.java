package com.pinyougou.seckill.service.impl;
/**
 * @Author xiongjinchen
 * @Date 2019/10/7 13:53
 * @Version 1.0
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.model.SeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/7 13:53
 */
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    @Autowired
    private RedisTemplate redisTemplate;

    public SeckillGoods getOne(Long id) {
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("goodsList").get(id);
        return seckillGoods;
    }

    /**
     * 查询秒杀商品列表
     * @return
     */
    public List<SeckillGoods> list() {
        List<SeckillGoods> goodsList = redisTemplate.boundHashOps("goodsList").values();
        return goodsList;
    }
}
