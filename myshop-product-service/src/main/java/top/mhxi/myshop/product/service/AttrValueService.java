package top.mhxi.myshop.product.service;


import top.mhxi.myshop.product.entity.AttrValue;

import java.util.List;

public interface AttrValueService {
    int insert(AttrValue attrValue);
    int deleteById(Long id);
    int update(AttrValue attrValue);
    AttrValue selectById(Long id);
    List<AttrValue> selectAll(Long id);
}