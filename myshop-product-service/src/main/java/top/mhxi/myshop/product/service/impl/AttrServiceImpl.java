package top.mhxi.myshop.product.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.to.AttrTO;
import top.mhxi.myshop.common.to.AttrValueTO;
import top.mhxi.myshop.common.utils.ResultCode;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.entity.AttrValue;

import top.mhxi.myshop.product.mapper.AttrMapper;
import top.mhxi.myshop.product.entity.Attr;
import top.mhxi.myshop.product.mapper.AttrValueMapper;
import top.mhxi.myshop.product.service.AttrService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private AttrMapper attrMapper;

    @Autowired
    private AttrValueMapper attrValueMapper;


    public int insert(Attr attr) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        attr.setId(idWorker.nextId());
        return attrMapper.insert(attr);
    }


    public int deleteById(Long id) {
        // 检查属性下是否有属性值，有则不删除
        List<AttrValue> attrValues = attrValueMapper.selectAll(id);
        if (!attrValues.isEmpty()) {
            throw new MyShopException(ResultCode.ERROR, "该属性下有属性值，无法删除");
        }
        return attrMapper.deleteByPrimaryKey(id);
    }


    public int update(Attr attr) {
        return attrMapper.updateByPrimaryKey(attr);
    }


    public Attr selectById(Long id) {
        return attrMapper.selectByPrimaryKey(id);
    }


    public List<Attr> selectAll(Long id) {
        return attrMapper.selectAll(id);
    }

    @Override
    public List<AttrTO> selectAllTree(Long id) {
        List<AttrTO> attrTOs = new ArrayList<>();
        List<Attr> attrs = attrMapper.selectAll(id);

        for (Attr attr : attrs) {
            AttrTO attrTO = new AttrTO();
            BeanUtils.copyProperties(attr, attrTO);

            List<AttrValue> attrValues = attrValueMapper.selectAll(attrTO.getId());
            List<AttrValueTO> attrValueTOS = new ArrayList<>();
            for (AttrValue value : attrValues) {
                AttrValueTO attrValueTO = new AttrValueTO();
                BeanUtils.copyProperties(value, attrValueTO);
                attrValueTOS.add(attrValueTO);
            }

            attrTO.setAttrValueTOs(attrValueTOS);
            attrTOs.add(attrTO);
        }
        return attrTOs;
    }

}