package top.mhxi.myshop.common.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
swagger的描述
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                    .title("MyShop接口文档")
                    .version("v1.0")
                    .description("电商系统接口说明")
            );
    }
}