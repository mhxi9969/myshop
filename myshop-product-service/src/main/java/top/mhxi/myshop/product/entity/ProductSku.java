package top.mhxi.myshop.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class ProductSku {
    private Long id;

    private String name;

    private Long spuId;

    private String picture;

    private BigDecimal price;

    private Integer stock;


    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date createTime;

    @Schema(description = "修改时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")  // 必须在这里加注解，才能正确转换json时间到java对象
    private Date updateTime;
}