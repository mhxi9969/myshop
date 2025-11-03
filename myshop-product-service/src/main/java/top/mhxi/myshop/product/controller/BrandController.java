package top.mhxi.myshop.product.controller;



import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.product.entity.Brand;
import top.mhxi.myshop.product.entity.query.BrandQueryCondition;
import top.mhxi.myshop.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "Brand管理")
@RestController
@RequestMapping("/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;


    @Operation(summary = "添加一个Brand")
    @PostMapping
    public R insert(@RequestBody Brand brand) {

        int i = brandService.insert(brand);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个Brand")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = brandService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个Brand")
    @PutMapping
    public R update(@RequestBody Brand brand) {

        int i = brandService.update(brand);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个Brand")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        Brand brand = brandService.selectById(id);

        if(brand != null) {
            return R.ok().data("record", brand);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有Brand")
    @GetMapping("/selectAll")
    public R selectAll() {
        List<Brand> brands = brandService.selectAll();

        if(!brands.isEmpty()) {
            return R.ok().data("record", brands);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据条件查询Brand，带分页")
    @PostMapping("/selectByCondition/{current}")
    public R selectByCondition(@PathVariable int current,
                               @RequestBody BrandQueryCondition brandQueryCondition) {
        PageInfo<Brand> info = brandService.selectByCondition(current, brandQueryCondition);

        if(!info.getList().isEmpty()) {

            return R.ok().data("record", info);
        } else {
            return R.error();
        }
    }
}