package com.pinyougou.page.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.GoodsDescMapper;
import com.pinyougou.mapper.GoodsMapper;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.model.Goods;
import com.pinyougou.model.GoodsDesc;
import com.pinyougou.model.Item;
import com.pinyougou.page.service.ItemPageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/29 12:14
 */
@Service
public class ItemPageServiceImpl implements ItemPageService {
    @Value("${HTML_PATH}")
    private String HTML_PATH;//文件生成的路径
    @Value("${HTML_SUFFIX}")
    private  String HTML_SUFFIX;//文件的后缀
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean;
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public void deleteHtml(Long id) {
        File file=new File(HTML_PATH+id+HTML_SUFFIX);
        if(file.exists()){
            file.delete();
        }
    }
    public Boolean buildHtml(Long goodsId) throws Exception {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        GoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        List<Item> itemsList = getItemListByGoodsId(goodsId);
        //封装数据模型
        Map<String,Object> dataMap=new HashMap<String, Object>();
        dataMap.put("goods",goods);
        dataMap.put("goodsDesc",goodsDesc);
//第一种        dataMap.put("itemList", JSON.toJSONString(itemsList));
        //第二种获取需要的数据,后台不转JSON，传对象到前台
        dataMap.put("itemList",itemsList);
        //
        //创建freeMarker生成Html
        Configuration configuration = freeMarkerConfigurationFactoryBean.createConfiguration();
        //创建模板对象
        Template template=configuration.getTemplate("item.ftl");

        //指定文件输出对象
//        Writer writer=new FileWriter(new File("D:/pinyougouProject/pinyougou/pinyougou-page-service/src/main/webapp/"+goodsId+".html"));
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HTML_PATH+goodsId+HTML_SUFFIX),"UTF-8"));

        //合成输出文件

        template.process(dataMap,writer);
        writer.flush();
        writer.close();
        return true;
    }



    //根据goodsId获取Item信息
    public List<Item> getItemListByGoodsId(Long goodsId){
        //状态为审核通过，
        Example example=new Example(Item.class);
        Example.Criteria criteria=example.createCriteria();
        //添加条件
        criteria.andEqualTo("status",1);
        criteria.andEqualTo("goodsId",goodsId);
        //isDefault为1的有限展示，因此排序为倒序
        example.orderBy("isDefault").desc();
        return itemMapper.selectByExample(example);
    }
}
