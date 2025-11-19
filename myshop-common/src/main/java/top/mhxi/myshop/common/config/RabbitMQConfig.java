package top.mhxi.myshop.common.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// 修改后，要删除rabbitmq的交换机和队列，才能重新创建
@Configuration
public class RabbitMQConfig {

    // 普通交换机
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange("order-delay-exchange");
    }

    // 普通 TTL 队列，用于延迟消息，消息到期后进入死信交换机，然后进入死信队列
    @Bean
    public Queue orderDelayQueue() {
        return QueueBuilder.durable("order.delay.queue")
                // 设置过期后，进入死信交换机
                .withArgument("x-dead-letter-exchange", "order-dlx-exchange")
                .withArgument("x-dead-letter-routing-key", "order.stock.rollback")
                .withArgument("x-message-ttl", 60000) // 60秒
                .build();
    }

    // 绑定关系
    @Bean
    public Binding orderDelayBinding() {
        return BindingBuilder.bind(orderDelayQueue())
                .to(orderDelayExchange())
                .with("order.delay"); // routing key
    }


    // 死信交换机
    @Bean
    public DirectExchange orderDLXExchange() {
        return new DirectExchange("order-dlx-exchange");
    }

    // 死信队列
    @Bean
    public Queue rollbackStockQueue() {
        return QueueBuilder.durable("order.stock.rollback.queue").build();
    }

    // 死信队列绑定
    @Bean
    public Binding rollbackStockBinding() {
        return BindingBuilder.bind(rollbackStockQueue())
                .to(orderDLXExchange())
                .with("order.stock.rollback");
    }
}