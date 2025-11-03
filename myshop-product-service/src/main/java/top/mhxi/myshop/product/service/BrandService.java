package top.mhxi.myshop.product.service;


import com.github.pagehelper.PageInfo;
import top.mhxi.myshop.product.entity.Brand;
import top.mhxi.myshop.product.entity.query.BrandQueryCondition;

import java.util.List;

public interface BrandService {
    int insert(Brand brand);
    int deleteById(Long id);
    int update(Brand brand);
    Brand selectById(Long id);
    List<Brand> selectAll();

    PageInfo<Brand> selectByCondition(int current, BrandQueryCondition brandQueryCondition);
}