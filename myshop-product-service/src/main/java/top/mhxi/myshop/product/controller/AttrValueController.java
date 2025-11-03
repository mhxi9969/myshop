package top.mhxi.myshop.product.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.product.entity.AttrValue;
import top.mhxi.myshop.product.service.AttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "AttrValue管理")
@RestController
@RequestMapping("/product/attrValue")
public class AttrValueController {

    @Autowired
    private AttrValueService attrValueService;


    @Operation(summary = "添加一个AttrValue")
    @PostMapping
    public R insert(@RequestBody AttrValue attrValue) {

        int i = attrValueService.insert(attrValue);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个AttrValue")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = attrValueService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个AttrValue")
    @PutMapping
    public R update(@RequestBody AttrValue attrValue) {

        int i = attrValueService.update(attrValue);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个AttrValue")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        AttrValue attrValue = attrValueService.selectById(id);

        if(attrValue != null) {
            return R.ok().data("record", attrValue);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有AttrValue")
    @GetMapping("/selectAll/{id}")
    public R selectAll(@PathVariable Long id) {
        List<AttrValue> attrValues = attrValueService.selectAll(id);

        if(!attrValues.isEmpty()) {
            return R.ok().data("record", attrValues);
        } else {
            return R.error().message("没有数据");
        }
    }

}