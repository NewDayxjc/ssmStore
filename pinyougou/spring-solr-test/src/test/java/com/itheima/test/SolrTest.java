package com.itheima.test;
/**
 * @Author xiongjinchen
 * @Date 2019/9/24 20:30
 * @Version 1.0
 */

import com.itheima.domain.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/24 20:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:spring-solr.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 第二种方式：条件查询，同时分页
     */
    @Test
    public void testQueryByCondition(){
        Query query=new SimpleQuery();
        //添加条件
//        Criteria criteria=new Criteria("item_title").is("华为");
        //item_title 在schema被设定为分词，contains会将其转为不分词
        Criteria criteria=new Criteria("item_title").contains("华为");
        //将条件对象给query对象
        query.addCriteria(criteria);
        //设置分页条件
        query.setOffset(0);
        query.setRows(5);
        //查询
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        for (Item item : items) {
            System.out.println(item);
        }
    }
    /***
     * 批量增加数据
     */
    @Test
    public void testBatchAdd(){
        List<Item> items=new ArrayList<Item>();
        for (int i = 0; i <10 ; i++) {
            Item item = new Item();
            item.setId(1L+(int)(Math.random()*100));
            item.setBrand("华为"+(int)(Math.random()*100));
            item.setCategory("手机"+(int)(Math.random()*100));
            item.setGoodsId(1L);
            item.setSeller("华为2号专卖店"+(int)(Math.random()*100));
            item.setTitle("华为Mate9"+(int)(Math.random()*100));
            item.setPrice(new BigDecimal(20+(int)(Math.random()*100)));
            items.add(item);
        }


        //执行保存操作
        solrTemplate.saveBeans(items);

        //提交
        solrTemplate.commit();
    }

    /**
     * 条件查询，同时分页
     */
    @Test
    public void testPage(){
        //创建Query分页条件
        Query query = new SimpleQuery("item_title:小米");
        //指定分页参数
        query.setOffset(1);
        query.setRows(5);
        //执行分页搜索
        //Query:搜索条件的封装
        //Item.class：将查询到的结果集需要转换为item
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        for (Item item : items) {
            System.out.println(item);
        }

    }

    /**
     * 分页查询
     */
    @Test
    public void testQueryPage(){
       //创建Query分页条件
        Query query = new SimpleQuery("*:*");
        //指定分页参数
        query.setOffset(1);
        query.setRows(5);
        //执行分页搜索
        //Query:搜索条件的封装
        //Item.class：将查询到的结果集需要转换为item
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);


//        for (Item item:items
//             ) {
//            System.out.println(item);
//        }
        //第二种方式
        //获取结果集
        List<Item> cotent = items.getContent();
        //获取总记录数
        long totalElements = items.getTotalElements();
        System.out.println("总记录数为"+totalElements);

        for (Item item : cotent) {
            System.out.println(item);
        }
    }
    /***
     * 增加数据测试
     */
    @Test
    public void testAdd(){
        Item item = new Item();
        item.setId(222L);
        item.setBrand("小米");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("小米2号专卖店");
        item.setTitle("小米手机");
        item.setPrice(new BigDecimal(2000));

        //执行保存操作
        solrTemplate.saveBean(item);

        //提交
        solrTemplate.commit();
    }
    @Test
    public void testQuery(){
        Item item = solrTemplate.getById(1L, Item.class);
        System.out.println(item);
    }

    /**
     * 删除单条数据
     */
    @Test
    public void testDeleteOne(){
        Query query = new SimpleQuery("id:127");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    /**
     * 分页查询
     */



    @Test
    public void testDeleteAll(){
        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


}