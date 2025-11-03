package top.mhxi.myshop.product.service;


import top.mhxi.myshop.product.entity.SkuAttrValue;

import java.util.List;

public interface SkuAttrValueService {
    int insert(SkuAttrValue skuAttrValue);
    int deleteById(Long id);
    int update(SkuAttrValue skuAttrValue);
    SkuAttrValue selectById(Long id);
    List<SkuAttrValue> selectAll();
}