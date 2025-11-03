package top.mhxi.myshop.product.mapper;

import java.util.List;
import top.mhxi.myshop.product.entity.AttrValue;

public interface AttrValueMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AttrValue record);

    AttrValue selectByPrimaryKey(Long id);

    List<AttrValue> selectAll(Long id);

    int updateByPrimaryKey(AttrValue record);
}