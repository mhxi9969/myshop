package top.mhxi.myshop.order.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.to.ProductSkuTO;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.order.entity.Order;
import top.mhxi.myshop.order.entity.vo.OrderSubmitVO;
import top.mhxi.myshop.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "Order管理")
@RestController
@RequestMapping("/order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @Operation(summary = "添加一个Order")
    @PostMapping
    public R insert(@RequestBody OrderSubmitVO orderSubmitVO,
                    @CookieValue(value = "SESSIONID", required = false) String sessionId) {

        long orderId = orderService.insert(orderSubmitVO, sessionId);
        if(orderId != 0) {
            return R.ok().data("record", orderId);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个Order")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = orderService.deleteById(id);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个Order")
    @PutMapping
    public R update(@RequestBody Order order) {
        int i = orderService.update(order);
        if(i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个Order")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        Order order = orderService.selectById(id);

        if(order != null) {
            return R.ok().data("record", order);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有Order")
    @GetMapping("/selectAll")
    public R selectAll(@CookieValue(value = "SESSIONID", required = false) String sessionId) {
        List<Order> orders = orderService.selectAll(sessionId);

        if(!orders.isEmpty()) {
            return R.ok().data("record", orders);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "轮询更新Order状态")
    @GetMapping("/poll/{id}")
    public R poll(@PathVariable String id) {
        if (orderService.poll(id)) {
            return R.ok().data("record","success");
        }
        return R.error();
    }

}