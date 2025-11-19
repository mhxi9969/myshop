package top.mhxi.myshop.product.service.impl;


import lombok.extern.slf4j.Slf4j;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.utils.ResultCode;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.entity.Attr;
import top.mhxi.myshop.product.mapper.AttrMapper;
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

    @Autowired
    private AttrMapper attrMapper;


    public int insert(Category category) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        category.setId(idWorker.nextId());
        return categoryMapper.insert(category);
    }


    public int deleteById(Long id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        // 如果是一级分类，看是否有2级分类，有则不能删除
        if (category.getParentId() == 0) {
            List<Category> categories = categoryMapper.selectAll2(category.getId());
            if (!categories.isEmpty()) {
                throw new MyShopException(ResultCode.ERROR, "该1级分类下有2级分类，无法删除");
            }
            // 如果是2级分类，看是否有属性，有则不能删除
        } else {
            List<Attr> attrs = attrMapper.selectAll(category.getId());
            if (!attrs.isEmpty()) {
                throw new MyShopException(ResultCode.ERROR, "该2级分类下有属性，无法删除");
            }
        }
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