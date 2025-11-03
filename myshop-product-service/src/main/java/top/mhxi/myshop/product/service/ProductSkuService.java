package top.mhxi.myshop.product.service;


import com.github.pagehelper.PageInfo;
import top.mhxi.myshop.product.entity.ProductSku;
import top.mhxi.myshop.product.entity.query.ProductSkuQueryCondition;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;

import java.util.List;

public interface ProductSkuService {
    int insert(ProductSkuTreeTO vo);
    int deleteById(Long id);
    int update(ProductSkuTreeTO vo);
    ProductSkuTreeTO selectByIdTree(Long id);
    List<ProductSkuTreeTO> selectAllTreeBySpuId(Long id);

    ProductSku selectById(Long id);
    PageInfo<ProductSku> selectByCondition(int current, ProductSkuQueryCondition productSkuQueryCondition);


    int updateStock(Long id, Integer num);
}