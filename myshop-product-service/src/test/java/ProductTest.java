import org.apache.commons.math.stat.descriptive.summary.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.mhxi.myshop.product.ProductServiceApplication;


@SpringBootTest(classes = ProductServiceApplication.class)
public class ProductTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void testRedis() {
        Product product = new Product();

        redisTemplate.opsForValue().set("product:1", product);
        Object o = redisTemplate.opsForValue().get("product:1");
    }
}
