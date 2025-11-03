package top.mhxi.myshop.common.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 各个微服务之间传输数据的TO，要放在common包下，依赖的子TO也要放在common下
 */
@Data
@Document(indexName = "sku_index")  // 对应ES索引名
public class ProductSkuTreeTO {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Long)
    private Long spuId;

    @Field(type = FieldType.Keyword)
    private String picture;

    @Field(type = FieldType.Long)
    private Long brandID;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Keyword)
    private String brandPicture;

    @Field(type = FieldType.Long)
    private Long category1ID;

    @Field(type = FieldType.Keyword)
    private String category1Name;

    @Field(type = FieldType.Long)
    private Long category2ID;

    @Field(type = FieldType.Keyword)
    private String category2Name;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Integer)
    private Integer stock;

    @Field(type = FieldType.Nested)
    private List<AttrTO> attrTOs;

    @Field(type = FieldType.Date)
    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date createTime;

    @Field(type = FieldType.Date)
    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date updateTime;
}
