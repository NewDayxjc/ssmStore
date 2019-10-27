package spring.solr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.solr.model.Item;

import java.math.BigDecimal;

/**
 * @author : xiongjinchen
 * @description: spring整合Junite
 * @date :2019/9/24 19:37
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ="classpath:spring-solr.xml")
public class SolrTest {
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 新增测试
     */
    @Test
    public void solrAddTest(){
        Item item=new Item();
        item.setId(1L);
        item.setBrand("华为");
        item.setCategory("手机");
        item.setGoodsId(1L);
        item.setSeller("华为2号专卖店");
        item.setTitle("华为Mate9");
        item.setPrice(new BigDecimal(2000));

        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }
}
