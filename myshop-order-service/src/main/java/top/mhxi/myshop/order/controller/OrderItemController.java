package top.mhxi.myshop.order.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.order.entity.OrderItem;
import top.mhxi.myshop.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "OrderItem管理")
@RestController
@RequestMapping("/order/orderItem")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;


    @Operation(summary = "添加一个OrderItem")
    @PostMapping
    public R insert(@RequestBody OrderItem orderItem) {

        int i = orderItemService.insert(orderItem);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个OrderItem")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = orderItemService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个OrderItem")
    @PutMapping
    public R update(@RequestBody OrderItem orderItem) {

        int i = orderItemService.update(orderItem);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个OrderItem")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        OrderItem orderItem = orderItemService.selectById(id);

        if(orderItem != null) {
            return R.ok().data("record", orderItem);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据Order的id，查询所有OrderItem")
    @GetMapping("/selectAll/{id}")
    public R selectAll(@PathVariable Long id) {
        List<OrderItem> orderItems = orderItemService.selectAll(id);

        if(!orderItems.isEmpty()) {
            return R.ok().data("record", orderItems);
        } else {
            return R.error();
        }
    }
}