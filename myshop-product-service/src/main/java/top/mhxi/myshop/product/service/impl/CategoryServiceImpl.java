package top.mhxi.myshop.product.service.impl;


import lombok.extern.slf4j.Slf4j;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.mapper.CategoryMapper;
import top.mhxi.myshop.product.entity.Category;
import top.mhxi.myshop.product.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    public int insert(Category category) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        category.setId(idWorker.nextId());
        return categoryMapper.insert(category);
    }


    public int deleteById(Long id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }


    public int update(Category category) {
        return categoryMapper.updateByPrimaryKey(category);
    }


    public Category selectById(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }


    public List<Category> selectAll() {
        return categoryMapper.selectAll();
    }

    public List<Category> selectAll2(Long id) {
        return categoryMapper.selectAll2(id);
    }

    @Override
    public List<Category> all2() {
        return categoryMapper.all2();
    }
}