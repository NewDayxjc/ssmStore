/**
 * @Author xiongjinchen
 * @Date 2019/10/1 17:54
 * @Version 1.0
 */

import com.pinyougou.manager.service.MessageSender;
import com.pinyougou.mq.MessageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiongjinchen
 * @description: TODO
 * @date :2019/10/1 17:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-MQ.xml")
public class ProducerTest {
    @Test
    public void sendMessageTest(){
        List<String> itemList = new ArrayList();
        itemList.add("玉米");
        itemList.add("大米");
        itemList.add("小米");
        itemList.add("米");
        MessageInfo messageInfo=new MessageInfo(MessageInfo.METHOD_UPDATE,itemList);
        MessageSender messageSender = new MessageSender();
        messageSender.sendObjectMessage(messageInfo);
    }

}
