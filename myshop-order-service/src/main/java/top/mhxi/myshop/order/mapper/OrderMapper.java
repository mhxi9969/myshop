package top.mhxi.myshop.order.mapper;

import java.util.List;
import top.mhxi.myshop.order.entity.Order;
import top.mhxi.myshop.order.entity.query.OrderQueryCondition;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    Order selectByPrimaryKey(Long id);

    List<Order> selectAll(Long id);

    int updateByPrimaryKey(Order record);

    List<Order> selectByCondition(OrderQueryCondition condition);
}