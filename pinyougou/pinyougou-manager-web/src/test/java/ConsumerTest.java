/**
 * @Author xiongjinchen
 * @Date 2019/10/1 18:21
 * @Version 1.0
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-MQ.xml")
public class ConsumerTest {
    @Test
    public void getMessage(){
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
