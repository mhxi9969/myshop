package top.mhxi.myshop.product.service;


import com.github.pagehelper.PageInfo;
import top.mhxi.myshop.product.entity.ProductSpu;
import top.mhxi.myshop.product.entity.query.ProductSpuQueryCondition;

import java.util.List;

public interface ProductSpuService {
    int insert(ProductSpu productSpu);
    int deleteById(Long id);
    int update(ProductSpu productSpu);
    ProductSpu selectById(Long id);
    List<ProductSpu> selectAll();

    PageInfo<ProductSpu> selectByCondition(int current, ProductSpuQueryCondition productSpuQueryCondition);
}