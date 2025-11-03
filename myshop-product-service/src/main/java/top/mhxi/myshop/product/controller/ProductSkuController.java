package top.mhxi.myshop.product.controller;



import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.product.entity.ProductSku;
import top.mhxi.myshop.product.entity.query.ProductSkuQueryCondition;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;
import top.mhxi.myshop.product.service.ProductSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "ProductSku管理")
@RestController
@RequestMapping("/product/productSku")
public class ProductSkuController {

    @Autowired
    private ProductSkuService productSkuService;


    @Operation(summary = "添加一个复杂ProductSku")
    @PostMapping
    public R insert(@RequestBody ProductSkuTreeTO vo) {

        int i = productSkuService.insert(vo);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    // todo 同时删除关联的属性值
    @Operation(summary = "根据id删除一个ProductSku")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = productSkuService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个复杂ProductSku")
    @PutMapping
    public R update(@RequestBody ProductSkuTreeTO vo) {

        int i = productSkuService.update(vo);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个复杂的ProductSku")
    @GetMapping("/tree/{id}")
    public R selectByIdTree(@PathVariable Long id) {
        ProductSkuTreeTO vo = productSkuService.selectByIdTree(id);

        if(vo != null) {
            return R.ok().data("record", vo);
        } else {
            return R.error();
        }
    }



    @Operation(summary = "根据spu id查询所有复杂的ProductSku")
    @GetMapping("/selectAllTreeBySpuId/{id}")
    public R selectAllTreeBySpuId(@PathVariable Long id) {
        List<ProductSkuTreeTO> vos = productSkuService.selectAllTreeBySpuId(id);

        if(!vos.isEmpty()) {

            return R.ok().data("record", vos);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个简单的ProductSku")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        ProductSku productSku = productSkuService.selectById(id);

        if(productSku != null) {
            top.mhxi.myshop.common.to.ProductSkuTO skuTO = new top.mhxi.myshop.common.to.ProductSkuTO();
            BeanUtils.copyProperties(productSku, skuTO);
            return R.ok().data("record", skuTO);
        } else {
            return R.error();
        }
    }

    @Operation(summary = "根据条件查询简单的Product，带分页")
    @PostMapping("/selectByCondition/{current}")
    public R selectByCondition(@PathVariable int current,
                               @RequestBody ProductSkuQueryCondition productSkuQueryCondition) {
        PageInfo<ProductSku> info = productSkuService.selectByCondition(current, productSkuQueryCondition);

        if(!info.getList().isEmpty()) {
            return R.ok().data("record", info);
        } else {
            return R.error();
        }
    }

    @Operation(summary = "更新库存")
    @GetMapping("/{id}/{num}")
    public R updateStock(@PathVariable Long id, @PathVariable Integer num) {

        int i = productSkuService.updateStock(id, num);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }

}