package com.pinyougou.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.OrderItemMapper;
import com.pinyougou.mapper.OrderMapper;
import com.pinyougou.mapper.PayLogMapper;
import com.pinyougou.model.Cart;
import com.pinyougou.model.Order;
import com.pinyougou.model.OrderItem;
import com.pinyougou.model.PayLog;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayLogMapper payLogMapper;

    /**
     * 查询用户日志
     * @return
     */
    @Override
    public PayLog  searchPayLogFromRedis(String username){
        PayLog payLog = (PayLog) redisTemplate.boundHashOps("PayLog").get(username);
        return payLog;
    }
    /***
     * 增加Order信息
     * @param order
     * @return
     */
    @Override
    public int add(Order order) {
        int accout=0;  //记录订单总数
        double money=0;
        //订单编号
        List<String> orderList=new ArrayList<String>();
        List<Cart> carts = (List<Cart>) redisTemplate.boundHashOps("CartData").get(order.getUserId());
        for (Cart cart : carts) {
            long id = idWorker.nextId();
            Order newOrder=new Order();
            newOrder.setOrderId(id);
            orderList.add(id + "");
            newOrder.setUpdateTime(order.getCreateTime());
            newOrder.setCreateTime(order.getCreateTime());
            newOrder.setReceiver(order.getReceiver());//收货人
            newOrder.setReceiverMobile(order.getReceiverMobile());
            newOrder.setReceiverAreaName(order.getReceiverAreaName());//地址
            newOrder.setSellerId(order.getSellerId());  //商家
            newOrder.setStatus(order.getStatus());    //订单状态
            newOrder.setPaymentType(order.getPaymentType());//支付类型
            newOrder.setSourceType(order.getSourceType());//来源
            //循环添加商品明细
            double price=0;  //记录一个订单价格总额
            for (OrderItem orderItem : cart.getOrderItemList()) {
                Long ItemId=idWorker.nextId();
                orderItem.setId(ItemId);
                orderItem.setOrderId(id);

                orderItem.setSellerId(orderItem.getSellerId());
                price+=orderItem.getTotalFee().doubleValue();
                money+=price;
                orderItemMapper.insertSelective(orderItem);
            }
            newOrder.setPayment(new BigDecimal(price));
            accout+=orderMapper.insertSelective(newOrder);
        }


        //生成日志
        PayLog payLog=new PayLog();
        payLog.setOutTradeNo(idWorker.nextId()+""); //支付订单号
        payLog.setUserId(order.getUserId());
        payLog.setPayType("1");
       payLog.setCreateTime(new Date());
        payLog.setTotalFee((long)(money*100));
        //ArrayList的数据含空格和  [   ]
        payLog.setOrderList(orderList.toString().replace("[","").replace("]","").replace(" ",""));

        payLogMapper.insertSelective(payLog);
        //存进redis
        redisTemplate.boundHashOps("PayLog").put(payLog.getUserId(),payLog);

        //清空购物车
//        redisTemplate.boundHashOps("CartData").delete(order.getUserId());
        return  accout;

        //模型
        /**
         * cartList
         * cart1    sellerId  小红
         *       List<orderItem>  5条
         *      item1
         *      item2
         * 发货： 小红
         * Order1
         * cart2  sellerId 淑华
         *       List<orderItem>
         *                item1
         *                item2
         *
         */
    }

    @Override
    public void updateOrderAndPayLogStatus(String out_trade_no, String transaction_id) {
        Date date=new Date();
        PayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);
//        PayLog payLogFromRedis = (PayLog)redisTemplate.boundHashOps("PayLog").get(username);
        //更新日志
        payLog.setTradeState("2");
        payLog.setTransactionId(transaction_id);
        payLog.setPayTime(date);//这里实际上应该是微信服务器返回的支付时间,这里为了方便，使用了new Data
        payLogMapper.updateByPrimaryKeySelective(payLog);
        //更新订单
        String orderList = payLog.getOrderList();
        String[] orders=orderList.split(",");

        for (String orderId : orders) {
            //根据商品id查询订单Id  黑马这边与本地数据库有些出入
            Order  order =  orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
            order.setStatus("2");
            order.setPaymentTime(date);//这里实际上应该是微信服务器返回的支付时间,这里为了方便，使用了new Data
            orderMapper.updateByPrimaryKeySelective(order);
        }
        //清除redis的日志
        redisTemplate.boundHashOps("PayLog").delete(payLog.getUserId());
    }

    /**
	 * 返回Order全部列表
	 * @return
	 */
	@Override
    public List<Order> getAll(){
        return orderMapper.selectAll();
    }


    /***
     * 分页返回Order列表
     * @param pageNum
     * @param pageSize
     * @return
     */
	@Override
    public PageInfo<Order> getAll(Order order,int pageNum, int pageSize) {
        //执行分页
        PageHelper.startPage(pageNum,pageSize);
       
        //执行查询
        List<Order> all = orderMapper.select(order);
        PageInfo<Order> pageInfo = new PageInfo<Order>(all);
        return pageInfo;
    }






    /***
     * 根据ID查询Order信息
     * @param id
     * @return
     */
    @Override
    public Order getOneById(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }


    /***
     * 根据ID修改Order信息
     * @param order
     * @return
     */
    @Override
    public int updateOrderById(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }


    /***
     * 根据ID批量删除Order信息
     * @param ids
     * @return
     */
    @Override
    public int deleteByIds(List<Long> ids) {
        //创建Example，来构建根据ID删除数据
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();

        //所需的SQL语句类似 delete from tb_order where id in(1,2,5,6)
        criteria.andIn("id",ids);
        return orderMapper.deleteByExample(example);
    }
}
