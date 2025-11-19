import org.apache.commons.math.stat.descriptive.summary.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import top.mhxi.myshop.user.UserServiceApplication;

@SpringBootTest(classes = UserServiceApplication.class)
public class UserTest {


    @Test
    public void testMail() {

    }
}
