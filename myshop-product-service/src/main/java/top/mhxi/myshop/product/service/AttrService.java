package top.mhxi.myshop.product.service;


import top.mhxi.myshop.common.to.AttrTO;
import top.mhxi.myshop.product.entity.Attr;


import java.util.List;

public interface AttrService {
    int insert(Attr attr);
    int deleteById(Long id);
    int update(Attr attr);
    Attr selectById(Long id);
    List<Attr> selectAll(Long id);
    List<AttrTO>  selectAllTree(Long id);
}