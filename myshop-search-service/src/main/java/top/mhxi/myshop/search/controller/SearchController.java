package top.mhxi.myshop.search.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.search.entity.SearchConditon;
import top.mhxi.myshop.search.service.SearchService;

import java.util.List;

@Tag(name = "search管理")
@RestController
@RequestMapping("/search/search")
public class SearchController {

    @Autowired
    SearchService searchService;

    // 新增商品
    @PostMapping("/add")
    public R addProduct(@RequestBody List<ProductSkuTreeTO> skuTreeTOS) {

        searchService.addProduct(skuTreeTOS);
        return R.ok();
    }

    // 下架商品
    @PostMapping("/remove")
    public R removeProduct(@RequestBody List<ProductSkuTreeTO> skuTreeTOS) {

            searchService.removeProduct(skuTreeTOS);
        return R.ok();
    }

    // 根据条件检索商品
    @PostMapping("/search/{page}")
    public R searchProduct(@RequestBody SearchConditon searchConditon, @PathVariable Integer page) {

        Page<ProductSkuTreeTO> result = searchService.searchProduct(searchConditon, page, 8);

        return R.ok().data("record", result);
    }

    // 根据spu检索商品sku
    @PostMapping("/searchBySpuID/{id}")
    public R searchBySpuID(@PathVariable Long id) {

        List<ProductSkuTreeTO> result = searchService.searchBySpuID(id);

        return R.ok().data("record", result);
    }
}
