package com.pinyougou.page.service.mq;
/**
 * @Author xiongjinchen
 * @Date 2019/10/1 15:26
 * @Version 1.0
 */

import com.pinyougou.model.Item;
import com.pinyougou.mq.MessageInfo;
import com.pinyougou.page.service.ItemPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/1 15:26
 */
public class TopicMessageListener implements MessageListener {
    @Autowired
    private ItemPageService itemPageService;
    @Override
    public void onMessage(Message message) {
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage= (ObjectMessage) message;
            try {
                //接收消息
                MessageInfo messageInfo = (MessageInfo) objectMessage.getObject();
                //判断操作
                if(messageInfo.getMethod()==MessageInfo.METHOD_UPDATE){
                    //获取消息内容
                    List<Item> items = (List<Item>) messageInfo.getContext();
                    //循环获取items的goodsId
                    for (Item item:items) {
                        Long goodsId = item.getGoodsId();
                        //操作  生成静态页
                        itemPageService.buildHtml(goodsId);
                    }
                }else if(messageInfo.getMethod()==MessageInfo.METHOD_DELETE){
                    List<Long> ids= (List<Long>) messageInfo.getContext();
                    //去重
                    Set<Long> goodsIds = getGoodsId(ids);
                    //循环遍历ids  根据goodsId删除已生成的静态页
                    for (Long id : goodsIds) {
                        itemPageService.deleteHtml(id);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    //去除重复的goodsId
    public Set<Long> getGoodsId(List<Long> ids){
        Set<Long> set=new HashSet<Long>();
        for (Long id : ids) {
            set.add(id);
        }
        return set;
    }
}
