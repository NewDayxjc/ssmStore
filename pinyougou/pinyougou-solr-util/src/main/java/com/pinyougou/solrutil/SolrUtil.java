package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.model.Item;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/25 7:45
 */
@Component
public  class SolrUtil {
    @Autowired
    private  ItemMapper itemMapper;


    @Autowired
    private SolrTemplate solrTemplate;

    public void Count(){

    }
    public  void batchInsert() {
        //查询状态为1的商品，select * from tb_item where status=1;
        Item item=new Item();
        item.setStatus("1");
        List<Item> items = itemMapper.select(item);
        //------------------新增start------------------
        //新增处：循环添加动态域

        for (Item itemm : items) {
            //将spec转换为map
            String spec = itemm.getSpec();
            Map<String,String> map = JSON.parseObject(spec, Map.class);
            //将其spec值循环设置到items中
            itemm.setSpecMap(map);
        }

        //导入索引库
        solrTemplate.saveBeans(items);

        //提交
        solrTemplate.commit();
    }
    public int deleteSolrDataAll() {
        Query query=new SimpleQuery("*:*");
        UpdateResponse del = solrTemplate.delete(query);

        int status = del.getStatus();
        solrTemplate.commit();
        return status;
    }
    /**
     * 第二种方式：条件查询，同时分页
     */
    public ScoredPage<Item> QueryByCondition(String filedName,String keywords){
        Query query=new SimpleQuery();
        //添加条件
//        Criteria criteria=new Criteria("item_title").is("华为");
        //item_title 在schema被设定为分词，contains会将其转为不分词
        Criteria criteria=new Criteria("item_spec_"+filedName).is(keywords);
        //将条件对象给query对象
        query.addCriteria(criteria);
        //设置分页条件
        query.setOffset(0);
        query.setRows(5);
        //查询
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        return items;
    }
}
