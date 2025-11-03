package top.mhxi.myshop.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemTO {

    private Long skuId;

    private Integer quantity;

    private Integer checked;
}
