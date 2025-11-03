package top.mhxi.myshop.product.mapper;

import java.util.List;
import top.mhxi.myshop.product.entity.SkuAttrValue;

public interface SkuAttrValueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SkuAttrValue record);

    SkuAttrValue selectByPrimaryKey(Long id);

    List<SkuAttrValue> selectAll(Long id);

    int updateByPrimaryKey(SkuAttrValue record);
}