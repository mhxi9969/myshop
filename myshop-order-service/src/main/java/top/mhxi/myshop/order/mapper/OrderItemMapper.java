package top.mhxi.myshop.order.mapper;

import java.util.List;
import top.mhxi.myshop.order.entity.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderItem record);

    OrderItem selectByPrimaryKey(Long id);

    List<OrderItem> selectAll(Long id);

    int updateByPrimaryKey(OrderItem record);
}