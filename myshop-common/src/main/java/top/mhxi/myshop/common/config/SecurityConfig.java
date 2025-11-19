package top.mhxi.myshop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
spring security拦截配置
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RedisSessionFilter redisSessionFilter) throws Exception {
        http.csrf().disable().authorizeRequests()
            // 允许所有人访问
            .antMatchers("/user/user/login",
                    "/user/user/register",
                    "/user/user/sendCode",
                    "/product/productSku/selectByCondition/**",
                    "/product/productSku/**",
                    "/search/search/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/pay/pay/**").permitAll()

            // 必须登录，角色是用户或管理员才能访问
            .antMatchers("/user/user/getUserBySession",
                    "/user/user/logout",
                    "/cart/cart/**",
                    "/order/order/**",
                    "/order/orderItem/**"
                    ).hasAnyRole("USER","ADMIN")

            // 其他接口，必须是管理员才能访问
            .anyRequest().hasRole("ADMIN")
            .and()
            .addFilterBefore(redisSessionFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
