package top.mhxi.myshop.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;


@EnableAsync  // 允许异步发送邮件
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "top.mhxi")  //配置spring扫描的包，进行bean管理
@MapperScan("top.mhxi.myshop.user.mapper")  // 配置mapper接口的位置
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
