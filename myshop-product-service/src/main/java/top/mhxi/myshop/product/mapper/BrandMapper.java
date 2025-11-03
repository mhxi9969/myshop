package top.mhxi.myshop.product.mapper;

import java.util.List;
import top.mhxi.myshop.product.entity.Brand;
import top.mhxi.myshop.product.entity.query.BrandQueryCondition;

public interface BrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Brand record);

    Brand selectByPrimaryKey(Long id);

    List<Brand> selectAll();

    int updateByPrimaryKey(Brand record);

    List<Brand> selectByCondition(BrandQueryCondition brandQueryCondition);
}