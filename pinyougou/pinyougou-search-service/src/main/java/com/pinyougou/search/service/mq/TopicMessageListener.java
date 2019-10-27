package com.pinyougou.search.service.mq;
/**
 * @Author xiongjinchen
 * @Date 2019/10/1 14:58
 * @Version 1.0
 */

import com.pinyougou.model.Item;
import com.pinyougou.mq.MessageInfo;
import com.pinyougou.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/1 14:58
 */
public class TopicMessageListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;
    @Override
    public void onMessage(Message message) {
        if(message instanceof ObjectMessage){
            ObjectMessage objectMessage= (ObjectMessage) message;
            try {
                MessageInfo messageInfo = (MessageInfo) objectMessage.getObject();
                //如果是修改
                if(messageInfo.getMethod()==MessageInfo.METHOD_UPDATE){
                    //强转 因为messageInfo.getContext()传过来的是一个List<Item>
                    List<Item> context = (List<Item>) messageInfo.getContext();
                    itemSearchService.importItemist(context);
                }else if(messageInfo.getMethod()==MessageInfo.METHOD_DELETE){
                    //强转 因为messageInfo.getContext()传过来的是一个List<Long>
                    List<Long> ids= (List<Long>) messageInfo.getContext();
                    itemSearchService.DeleteItemByIds(ids);
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}
