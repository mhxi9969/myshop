package top.mhxi.myshop.product.mapper;

import java.util.List;
import top.mhxi.myshop.product.entity.Attr;

public interface AttrMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attr record);

    Attr selectByPrimaryKey(Long id);

    List<Attr> selectAll(Long id);

    int updateByPrimaryKey(Attr record);
}