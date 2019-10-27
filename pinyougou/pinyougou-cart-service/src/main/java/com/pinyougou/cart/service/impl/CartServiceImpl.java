package com.pinyougou.cart.service.impl;
/**
 * @Author xiongjinchen
 * @Date 2019/10/4 9:35
 * @Version 1.0
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.http.Result;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.model.Cart;
import com.pinyougou.model.Item;
import com.pinyougou.model.OrderItem;
import com.pinyougou.model.Seller;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.data.redis.core.RedisTemplate;
import pinyougou.cart.service.CartService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/4 9:35
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 合并cookie和redis的购物车
     * @param redisCarts
     * @param cookieCart
     * @return
     */
    public List<Cart> mergeCartList(List<Cart> redisCarts, List<Cart> cookieCart){
        for (Cart cart : cookieCart) {
            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                Long itemId = orderItem.getItemId();
                Integer num = orderItem.getNum();
                redisCarts=add(redisCarts,itemId,num);
            }
        }
        return redisCarts;
    }

    /**
     * 增加购物车
     * @param carts
     * @param itemId
     * @param num
     * @return
     */
    public List<Cart> add(List<Cart> carts, Long itemId, Integer num) {
        if(carts==null){
            carts=new ArrayList<Cart>();
        }
        //获取原有购物车数据
        Item item = itemMapper.selectByPrimaryKey(itemId);
        String sellerId=item.getSellerId();
        //购物车有该商家购物车
        Cart cart = searchSellCart(carts, sellerId);
        if(cart!=null){

            //所添商品在该商家购物车是否存在存在
            OrderItem orderItem = searchOrder(cart, itemId);
            if(orderItem!=null){
                //如果存在  数量累加，价格重新计量
                num =  num.intValue() + orderItem.getNum().intValue();
                orderItem.setNum(num);
                BigDecimal price = orderItem.getPrice();
                double totalFee=num*price.doubleValue();
                orderItem.setTotalFee(BigDecimal.valueOf(totalFee));

                if(num<=0){
                    cart.getOrderItemList().remove(orderItem);
                }
                if(cart.getOrderItemList().size()<=0){
                    carts.remove(cart);
                }
            }else {
                //如果该商品不存在,则添加该商品，并放进该商家购物车
                orderItem = createOrderItem(item, num);
                //加入购物车
                cart.getOrderItemList().add(orderItem);
            }

        }else{
            //无该商家购物车,构建购物车，并将其添加到购物车集合
            cart = createCart(item, num);
            carts.add(cart);
        }
        return carts;
    }

    /**
     * 若已登录将购物车数据放到Redis
     * @param carts
     * @param username
     * @return
     */
    public void saveRedis(List<Cart> carts, String username) {
        redisTemplate.boundHashOps("CartData").put(username,carts);
    }

    public List<Cart> findByRedis(String username) {
        List<Cart> cartData = (List<Cart>) redisTemplate.boundHashOps("CartData").get(username);
        return cartData;
    }

    /**
     * 搜索商家是否存在
     * @param carts
     * @param sellerId
     * @return
     */
    public Cart searchSellCart(List<Cart> carts,String sellerId){
        for (Cart cart : carts) {
            if(sellerId.equals(cart.getSellerId())){
                System.out.println(sellerId.equals(cart.getSellerId()));
                return cart;
            }
        }
        return null;
    }
    public OrderItem searchOrder(Cart cart,Long itemId){
        for (OrderItem item : cart.getOrderItemList()) {
            if(item.getItemId().equals(itemId)){
                return item;
            }
        }
        return null;
    }

    /**
     * 构建商品明细
     * @param item
     * @param num
     * @return
     */

    public OrderItem createOrderItem(Item item,Integer num){
        OrderItem orderItem=new OrderItem();
        orderItem.setTitle(item.getTitle());
        orderItem.setSellerId(item.getSellerId());

        orderItem.setPicPath(item.getImage());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num.intValue());

//        orderItem.setId(item.getId()); //TODO  购物车ID
        double totalFee=num.intValue()*item.getPrice().doubleValue();
        orderItem.setTotalFee(BigDecimal.valueOf(totalFee));
        orderItem.setItemId(item.getId());
        return orderItem;
    }

    /**
     * 构建购物车
     * @param item
     * @param num
     * @return
     */
    public Cart createCart(Item item,Integer num){
        Cart cart=new Cart();
        //获取商家名
        cart.setSellerId(item.getSellerId());
        cart.setSellerName(item.getSeller());

        OrderItem orderItem = createOrderItem(item, num);
        cart.getOrderItemList().add(orderItem);
        return cart;
    }
}
