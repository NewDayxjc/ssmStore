package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.http.Result;
import com.pinyougou.model.PayLog;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 *
 * @date :2019/10/6 15:24
 */
@RestController
@RequestMapping(value = "/pay")
public class PayController {
    @Reference
    private WeiXinPayService weiXinPayService;
    @Reference
    private OrderService orderService;
//    @Autowired
//    private IdWorker idWorker;

   private  String id;
    /**
     * 创建订单
     * @return
     */
    @RequestMapping("/createNative")
    public Map<String,String> createNative(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        PayLog payLog = orderService.searchPayLogFromRedis(username);
        if(payLog!=null){
            return weiXinPayService.createNative(payLog.getOutTradeNo(),"1");
        }

        id= "7864646"+(int)(Math.random()*100000);
            String total_fee="1";
            return weiXinPayService.createNative(id,total_fee);
    }

    @RequestMapping(value = "/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) throws Exception {
        int acount=0;
        while(true){
            Map<String,String> map = weiXinPayService.queryNative(out_trade_no);
            String trade_state =  map.get("trade_state");

            if(trade_state.equals("SUCCESS")){
                //更新商品和支付日志状态
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                orderService.updateOrderAndPayLogStatus(out_trade_no,map.get("transaction_id"));
                return  new Result(true,"支付成功");
            }else if("PAYERROR".equals(trade_state)){
                return  new Result(false,"未支付成功");
            }

            Thread.sleep(3000);
            acount++;
            if(acount>5){   //TODO 超时自动刷新二维码 出异常
                System.out.println(acount);
                return  new Result(false,"timeout");
            }
       }
    }

}
