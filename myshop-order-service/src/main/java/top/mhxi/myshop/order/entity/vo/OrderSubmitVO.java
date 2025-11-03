package top.mhxi.myshop.order.entity.vo;

import lombok.Data;
import top.mhxi.myshop.order.entity.Order;

import java.util.List;

@Data
public class OrderSubmitVO {
    private Order order;
    private List<String> skuPrice;
}
