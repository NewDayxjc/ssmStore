package com.pinyougou.search.service.impl;
/**
 * @Author xiongjinchen
 * @Date 2019/9/25 0:36
 * @Version 1.0
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.model.Item;
import com.pinyougou.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/25 0:36
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 高亮显示
     * @param search
     * @return
     */
    public Map<String, Object> search(Map search) {
        //每个查询方法必须在一个查询条件结束后下方写，不然会覆盖
        //搜索条件
//        Query query=new SimpleQuery("*:*");
        HighlightQuery query=new SimpleHighlightQuery(new SimpleStringCriteria("*:*"));
        //配置高亮
        hightSettings(query);

        String category=null;
        //如果search为空，则搜索全部数据
        if(search!=null){
            String keyword = (String)search.get("keywords");
            if(StringUtils.isNotBlank(keyword)){
                //添加条件
                Criteria criteria=new Criteria("item_keywords").is(keyword.replace(" ",""));
                //将其加入到query
                query.addCriteria(criteria);
            }

            //分类过滤
            category = (String) search.get("category");
            if(StringUtils.isNotBlank(category)) {
                //设置条件
                Criteria criteria = new Criteria("item_category").is(category);
                //创建过滤
                FilterQuery filter = new SimpleFilterQuery();
                //添加条件
                filter.addCriteria(criteria);
                query.addFilterQuery(filter);
            }

            //品牌过滤
            String brand = (String) search.get("brand");
            if(StringUtils.isNotBlank(brand)){
                Criteria criteria=new Criteria("item_brand").is(brand);
                FilterQuery filter=new SimpleFilterQuery();
                filter.addCriteria(criteria);
                query.addFilterQuery(filter);
            }

            //规格过滤
            //获取前台传入的规格数据  这里是JSON格式，不可以转换为String
            Object spec = search.get("spec");
            if(spec!=null) {
                Map<String, String> specMap = JSON.parseObject(spec.toString(), Map.class);
                if (specMap.size()!=0){
                    //遍历获取值  specMap会有许多值
                    for (Map.Entry<String, String> entry : specMap.entrySet()) {
                        //获取键
                        String key = entry.getKey();
                        //获取值
                        String value = entry.getValue();
                        //构建条件
                        Criteria criteria=new Criteria("item_spec_"+key).is(value);
                        //构造过滤器
                        FilterQuery filter=new SimpleFilterQuery();
                        filter.addCriteria(criteria);
                        query.addFilterQuery(filter);
                    }
                }
            }
            //条件排序
            String sort = (String) search.get("sort");
            String sortField = (String) search.get("sortField");
            if(StringUtils.isNotBlank(sort)&& StringUtils.isNotBlank(sortField)){
                //构造排序条件
                Sort orders=null;
                if(sort.equalsIgnoreCase("ASC")){
                    orders=new Sort(Sort.Direction.ASC,sortField);
                }else{
                    orders=new Sort(Sort.Direction.DESC,sortField);
                }
                query.addSort(orders); //TODO  销量排序  评价排序  nginx路径反射
            }
            //根据价格区间过滤：
            /**
             * 1:价格区间的连接符可能是 -：  x=<  price  <y
             * 2: 也有可能是空格         ' '：x=< price  只需要第一个价格区间
             */
            String price=(String) search.get("price");
            if(StringUtils.isNotBlank(price)){
                String[] ranges = price.split("-");
                if(ranges!=null &&ranges.length==2) {
                    Criteria criteria = new Criteria("item_price").between(ranges[0], ranges[1],true,false);
                    FilterQuery filter = new SimpleFilterQuery(criteria);
                    query.addFilterQuery(filter);
                }
                /*
                greaterThan:this.between(lowerBound, (Object)null, false, true);

                greaterThanEqual:查询一个数字及其以上: this.between(lowerBound, (Object)null);
                 */
                ranges = price.split(" ");
                if(ranges!=null&&ranges.length==2){
                    Criteria criteria =new Criteria("item_price")
                            .greaterThanEqual(ranges[0]);
                    FilterQuery filter=new SimpleFilterQuery(criteria);
                    query.addFilterQuery(filter);
                }
            }

        }
        Integer pageNum = (Integer) search.get("pageNum");

        Integer size = (Integer) search.get("size");
        if(pageNum==null){
            pageNum=1;
        }
        if(size==null){
            size=10;
        }
        //分页
        query.setOffset((pageNum-1)*size);
        query.setRows(size); //TODO  分页显示最后一页存在问题

        HighlightPage<Item> items = solrTemplate.queryForHighlightPage(query, Item.class);
        //获取高亮值
        hightGettings(items);



        //返回的map
        Map<String,Object> mapData=new HashMap<String, Object>();
//将分组集合封装到map集合
        List<String> categoryList = getCategoryList(query);

        //当用户选择了分类时，查询分类对应的品牌规格数据
        if(StringUtils.isNotBlank(category)){
            Map<String, Object> map = BrandAndSpec(category);
            mapData.putAll(map);
        }else {
            //默认选中第一个分类
            if(categoryList!=null&& categoryList.size()>0){
                Map<String,Object> map=BrandAndSpec(categoryList.get(0));
                //获取品牌和规格
                mapData.putAll(map);
            }
        }

        //将分组集合封装到map集合
        mapData.put("categoryList",categoryList);
        //获取总记录数
        long totalElements = items.getTotalElements();
        mapData.put("total",totalElements);
        //获取结果集
        List<Item> content = items.getContent();
        mapData.put("rows",content);

        return mapData;
    }

    /**
     * 将根据goodsId查询出来的item批量导入索引库
     * @param itemList
     */
    @Override
    public void importItemist(List<Item> itemList) {
        if(itemList!=null && itemList.size()>0) {
            solrTemplate.saveBeans(itemList);
            solrTemplate.commit();
        }
    }

    @Override
    public void DeleteItemByIds(List<Long> ids) {
        Criteria criteria =new Criteria("item_goodsid").is(ids);
        Query query=new SimpleQuery(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    //查询分组信息
    public List<String> getCategoryList(HighlightQuery query) {

        //解决分组数据未在第二页及其以上显示问题：分组数据只有几条，query已经做了分页处理,对分页进行重置为第一页
        query.setOffset(0);
        query.setRows(100);
        //分组
        GroupOptions groupOptions=new GroupOptions();
        //添加要查询的分组域条件
        groupOptions.addGroupByField("item_category");
        //根据category分组
        //将其添加道query中
        query.setGroupOptions(groupOptions);
        //查询
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);

        //获取获取对应域的分组结果
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
        //groupResult具有键值对的结构
        List<String> categoryList=new ArrayList<String>();
        for (GroupEntry<Item> group: groupResult.getGroupEntries()) {
            //获取对应结果集
            String groupValue = group.getGroupValue();

            categoryList.add(groupValue);
        }
        return categoryList;
    }

    public Map<String,Object> BrandAndSpec(String category){
        Long templateId = (Long) redisTemplate.boundHashOps("ItemCat").get(category);
        Map<String, Object> mapData = new HashMap<String, Object>();
        if(templateId!=null) {
            List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(templateId);
            List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(templateId);
            mapData.put("brandList", brandList);
            mapData.put("specList", specList);
        }
        return mapData;
    }

// //获取高亮值
    public void hightGettings(HighlightPage<Item> items) {
        //数据包含高亮和非高亮的
        List<HighlightEntry<Item>> highlighted = items.getHighlighted();

        for (HighlightEntry<Item> item : highlighted) {
            //获取非高亮数据
            Item entity = item.getEntity();
            //获取被循环高亮数据假设只有一条数据
            List<HighlightEntry.Highlight> highlights = item.getHighlights();
            if(highlights!=null&&highlights.size()>0){
                //将非高亮数据转成高亮,获取其中的一条数据
                HighlightEntry.Highlight highlightGetOne = highlights.get(0);
                //获取切片  从高亮数据获取高亮数据(假设只有一条值)
                List<String> snipplets = highlightGetOne.getSnipplets();

                //进行拼接
                String strHight="";
                for (int i=0;i<snipplets.size();i++) {
                    if(snipplets!=null&& snipplets.size()>0) {
                        strHight = snipplets.get(i);
                    }
                }

                entity.setTitle(strHight);
            }
        }
    }
////配置高亮
    public void hightSettings(HighlightQuery query) {
        HighlightOptions highlightOptions=new HighlightOptions();
        //配置高亮
        //设置item_title域为高亮域
        highlightOptions.addField("item_title");
        //设置前缀
        highlightOptions.setSimplePrefix("<span style=\"color:red\">");
        //设置后缀
        highlightOptions.setSimplePostfix("</span>");

        //将高亮选项添加到query
        query.setHighlightOptions(highlightOptions);
    }
//获取高亮数据


    /**
     * 查询非高亮数据
     * @param search
     * @return
     */
    public Map<String, Object> searchNotHight(Map search) {
        //搜索条件
        Query query=new SimpleQuery("*:*");
        //如果search为空，则搜索全部数据
        if(search!=null){
            String keyword = (String)search.get("keywords");
            if(StringUtils.isNotBlank(keyword)){
                //添加条件
                Criteria criteria=new Criteria("item_keywords").is(keyword);
                //将其加入到query
                query.addCriteria(criteria);
            }
        }
        //分页
        query.setOffset(3);
        query.setRows(50);
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);

        //返回的map
        Map<String,Object> mapData=new HashMap<String, Object>();
        //获取总记录数
        long totalElements = items.getTotalElements();
        mapData.put("total",totalElements);
        //获取结果集
        List<Item> content = items.getContent();
        mapData.put("rows",content);

        return mapData;
    }

}
