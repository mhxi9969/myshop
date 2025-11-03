package top.mhxi.myshop.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderItem {
    private Long id;

    private Long orderId;

    private Long skuId;

    private String skuName;

    private String skuPicture;

    private BigDecimal skuPrice;

    private Integer skuQuantity;

    private BigDecimal totalPrice;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date createTime;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date updateTime;

}