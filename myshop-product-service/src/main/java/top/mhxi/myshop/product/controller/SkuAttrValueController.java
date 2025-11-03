package top.mhxi.myshop.product.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.product.entity.SkuAttrValue;
import top.mhxi.myshop.product.service.SkuAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "SkuAttrValue管理")
@RestController
@RequestMapping("/product/skuAttrValue")
public class SkuAttrValueController {

    @Autowired
    private SkuAttrValueService skuAttrValueService;


    @Operation(summary = "添加一个SkuAttrValue")
    @PostMapping
    public R insert(@RequestBody SkuAttrValue skuAttrValue) {

        int i = skuAttrValueService.insert(skuAttrValue);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个SkuAttrValue")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = skuAttrValueService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个SkuAttrValue")
    @PutMapping
    public R update(@RequestBody SkuAttrValue skuAttrValue) {

        int i = skuAttrValueService.update(skuAttrValue);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个SkuAttrValue")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        SkuAttrValue skuAttrValue = skuAttrValueService.selectById(id);

        if(skuAttrValue != null) {
            return R.ok().data("record", skuAttrValue);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有SkuAttrValue")
    @GetMapping("/selectAll")
    public R selectAll() {
        List<SkuAttrValue> skuAttrValues = skuAttrValueService.selectAll();

        if(!skuAttrValues.isEmpty()) {
            return R.ok().data("record", skuAttrValues);
        } else {
            return R.error();
        }
    }
}