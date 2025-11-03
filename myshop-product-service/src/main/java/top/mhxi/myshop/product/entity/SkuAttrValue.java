package top.mhxi.myshop.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class SkuAttrValue {
    private Long id;

    private Long skuId;

    private Long attrId;

    private Long valueId;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date createTime;

    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date updateTime;

}