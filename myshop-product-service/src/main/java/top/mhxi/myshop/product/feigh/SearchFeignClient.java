package top.mhxi.myshop.product.feigh;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;
import top.mhxi.myshop.common.utils.R;

import java.util.List;

@FeignClient(name = "search")
public interface SearchFeignClient {

    // 搜索引擎上架
    @PostMapping("/search/search/add")
    R addProduct(@RequestBody List<ProductSkuTreeTO> skuTreeTOS);

    // 搜索引擎下架
    @PostMapping("/search/search/remove")
    R removeProduct(@RequestBody List<ProductSkuTreeTO> skuTreeTOS);
}
