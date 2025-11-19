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

    // 直接执行 原子 SQL，防止超卖
    @Update("UPDATE product_product_sku SET stock = stock - #{num} WHERE id = #{id} AND stock >= #{num}")
    int updateStock(@Param("id") Long id, @Param("num") Integer num);

    // 回滚库存
    @Update("UPDATE product_product_sku SET stock = stock + #{num} WHERE id = #{id}")
    int rollBackStock(@Param("id") Long id, @Param("num") Integer num);
}