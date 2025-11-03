package top.mhxi.myshop.product.service;


import top.mhxi.myshop.product.entity.Category;

import java.util.List;

public interface CategoryService {
    int insert(Category category);
    int deleteById(Long id);
    int update(Category category);
    Category selectById(Long id);
    List<Category> selectAll();
    List<Category> selectAll2(Long id);

    List<Category> all2();

}