package top.mhxi.myshop.order.mapper;

import java.util.List;
import top.mhxi.myshop.order.entity.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    Order selectByPrimaryKey(Long id);

    List<Order> selectAll(Long id);

    int updateByPrimaryKey(Order record);
}