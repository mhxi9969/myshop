package top.mhxi.myshop.product.mapper;

import java.util.List;

import top.mhxi.myshop.product.entity.ProductSpu;
import top.mhxi.myshop.product.entity.query.ProductSpuQueryCondition;

public interface ProductSpuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductSpu record);

    ProductSpu selectByPrimaryKey(Long id);

    List<ProductSpu> selectAll();

    int updateByPrimaryKey(ProductSpu record);

    List<ProductSpu> selectByCondition(ProductSpuQueryCondition productSpuQueryCondition);
}