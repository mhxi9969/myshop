package top.mhxi.myshop.product.service.impl;


import lombok.extern.slf4j.Slf4j;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.mapper.AttrValueMapper;
import top.mhxi.myshop.product.entity.AttrValue;
import top.mhxi.myshop.product.service.AttrValueService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Service
public class AttrValueServiceImpl implements AttrValueService {

    @Autowired
    private AttrValueMapper attrValueMapper;


    public int insert(AttrValue attrValue) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        attrValue.setId(idWorker.nextId());
        return attrValueMapper.insert(attrValue);
    }


    public int deleteById(Long id) {
        return attrValueMapper.deleteByPrimaryKey(id);
    }


    public int update(AttrValue attrValue) {
        return attrValueMapper.updateByPrimaryKey(attrValue);
    }


    public AttrValue selectById(Long id) {
        return attrValueMapper.selectByPrimaryKey(id);
    }


    public List<AttrValue> selectAll(Long id) {
        return attrValueMapper.selectAll(id);
    }

}