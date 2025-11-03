package top.mhxi.myshop.product.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import top.mhxi.myshop.common.to.AttrTO;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.product.entity.Attr;
import top.mhxi.myshop.product.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Attr管理")
@RestController
@RequestMapping("/product/attr")
public class AttrController {

    @Autowired
    private AttrService attrService;


    @Operation(summary = "添加一个Attr")
    @PostMapping
    public R insert(@RequestBody Attr attr) {

        int i = attrService.insert(attr);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个Attr")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = attrService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个Attr")
    @PutMapping
    public R update(@RequestBody Attr attr) {

        int i = attrService.update(attr);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个Attr")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        Attr attr = attrService.selectById(id);

        if(attr != null) {

            return R.ok().data("record", attr);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据二级分类id，查询所有Attr")
    @GetMapping("/selectAll/{id}")
    public R selectAll(@PathVariable Long id) {
        List<Attr> attrs = attrService.selectAll(id);

        if(!attrs.isEmpty()) {
            return R.ok().data("record", attrs);
        } else {
            return R.error().message("没有数据");
        }
    }


    @Operation(summary = "根据二级分类id，查询所有复杂Attr，带属性值选项")
    @GetMapping("/selectAllTree/{id}")
    public R selectAllTree(@PathVariable Long id) {
        List<AttrTO> list = attrService.selectAllTree(id);

        if(!list.isEmpty()) {
            return R.ok().data("record", list);
        } else {
            return R.error().message("没有数据");
        }
    }
}