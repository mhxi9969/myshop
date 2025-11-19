package top.mhxi.myshop.product.service.impl;


import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.internal.bytebuddy.implementation.bytecode.Throw;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.to.ProductSkuTreeTO;
import top.mhxi.myshop.common.utils.ResultCode;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.entity.ProductSku;
import top.mhxi.myshop.product.entity.ProductSpu;
import top.mhxi.myshop.product.entity.query.ProductSpuQueryCondition;
import top.mhxi.myshop.product.feigh.SearchFeignClient;
import top.mhxi.myshop.product.mapper.ProductSkuMapper;
import top.mhxi.myshop.product.mapper.ProductSpuMapper;
import top.mhxi.myshop.product.entity.ProductSpu;
import top.mhxi.myshop.product.service.ProductSkuService;
import top.mhxi.myshop.product.service.ProductSpuService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Slf4j
@Service
public class ProductSpuServiceImpl implements ProductSpuService {

    @Autowired
    private ProductSpuMapper productSpuMapper;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    SearchFeignClient searchFeignClient;



    public int insert(ProductSpu productSpu) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        productSpu.setId(idWorker.nextId());
        return productSpuMapper.insert(productSpu);
    }


    public int deleteById(Long id) {
        // 检查spu下是否有sku
        List<ProductSku> productSkus = productSkuMapper.selectAll(id);
        if (!productSkus.isEmpty()) {
            throw new MyShopException(ResultCode.ERROR, "该spu下有sku，无法删除");
        }
        return productSpuMapper.deleteByPrimaryKey(id);
    }


    // 更新时，触发spu上架下架功能
    public int update(ProductSpu productSpu) {
        ProductSpu spu = productSpuMapper.selectByPrimaryKey(productSpu.getId());

        // 上架spu的所有sku
        if(spu.getStatus()==0 && productSpu.getStatus()==1) {
            List<ProductSkuTreeTO> skuTreeTOS = productSkuService.selectAllTreeBySpuId(productSpu.getId());
            searchFeignClient.addProduct(skuTreeTOS);
        }
        // 下架spu的所有sku
        else if (spu.getStatus()==1 && productSpu.getStatus()==0) {
            List<ProductSkuTreeTO> skuTreeTOS = productSkuService.selectAllTreeBySpuId(productSpu.getId());
            searchFeignClient.removeProduct(skuTreeTOS);
        }

        return productSpuMapper.updateByPrimaryKey(productSpu);
    }


    public ProductSpu selectById(Long id) {
        return productSpuMapper.selectByPrimaryKey(id);
    }


    public List<ProductSpu> selectAll() {
        return productSpuMapper.selectAll();
    }

    @Override
    public PageInfo<ProductSpu> selectByCondition(int current, ProductSpuQueryCondition productSpuQueryCondition) {
        PageHelper.startPage(current, 3);
        List<ProductSpu> list = productSpuMapper.selectByCondition(productSpuQueryCondition);

        return new PageInfo<ProductSpu>(list);
    }
}