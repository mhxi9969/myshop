package top.mhxi.myshop.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private Long id;

    private Long userId;

    private BigDecimal totalAmount;

    private Integer status;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private String tradeId;

    private Date payTime;

    private Date deliveryTime;

    private Date receiveTime;

    private Date closeTime;

    private String remark;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date createTime;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date updateTime;

}