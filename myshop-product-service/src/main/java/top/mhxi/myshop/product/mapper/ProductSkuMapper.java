package top.mhxi.myshop.product.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.mhxi.myshop.product.entity.ProductSku;
import top.mhxi.myshop.product.entity.query.ProductSkuQueryCondition;

public interface ProductSkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductSku record);

    ProductSku selectByPrimaryKey(Long id);

    List<ProductSku> selectAll(Long id);

    int updateByPrimaryKey(ProductSku record);

    List<ProductSku> selectByCondition(ProductSkuQueryCondition productSkuQueryCondition);

    @Update("UPDATE product_product_sku SET stock = stock - #{num} WHERE id = #{id}")
    void updateStock(@Param("id") Long id,@Param("num") Integer num);
}