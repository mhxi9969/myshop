package top.mhxi.myshop.common.to;

import lombok.Data;

@Data
public class CartItemTO {

    private Long skuId;

    private Integer quantity;

    private Integer checked;
}
