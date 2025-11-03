package top.mhxi.myshop.cart.service;

import top.mhxi.myshop.common.to.CartItemTO;

import java.util.List;

public interface CartService {
    int insert(CartItemTO cartItemTO, String sessionId);

    int deleteById(Long id, String sessionId);

    int update(CartItemTO cartItemTO, String sessionId);

    List<CartItemTO> selectAll(String sessionId);

    List<CartItemTO> selectAllChecked(String sessionId);
}
