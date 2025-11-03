package top.mhxi.myshop.product.feigh;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;
import top.mhxi.myshop.common.utils.R;

import java.util.List;

@FeignClient(name = "search")
public interface SearchFeignClient {

    @PostMapping("/search/search/add")
    R addProduct(@RequestBody List<ProductSkuTreeTO> skuTreeTOS);

    @PostMapping("/search/search/remove")
    R removeProduct(@RequestBody List<ProductSkuTreeTO> skuTreeTOS);
}
