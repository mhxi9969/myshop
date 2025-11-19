package top.mhxi.myshop.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mhxi.myshop.cart.service.CartService;
import top.mhxi.myshop.common.to.CartItemTO;
import top.mhxi.myshop.common.utils.R;

import java.util.List;

@Tag(name = "Cart管理")
@RestController
@RequestMapping("/cart/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "添加一个CartItemTO")
    @PostMapping
    public R insert(@RequestBody CartItemTO cartItemTO,
                    @CookieValue(value = "SESSIONID", required = false) String sessionId) {

        int i = cartService.insert(cartItemTO, sessionId);
        if (i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @Operation(summary = "根据id删除一个CartItemTO")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id,
                        @CookieValue(value = "SESSIONID", required = false) String sessionId) {
        int i = cartService.deleteById(id, sessionId);
        if (i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @Operation(summary = "更新一个CartItemTO")
    @PutMapping
    public R update(@RequestBody CartItemTO cartItemTO,
                    @CookieValue(value = "SESSIONID", required = false) String sessionId) {

        int i = cartService.update(cartItemTO, sessionId);
        if (i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @Operation(summary = "查询所有CartItemTO")
    @GetMapping("/selectAll")
    public R selectAll(@CookieValue(value = "SESSIONID", required = false) String sessionId) {
        List<CartItemTO> cartItemTOs = cartService.selectAll(sessionId);

        if (!cartItemTOs.isEmpty()) {
            return R.ok().data("record", cartItemTOs);
        } else {
            return R.error();
        }
    }

    @Operation(summary = "查询所有已选择的CartItemTO")
    @GetMapping("/selectAllChecked")
    public R selectAllChecked(@CookieValue(value = "SESSIONID", required = false) String sessionId) {
        List<CartItemTO> cartItemTOs = cartService.selectAllChecked(sessionId);

        if (!cartItemTOs.isEmpty()) {
            return R.ok().data("record", cartItemTOs);
        } else {
            return R.error();
        }
    }
}
