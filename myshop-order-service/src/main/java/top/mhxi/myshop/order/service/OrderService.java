package top.mhxi.myshop.order.service;


import com.github.pagehelper.PageInfo;
import top.mhxi.myshop.order.entity.Order;
import top.mhxi.myshop.order.entity.query.OrderQueryCondition;
import top.mhxi.myshop.order.entity.vo.OrderSubmitVO;

import java.util.List;

public interface OrderService {
    Long insert(OrderSubmitVO orderSubmitVO, String sessionId);
    int deleteById(Long id);
    int update(Order order);
    Order selectById(Long id);
    List<Order> selectAll(String sessionId);

    boolean poll(String id);

    String getOrderToken();

    PageInfo<Order> selectByCondition(int current, OrderQueryCondition condition);
}