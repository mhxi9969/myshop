package top.mhxi.myshop.product.entity.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class BrandQueryCondition {

    private String name;

}
