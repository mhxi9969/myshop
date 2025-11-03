package top.mhxi.myshop.product.controller;



import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.product.entity.Category;
import top.mhxi.myshop.product.entity.ProductSpu;
import top.mhxi.myshop.product.entity.query.ProductSpuQueryCondition;
import top.mhxi.myshop.product.service.CategoryService;
import top.mhxi.myshop.product.service.ProductSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "ProductSpu管理")
@RestController
@RequestMapping("/product/productSpu")
public class ProductSpuController {

    @Autowired
    private ProductSpuService productSpuService;

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "添加一个ProductSpu")
    @PostMapping
    public R insert(@RequestBody ProductSpu productSpu) {
        int i = productSpuService.insert(productSpu);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个ProductSpu")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = productSpuService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个ProductSpu")
    @PutMapping
    public R update(@RequestBody ProductSpu productSpu) {
        int i = productSpuService.update(productSpu);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个ProductSpu")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        ProductSpu productSpu = productSpuService.selectById(id);

        if(productSpu != null) {
            return R.ok().data("record", productSpu);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有ProductSpu")
    @GetMapping("/selectAll")
    public R selectAll() {
        List<ProductSpu> productSpus = productSpuService.selectAll();

        if(!productSpus.isEmpty()) {

            return R.ok().data("record", productSpus);
        } else {
            return R.error();
        }
    }

    @Operation(summary = "根据条件查询Product，带分页")
    @PostMapping("/selectByCondition/{current}")
    public R selectByCondition(@PathVariable int current,
                               @RequestBody ProductSpuQueryCondition productSpuQueryCondition) {
        PageInfo<ProductSpu> info = productSpuService.selectByCondition(current, productSpuQueryCondition);

        if(!info.getList().isEmpty()) {

            return R.ok().data("record", info);
        } else {
            return R.error();
        }
    }
}