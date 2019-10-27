package pinyougou.cart.service;
import com.pinyougou.model.Cart;
import com.pinyougou.model.ItemCat;

import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/4 9:32
 */
public interface CartService {
    /**
     * 合并cookie和redis的购物车
     * @param redisCarts
     * @param cookieCart
     * @return
     */
    List<Cart> mergeCartList(List<Cart> redisCarts, List<Cart> cookieCart);

    /**
      * 添加商品到购物车
      * @param cart
      * @param itemId
      * @param num
      * @return
      */
     List<Cart> add(List<Cart> cart,Long itemId,Integer num);

     /**
      * 若已登录将购物车数据放到Redis
      * @param carts
      * @param username
      * @return
      */
     void saveRedis(List<Cart> carts,String username);

     /**
      * 根据用户从Redis中获取购物车数据
      * @param username
      * @return
      */
     List<Cart> findByRedis(String username);
}
