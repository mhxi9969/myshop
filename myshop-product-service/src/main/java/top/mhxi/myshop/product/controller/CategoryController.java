package top.mhxi.myshop.product.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.product.entity.Category;
import top.mhxi.myshop.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "Category管理")
@RestController
@RequestMapping("/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "添加一个Category")
    @PostMapping
    public R insert(@RequestBody Category category) {

        int i = categoryService.insert(category);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个Category")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = categoryService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个Category")
    @PutMapping
    public R update(@RequestBody Category category) {

        int i = categoryService.update(category);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个Category")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        Category category = categoryService.selectById(id);

        if(category != null) {
            return R.ok().data("record", category);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有一级Category")
    @GetMapping("/selectAll")
    public R selectAll() {
        List<Category> categorys = categoryService.selectAll();

        if(!categorys.isEmpty()) {
            return R.ok().data("record", categorys);
        } else {
            return R.error().message("没有数据");
        }
    }

    @Operation(summary = "根据父分类id查询所有二级Category")
    @GetMapping("/selectAll2/{id}")
    public R selectAll2(@PathVariable Long id) {
        List<Category> categorys = categoryService.selectAll2(id);

        if(!categorys.isEmpty()) {
            return R.ok().data("record", categorys);
        } else {
            return R.error().message("没有数据");
        }
    }

    @Operation(summary = "查询所有二级Category")
    @GetMapping("/all2")
    public R all2() {
        List<Category> categorys = categoryService.all2();

        if(!categorys.isEmpty()) {
            return R.ok().data("record", categorys);
        } else {
            return R.error().message("没有数据");
        }
    }
}