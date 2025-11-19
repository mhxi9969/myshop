package top.mhxi.myshop.common.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RollbackStockMessage {
    private Long orderId;
    private Long skuId;
    private Integer quantity;
}