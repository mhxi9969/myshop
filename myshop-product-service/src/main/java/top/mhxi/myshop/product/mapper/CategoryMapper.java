package top.mhxi.myshop.product.mapper;

import java.util.List;
import top.mhxi.myshop.product.entity.Category;

public interface CategoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    Category selectByPrimaryKey(Long id);

    List<Category> selectAll();

    int updateByPrimaryKey(Category record);

    List<Category> selectAll2(Long id);

    List<Category> all2();
}