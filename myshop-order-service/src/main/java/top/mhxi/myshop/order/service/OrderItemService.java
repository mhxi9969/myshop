package top.mhxi.myshop.order.service;


import top.mhxi.myshop.order.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    int insert(OrderItem orderItem);
    int deleteById(Long id);
    int update(OrderItem orderItem);
    OrderItem selectById(Long id);
    List<OrderItem> selectAll(Long id);
}