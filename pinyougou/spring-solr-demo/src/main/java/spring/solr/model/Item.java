package spring.solr.model;
/**
 * @Author xiongjinchen
 * @Date 2019/9/24 19:35
 * @Version 1.0
 */

import org.apache.solr.client.solrj.beans.Field;

import java.io.*;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/9/24 19:35
 */
public class Item {
    @Field
    private Long id;

    @Field("item_title")
    private String title;

    @Field("item_price")
    private BigDecimal price;

    @Field("item_image")
    private String image;

    @Field("item_goodsid")
    private Long goodsId;

    @Field("item_category")
    private String category;

    @Field("item_brand")
    private String brand;

    @Field("item_seller")
    private String seller;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", goodsId=" + goodsId +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", seller='" + seller + '\'' +
                '}';
    }


        /*Properties properties=new Properties();
        FileOutputStream file=new FileOutputStream("classpath:spring-solr.xml");

        properties.load();*/
//        try {
//            Properties properties = new Properties();
//            properties.load(ClassLoader.getSystemResourceAsStream("sys_jdbc.properties"));
//            System.out.println(properties);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }




}
