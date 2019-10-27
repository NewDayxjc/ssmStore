package com.pinyougou.model;
/**
 * @Author xiongjinchen
 * @Date 2019/10/4 8:47
 * @Version 1.0
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/4 8:47
 */
public class Cart implements Serializable {
    private String sellerId;//商家ID
    private String sellerName;//商家名称
    private List<OrderItem> orderItemList=new ArrayList<OrderItem>();//购物车明细  先new一下，防止空指针

    public Cart() {
    }

    public Cart(String sellerId, String sellerName, List<OrderItem> orderItemList) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.orderItemList = orderItemList;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "sellerId='" + sellerId + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
