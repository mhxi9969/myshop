package top.mhxi.myshop.product.service.impl;


import lombok.extern.slf4j.Slf4j;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.mapper.SkuAttrValueMapper;
import top.mhxi.myshop.product.entity.SkuAttrValue;
import top.mhxi.myshop.product.service.SkuAttrValueService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Service
public class SkuAttrValueServiceImpl implements SkuAttrValueService {

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;


    public int insert(SkuAttrValue skuAttrValue) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        skuAttrValue.setId(idWorker.nextId());
        return skuAttrValueMapper.insert(skuAttrValue);
    }


    public int deleteById(Long id) {
        return skuAttrValueMapper.deleteByPrimaryKey(id);
    }


    public int update(SkuAttrValue skuAttrValue) {
        return skuAttrValueMapper.updateByPrimaryKey(skuAttrValue);
    }


    public SkuAttrValue selectById(Long id) {
        return skuAttrValueMapper.selectByPrimaryKey(id);
    }


    public List<SkuAttrValue> selectAll() {
        // 废弃方法，用不到，暂时处理
        return skuAttrValueMapper.selectAll(0L);
    }

}