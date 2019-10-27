package com.pinyougou.seckill.service;

import com.pinyougou.model.SeckillGoods;

import java.util.List;

/**
 * @Author xiongjinchen
 * @Date 2019/10/7 13:51
 * @Version 1.0
 */
public interface SeckillGoodsService {


    /**
     * 查询单个秒杀商品列表
     * @param id
     * @return
     */
    SeckillGoods getOne(Long id);
    /**
     * 查询秒杀商品列表
     * @return
     */
    public List<SeckillGoods> list();
}
