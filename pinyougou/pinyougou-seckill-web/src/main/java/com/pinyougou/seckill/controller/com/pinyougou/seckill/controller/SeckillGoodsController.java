package com.pinyougou.seckill.controller.com.pinyougou.seckill.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.model.SeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/7 14:53
 */
@RestController
@RequestMapping(value = "/seckill/goods" )
public class SeckillGoodsController {
    @Reference
    private SeckillGoodsService seckillGoodsService;

    @RequestMapping(value = "/one")
    public SeckillGoods getOne(Long id){
        SeckillGoods seckillGoods = seckillGoodsService.getOne(id);
        return  seckillGoods;
    }

    /**
     * 列表查询
     * @return
     */
    @RequestMapping(value = "/list")
    public List<SeckillGoods> list(){
        List<SeckillGoods> list = seckillGoodsService.list();
        return  list;
    }
}
