package com.pinyougou.test;
/**
 * @Author xiongjinchen
 * @Date 2019/9/25 9:42
 * @Version 1.0
 */

import com.pinyougou.model.Item;
import com.pinyougou.solrutil.SolrUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.query.result.ScoredPage;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/25 9:42
 */
public class SolrUtilTest {

    @Autowired
    private SolrUtil solrUtil;
    @Before
    public void init(){
        //获取spring容器
        ApplicationContext act= new  ClassPathXmlApplicationContext("classpath:spring-solr.xml");
        //获取solr的实例
        this.solrUtil = act.getBean(SolrUtil.class);
    }

    /**
     * 数据批量导入
     */

    @Test
    public void test(){
        solrUtil.batchInsert();
    }


    @Test
    public void testSelect(){
        ScoredPage<Item> items = solrUtil.QueryByCondition("网络", "联通2G");
        for (Item item : items) {
            System.out.println(item);
        }
    }
    @Test
    public void testDeleteAll(){
        int count = solrUtil.deleteSolrDataAll();
        System.out.println(count);
    }

}
