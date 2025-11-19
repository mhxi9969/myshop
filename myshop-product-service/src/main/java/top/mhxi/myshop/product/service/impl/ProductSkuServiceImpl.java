package top.mhxi.myshop.product.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.to.AttrTO;
import top.mhxi.myshop.common.to.AttrValueTO;
import top.mhxi.myshop.common.to.RollbackStockMessage;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.common.utils.ResultCode;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.product.entity.Attr;
import top.mhxi.myshop.product.entity.AttrValue;
import top.mhxi.myshop.product.entity.ProductSku;
import top.mhxi.myshop.product.entity.SkuAttrValue;
import top.mhxi.myshop.product.entity.query.ProductSkuQueryCondition;

import top.mhxi.myshop.common.to.ProductSkuTreeTO;
import top.mhxi.myshop.product.mapper.AttrMapper;
import top.mhxi.myshop.product.mapper.AttrValueMapper;
import top.mhxi.myshop.product.mapper.ProductSkuMapper;
import top.mhxi.myshop.product.mapper.SkuAttrValueMapper;
import top.mhxi.myshop.product.service.ProductSkuService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class ProductSkuServiceImpl implements ProductSkuService {

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private AttrMapper attrMapper;

    @Autowired
    private AttrValueMapper attrValueMapper;


    // 多表操作，必须使用事务
    @Transactional
    public int insert(ProductSkuTreeTO productSkuTreeTO) {
        ProductSku productSku = new ProductSku();
        BeanUtils.copyProperties(productSkuTreeTO,productSku);

        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        productSku.setId(idWorker.nextId());
        productSkuMapper.insert(productSku);

        List<AttrTO> attrTOs = productSkuTreeTO.getAttrTOs();
        for (AttrTO attrTO : attrTOs) {
            SkuAttrValue skuAttrValue = new SkuAttrValue();

            skuAttrValue.setId(idWorker.nextId());
            skuAttrValue.setSkuId(productSku.getId());
            skuAttrValue.setAttrId(attrTO.getId());
            skuAttrValue.setValueId(attrTO.getSkuValueId());

            skuAttrValueMapper.insert(skuAttrValue);
        }

        return 1;
    }


    @CacheEvict(value = "ProductSku", key = "#id")
    public int deleteById(Long id) {
        return productSkuMapper.deleteByPrimaryKey(id);
    }


    // 缓存的key要写对，否则会报错
    @CacheEvict(value = "ProductSku", key = "#productSkuTreeTO.id")
    public int update(ProductSkuTreeTO productSkuTreeTO) {
        ProductSku productSku = new ProductSku();
        BeanUtils.copyProperties(productSkuTreeTO,productSku);

        productSkuMapper.updateByPrimaryKey(productSku);

        List<AttrTO> attrTOs = productSkuTreeTO.getAttrTOs();
        for (AttrTO attrTO : attrTOs) {
            SkuAttrValue skuAttrValue = new SkuAttrValue();

            skuAttrValue.setSkuId(productSku.getId());
            skuAttrValue.setId(attrTO.getLinkId());
            skuAttrValue.setAttrId(attrTO.getId());
            skuAttrValue.setValueId(attrTO.getSkuValueId());

            skuAttrValueMapper.updateByPrimaryKey(skuAttrValue);
        }
        return 1;
    }


    public ProductSkuTreeTO selectByIdTree(Long id) {
        ProductSkuTreeTO productSkuTreeTO = new ProductSkuTreeTO();
        ProductSku productSku = productSkuMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(productSku, productSkuTreeTO);

        List<AttrTO> attrTOs = new ArrayList<>();
        productSkuTreeTO.setAttrTOs(attrTOs);

        List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.selectAll(id);

        for (SkuAttrValue skuAttrValue : skuAttrValues) {
            AttrTO attrTO = new AttrTO();

            attrTO.setLinkId(skuAttrValue.getId());
            attrTO.setId(skuAttrValue.getAttrId());
            attrTO.setSkuValueId(skuAttrValue.getValueId());
            attrTOs.add(attrTO);
        }

        for (AttrTO attrTO : attrTOs) {
            Attr attr = attrMapper.selectByPrimaryKey(attrTO.getId());
            BeanUtils.copyProperties(attr, attrTO);

            AttrValue attrValue = attrValueMapper.selectByPrimaryKey(attrTO.getSkuValueId());
            attrTO.setSkuValueName(attrValue.getName());

            // 每个属性对应的所有属性值
            List<AttrValue> attrValues = attrValueMapper.selectAll(attrTO.getId());

            List<AttrValueTO> attrValueTOs = new ArrayList<>();
            for (AttrValue value : attrValues) {
                AttrValueTO attrValueTO = new AttrValueTO();
                BeanUtils.copyProperties(value, attrValueTO);
                attrValueTOs.add(attrValueTO);
            }

            attrTO.setAttrValueTOs(attrValueTOs);

        }

        return productSkuTreeTO;
    }


    public List<ProductSkuTreeTO> selectAllTreeBySpuId(Long id) {
        List<ProductSkuTreeTO> productSkuTreeTOS = new ArrayList<>();

        List<ProductSku> productSkus = productSkuMapper.selectAll(id);
        for (ProductSku sku : productSkus) {
            ProductSkuTreeTO productSkuTreeTO = this.selectByIdTree(sku.getId());
            productSkuTreeTOS.add(productSkuTreeTO);
        }

        return productSkuTreeTOS;
    }


    @Cacheable(value = "ProductSku", key = "#id")
    public ProductSku selectById(Long id) {
        return productSkuMapper.selectByPrimaryKey(id);
    }


    @Override
    public PageInfo<ProductSku> selectByCondition(int current, ProductSkuQueryCondition productSkuQueryCondition) {
        PageHelper.startPage(current, 3);
        List<ProductSku> list = productSkuMapper.selectByCondition(productSkuQueryCondition);

        return new PageInfo<ProductSku>(list);
    }

    // 扣减库存
    @Override
    public void updateStock(Long id, Integer num) {
        int rows = productSkuMapper.updateStock(id, num);
        if (rows == 0) {
            throw new MyShopException(ResultCode.ERROR, "库存不足");
        }
    }

    @Override
    public void rollBackStock(Long id, Integer num) {
        int rows = productSkuMapper.rollBackStock(id, num);
        if (rows == 0) {
            throw new MyShopException(ResultCode.ERROR, "回滚库存失败");
        }
    }


}