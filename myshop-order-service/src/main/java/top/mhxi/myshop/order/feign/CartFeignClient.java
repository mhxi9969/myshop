package top.mhxi.myshop.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.mhxi.myshop.common.utils.R;

@FeignClient(name = "cart")
public interface CartFeignClient {

    @GetMapping("/cart/cart/selectAllChecked")
    R selectAllChecked();

    @DeleteMapping("/cart/cart/{id}")
    R deleteById(@PathVariable Long id);
}
