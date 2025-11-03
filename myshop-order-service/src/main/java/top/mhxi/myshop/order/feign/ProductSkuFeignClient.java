package top.mhxi.myshop.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.mhxi.myshop.common.utils.R;


@FeignClient(name = "product")
public interface ProductSkuFeignClient {

    @GetMapping("/product/productSku/{id}")
    R selectById(@PathVariable Long id);
}
