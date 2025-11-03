package top.mhxi.myshop.order.service.impl;


import lombok.extern.slf4j.Slf4j;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.order.mapper.OrderItemMapper;
import top.mhxi.myshop.order.entity.OrderItem;
import top.mhxi.myshop.order.service.OrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;


    public int insert(OrderItem orderItem) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        orderItem.setId(idWorker.nextId());
        return orderItemMapper.insert(orderItem);
    }


    public int deleteById(Long id) {
        return orderItemMapper.deleteByPrimaryKey(id);
    }


    public int update(OrderItem orderItem) {
        return orderItemMapper.updateByPrimaryKey(orderItem);
    }


    public OrderItem selectById(Long id) {
        return orderItemMapper.selectByPrimaryKey(id);
    }


    public List<OrderItem> selectAll(Long id) {
        return orderItemMapper.selectAll(id);
    }

}