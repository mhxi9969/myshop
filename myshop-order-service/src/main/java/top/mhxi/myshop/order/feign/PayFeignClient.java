package top.mhxi.myshop.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.mhxi.myshop.common.utils.R;

import java.math.BigDecimal;

@FeignClient(name = "payment")
public interface PayFeignClient {

    @GetMapping("/pay/pay/create/{id}/{price}")
    R createPayment(@PathVariable Long id, @PathVariable BigDecimal price);


    @GetMapping("/pay/pay/search/{id}/")
    R poll(@PathVariable String id);
}
