package top.mhxi.myshop.product.mapper;

import java.util.List;


import top.mhxi.myshop.product.entity.ProductSku;
import top.mhxi.myshop.product.entity.query.ProductSkuQueryCondition;

public interface ProductSkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductSku record);

    ProductSku selectByPrimaryKey(Long id);

    List<ProductSku> selectAll(Long id);

    int updateByPrimaryKey(ProductSku record);

    List<ProductSku> selectByCondition(ProductSkuQueryCondition productSkuQueryCondition);
}