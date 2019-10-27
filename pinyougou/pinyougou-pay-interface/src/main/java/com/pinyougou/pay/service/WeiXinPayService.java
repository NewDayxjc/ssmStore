package com.pinyougou.pay.service;

import java.util.Map;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/6 14:33
 */
public interface WeiXinPayService {
    /**
     * 创建支付二维码url
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    public Map  createNative(String out_trade_no,String total_fee);

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    public Map queryNative(String out_trade_no);

    Map removeOrder(String out_trade_no) throws Exception;
}
