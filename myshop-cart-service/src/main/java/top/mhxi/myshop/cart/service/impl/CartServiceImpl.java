package top.mhxi.myshop.cart.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.mhxi.myshop.cart.service.CartService;
import top.mhxi.myshop.common.to.CartItemTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 增加购物项
    @Override
    public int insert(CartItemTO cartItemTO, String sessionId) {
        try {
            String key = "cart:" + sessionId;
            String field = cartItemTO.getSkuId().toString();

            // 检查购物项是否已经存在
            String existing = (String) redisTemplate.opsForHash().get(key, field);

            // 如果购物项存在，则添加购物项数量
            if (existing != null) {
                CartItemTO existingCart = objectMapper.readValue(existing, CartItemTO.class);
                existingCart.setQuantity(existingCart.getQuantity() + cartItemTO.getQuantity());
                redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(existingCart));
                // 如果购物项不存在，则新增购物项
            } else {
                redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(cartItemTO));
            }

            // 更新购物车的过期时间，365天
            redisTemplate.expire(key, 365, TimeUnit.DAYS);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 删除购物项
    @Override
    public int deleteById(Long skuId, String sessionId) {
        try {
            String key = "cart:" + sessionId;
            Long removed = redisTemplate.opsForHash().delete(key, skuId.toString());
            return removed != null && removed > 0 ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 更新购物项（修改数量）
    @Override
    public int update(CartItemTO cartItemTO, String sessionId) {
        try {
            String key = "cart:" + sessionId;
            String field = cartItemTO.getSkuId().toString();

            if (redisTemplate.opsForHash().hasKey(key, field)) {
                redisTemplate.opsForHash().put(key, field, objectMapper.writeValueAsString(cartItemTO));
                redisTemplate.expire(key, 365, TimeUnit.DAYS);
                return 1;
            } else {
                return 0; // 购物项不存在
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 查询购物车所有项
    @Override
    public List<CartItemTO> selectAll(String sessionId) {
        List<CartItemTO> list = new ArrayList<>();
        try {
            String key = "cart:" + sessionId;
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            // 从值中遍历，转成CartItemTO
            for (Object value : entries.values()) {
                CartItemTO cartItemTO = objectMapper.readValue((String) value, CartItemTO.class);
                list.add(cartItemTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 查询购物车所有选中的购物项
    @Override
    public List<CartItemTO> selectAllChecked(String sessionId) {
        List<CartItemTO> list = new ArrayList<>();
        try {
            String key = "cart:" + sessionId;
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            for (Object value : entries.values()) {
                CartItemTO cartItemTO = objectMapper.readValue((String) value, CartItemTO.class);

                // 返回选中的购物项
                if (cartItemTO.getChecked() == 1) {
                    list.add(cartItemTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
