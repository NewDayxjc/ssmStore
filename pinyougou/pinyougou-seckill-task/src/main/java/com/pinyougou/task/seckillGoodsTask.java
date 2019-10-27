package com.pinyougou.task;

import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.model.SeckillGoods;
import com.pinyougou.model.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * @author : xiongjinchen
 * @description: 任务调度 定时任务
 * @date :2019/10/7 10:55
 */
@Component
public class seckillGoodsTask {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    /**
     * @Scheduled 任务调度注解
     * cron：用来编写时间表达式
     *
     * 状态为审核通过
     * 开始时间=<当前时间=<结束时间
     * 库存数>0
     *若已存入redis，则当前商品不需要再次存入
     */
    @Scheduled(cron = "0/15 * * * * ?")
    public  void pushSeckill(){

        Example example=new Example(SeckillGoods.class);
        Example.Criteria criteria=example.createCriteria();
        //审核状态为通过
        criteria.andEqualTo("status","1");
        //库存数>0
        criteria.andGreaterThan("stockCount",0);
        //开始时间=<当前时间=<结束时间   select * from tb_seckill_goods where now() between start_time and end_time
        criteria.andCondition("NOW() BETWEEN start_time AND end_time");


        Set<Long> ids = redisTemplate.boundHashOps("goodsList").keys();
        if(ids!=null & ids.size()>=0) {
            criteria.andNotIn("id", ids);
        }

        //若已存入redis，则当前商品不需要再次存入 再次进行查询
        List<SeckillGoods> goodsList = seckillGoodsMapper.selectByExample(example);
        System.out.println((goodsList==null||goodsList.size()<=0)?"当前无商品":"共加入"+goodsList.size());
        for (SeckillGoods goods : goodsList) {
            redisTemplate.boundHashOps("goodsList").put(goods.getId(),goods);
            push(goods);
        }

    }
    //使用队列解决并发问题
    public void push(SeckillGoods seckillGoods){
        for (int i = 0; i <seckillGoods.getStockCount() ; i++) {
            redisTemplate.boundListOps("seckillGoodId_"+seckillGoods.getId()).leftPush(seckillGoods.getId());
        }

    }


}
