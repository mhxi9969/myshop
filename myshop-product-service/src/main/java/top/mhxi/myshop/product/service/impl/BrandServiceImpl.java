package top.mhxi.myshop.product.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.entity.query.BrandQueryCondition;
import top.mhxi.myshop.product.mapper.BrandMapper;
import top.mhxi.myshop.product.entity.Brand;
import top.mhxi.myshop.product.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;


    public int insert(Brand brand) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        brand.setId(idWorker.nextId());
        return brandMapper.insert(brand);
    }


    public int deleteById(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }


    public int update(Brand brand) {
        return brandMapper.updateByPrimaryKey(brand);
    }


    public Brand selectById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }


    public List<Brand> selectAll() {
        return brandMapper.selectAll();
    }

    @Override
    public PageInfo<Brand> selectByCondition(int current, BrandQueryCondition brandQueryCondition) {
        PageHelper.startPage(current, 3);
        List<Brand> list = brandMapper.selectByCondition(brandQueryCondition);

        return new PageInfo<Brand>(list);
    }

}