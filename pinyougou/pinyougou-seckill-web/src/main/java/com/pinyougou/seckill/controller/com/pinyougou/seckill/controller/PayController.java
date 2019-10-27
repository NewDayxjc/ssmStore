package com.pinyougou.seckill.controller.com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.http.Result;
import com.pinyougou.model.PayLog;
import com.pinyougou.model.SeckillOrder;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.seckill.service.SeckillOrderService;
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
    private SeckillOrderService seckillOrderService;
//    @Autowired
//    private IdWorker idWorker;

    /**
     * 创建订单
     * @return
     */
    @RequestMapping("/createNative")
    public Map<String,String> createNative(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        SeckillOrder order = seckillOrderService.getOrderByusername(username);
        String id=order.getId()+"";
//        String total_fee=order.getMoney()+"";
            String total_fee="1";
            return weiXinPayService.createNative(id,total_fee);
    }

    @RequestMapping(value = "/queryPayStatus")
    public Result queryPayStatus(String out_trade_no) throws Exception {
        int acount=0;
        while(true){

            Map<String,String> map = weiXinPayService.queryNative(out_trade_no);
            String trade_state =  map.get("trade_state");
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if("SUCCESS".equals(trade_state)){
                //添加订单并更新订单状态为已支付
                seckillOrderService.insertOrder(username,map.get("transaction_id"));
                return  new Result(true,"支付成功");
            }else if("PAYERROR".equals(trade_state)){
                return  new Result(false,"未支付成功");
            }

            Thread.sleep(3000);
            acount++;
            if(acount>10){
                //用户在一定时间30分(30秒)内未付款，则关闭订单

                //关闭微信订单请求
                Map<String,String> closeResult = weiXinPayService.removeOrder(out_trade_no);
                //  微信回应
                if("SUCCESS".equals(closeResult.get("result_code"))){
                    seckillOrderService.removeOrder(username);
                }else {
                    String err_code = closeResult.get("err_code");
                    if("ORDERPAID".equals(err_code)){
                        //查询订单状态
                        Map<String,String> dataMap = weiXinPayService.queryNative(out_trade_no);
                        //订单已支付,用户支付成功
                        //更改订单和日志状态
                        seckillOrderService.updateOrderStatus(username,dataMap.get("transaction_id"));//TODO 异常
                        return  new Result(true,"支付成功");
                    }

                }
                return  new Result(false,"timeout");
            }
       }
    }

}
