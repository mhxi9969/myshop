import org.apache.commons.math.stat.descriptive.summary.Product;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.mhxi.myshop.common.config.RabbitMQConfig;
import top.mhxi.myshop.order.OrderServiceApplication;

@SpringBootTest(classes = OrderServiceApplication.class)
public class OrderTest {

}
