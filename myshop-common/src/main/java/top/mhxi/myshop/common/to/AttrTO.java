package top.mhxi.myshop.common.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


import java.util.Date;
import java.util.List;


@Data
public class AttrTO {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Long)
    private Long linkId;

    @Field(type = FieldType.Long)
    private Long skuValueId;

    @Field(type = FieldType.Keyword)
    private String skuValueName;

    @Field(type = FieldType.Nested)
    private List<AttrValueTO> attrValueTOs;

    @Field(type = FieldType.Date)
    @Schema(description = "创建时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")
    private Date createTime;

    @Field(type = FieldType.Date)
    @Schema(description = "修改时间", type = "string", example = "2025-10-21 14:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Tokyo")  // 必须在这里加注解，才能正确转换json时间到java对象
    private Date updateTime;
}