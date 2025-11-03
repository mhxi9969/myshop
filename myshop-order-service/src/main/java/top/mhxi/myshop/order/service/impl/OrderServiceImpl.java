package top.mhxi.myshop.order.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.common.utils.ResultCode;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.common.to.ProductSkuTO;
import top.mhxi.myshop.common.to.UserTO;
import top.mhxi.myshop.common.to.CartItemTO;
import top.mhxi.myshop.order.entity.OrderItem;
import top.mhxi.myshop.order.entity.vo.OrderSubmitVO;
import top.mhxi.myshop.order.feign.CartFeignClient;
import top.mhxi.myshop.order.feign.PayFeignClient;
import top.mhxi.myshop.order.feign.ProductSkuFeignClient;
import top.mhxi.myshop.order.feign.UserFeignClient;
import top.mhxi.myshop.order.mapper.OrderItemMapper;
import top.mhxi.myshop.order.mapper.OrderMapper;
import top.mhxi.myshop.order.entity.Order;
import top.mhxi.myshop.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private ProductSkuFeignClient productSkuFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private PayFeignClient payFeignClient;


    // 事务，生成订单
    @Transactional
    public Long insert(OrderSubmitVO orderSubmitVO, String sessionId) {
        Order order = orderSubmitVO.getOrder();
        List<String> skuPrice = orderSubmitVO.getSkuPrice();

        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        Long orderId = idWorker.nextId();
        order.setId(orderId);

        order.setStatus(0);
        BigDecimal orderTotal = new BigDecimal(0);

        ObjectMapper mapper = new ObjectMapper();


        // feign拿到user
        R userR = userFeignClient.getUserBySession();
        Object recordObj = userR.getData().get("record");
        UserTO userTO = mapper.convertValue(recordObj, UserTO.class);
        order.setUserId(userTO.getId());


        // feign拿到用户的cart
        R cartR = cartFeignClient.selectAllChecked();

        List<Object> list = (List<Object>) cartR.getData().get("record");

        List<CartItemTO> cartItemTOList = new LinkedList<>();
        for (Object obj : list) {
            CartItemTO c = mapper.convertValue(obj, CartItemTO.class);
            cartItemTOList.add(c);
        }


        // 遍历购物车，获取sku，生成订单项
        for (CartItemTO cartItemTO : cartItemTOList) {
            Long skuId = cartItemTO.getSkuId();
            Integer quantity = cartItemTO.getQuantity();

            // feign拿到每个sku
            R skuR = productSkuFeignClient.selectById(skuId);
            Object skuObj = skuR.getData().get("record");
            ProductSkuTO skuTO = mapper.convertValue(skuObj, ProductSkuTO.class);

            // 确认库存，要加锁，防止超卖
            if(skuTO.getStock() < cartItemTO.getQuantity()) {
                throw new MyShopException(ResultCode.ERROR," 库存不足");
            }

            // 检查和前端传来的价格一致
            for (String s : skuPrice) {
                String[] split = s.split(":");
                if (split[0].equals(skuTO.getId().toString())) {   // 比较要用equals

                    BigDecimal frontPrice = new BigDecimal(split[1]);  // 转成BigDecimal

                    if (frontPrice.compareTo(skuTO.getPrice()) != 0) {
                        throw new MyShopException(ResultCode.ERROR,"商品价格已经变动，请刷新页面后重新提交");
                    }
                }
            }


            //生成订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setId(idWorker.nextId());
            orderItem.setOrderId(orderId);
            orderItem.setSkuId(skuTO.getId());
            orderItem.setSkuName(skuTO.getName());
            orderItem.setSkuPicture(skuTO.getPicture());
            orderItem.setSkuPrice(skuTO.getPrice());
            orderItem.setSkuQuantity(quantity);

            BigDecimal b = orderItem.getSkuPrice().multiply(BigDecimal.valueOf(quantity));
            orderItem.setTotalPrice(b);
            orderTotal = orderTotal.add(b);

            orderItemMapper.insert(orderItem);
        }

        order.setTotalAmount(orderTotal);

        // 生成支付链接
        R paymentR = payFeignClient.createPayment(order.getId(), order.getTotalAmount());
        Object url = paymentR.getData().get("record");
        String s = mapper.convertValue(url, String.class);
        order.setTradeId(s);

        // 生成订单
        orderMapper.insert(order);


        // 清空购物车
        for (CartItemTO cartItemTO : cartItemTOList) {
            Long id = cartItemTO.getSkuId();
            cartFeignClient.deleteById(id);
        }

        return orderId;
    }


    public int deleteById(Long id) {
        return orderMapper.deleteByPrimaryKey(id);
    }


    public int update(Order order) {
        return orderMapper.updateByPrimaryKey(order);
    }


    public Order selectById(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }


    public List<Order> selectAll(String sessionId) {
        // feign拿到user
        R userR = userFeignClient.getUserBySession();
        Object recordObj = userR.getData().get("record");
        ObjectMapper mapper = new ObjectMapper();

        UserTO userTO = mapper.convertValue(recordObj, UserTO.class);


        return orderMapper.selectAll(userTO.getId());
    }

    @Override
    public boolean poll(String id) {
        R r = payFeignClient.poll(id);

        Object obj = r.getData().get("record");
        ObjectMapper mapper = new ObjectMapper();
        String  s = mapper.convertValue(obj, String.class);
        if(s.equals("COMPLETED")) {
            Order order = orderMapper.selectByPrimaryKey(Long.valueOf(id));
            order.setStatus(1);
            orderMapper.updateByPrimaryKey(order);
            return true;
        }

        return false;
    }

}