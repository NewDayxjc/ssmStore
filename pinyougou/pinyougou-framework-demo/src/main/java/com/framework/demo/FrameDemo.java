package com.framework.demo;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/29 9:36
 */
public class FrameDemo {
    public static void main(String[] args) throws Exception {
        //准备数据模型，可以是任何java数据类型，也可以是JavaBean，推荐使用Map
        Map<String,Object> dataMap=new HashMap<String, Object>();


        //list
        List goodsList=new ArrayList();
        Map goodsMap1=new HashMap();
        goodsMap1.put("name","小红");
        goodsMap1.put("price","100");

        Map goodsMap2=new HashMap();
        goodsMap2.put("name","小青");
        goodsMap2.put("price","200");

        Map goodsMap3=new HashMap();
        goodsMap3.put("name","小明");
        goodsMap3.put("price","300");
        goodsList.add(goodsMap1);
        goodsList.add(goodsMap2);
        goodsList.add(goodsMap3);

        dataMap.put("now", new Date());
        dataMap.put("success",true);
        dataMap.put("username","小红");
        dataMap.put("goodList",goodsList);
        dataMap.put("point",789456);
        dataMap.put("aaa",null);
        //配置Freemarker

        //创建Configuration
        Configuration configuration=new Configuration(Configuration.VERSION_2_3_23);
        //设置模板路径
//        configuration.setDirectoryForTemplateLoading(new File("D:/pinyougouProject/pinyougou/pinyougou-framework-demo/src/main/resources"));
        //解决文件乱码
        configuration.setDirectoryForTemplateLoading(new File("D:/pinyougouProject/pinyougou/pinyougou-framework-demo/src/main/resources"));
        //设置模板内容编码
        configuration.setDefaultEncoding("UTF-8");

        //创建模板对象
        Template template = configuration.getTemplate("test.ftl");
        //指定输出文件
        Writer writer=new FileWriter("D:/1.html");
        //合成输出
        template.process(dataMap,writer);
        writer.flush();
        writer.close();
    }
}
