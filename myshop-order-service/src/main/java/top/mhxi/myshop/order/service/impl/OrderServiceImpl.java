package top.mhxi.myshop.order.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.transaction.annotation.Transactional;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.to.RollbackStockMessage;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;


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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    // 事务，生成订单
    @Transactional
    public Long insert(OrderSubmitVO orderSubmitVO, String sessionId) {
        String token = orderSubmitVO.getToken();
        if (token == null) {
            throw new MyShopException(ResultCode.ERROR, "请携带token");
        }

        Boolean checkToken = redisTemplate.opsForValue().getOperations().delete("order:token:" + token);
        if (checkToken == false) {
            throw new MyShopException(ResultCode.ERROR, "请勿重复提交订单");
        }

        Order order = orderSubmitVO.getOrder();
        List<String> skuPrice = orderSubmitVO.getSkuPrice();

        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        Long orderId = idWorker.nextId();
        order.setId(orderId);

        //设置order状态，初始状态为0
        order.setStatus(0);
        BigDecimal orderTotal = new BigDecimal(0);

        ObjectMapper mapper = new ObjectMapper();

        // 根据session，从feign拿到user，设置订单的user id
        R userR = userFeignClient.getUserBySession();
        Object recordObj = userR.getData().get("record");
        UserTO userTO = mapper.convertValue(recordObj, UserTO.class);
        order.setUserId(userTO.getId());


        // feign拿到用户的cart，拿到选中的购物项
        R cartR = cartFeignClient.selectAllChecked();
        List<Object> list = (List<Object>) cartR.getData().get("record");

        // 如果购物车选中为空，抛异常
        if (list == null || list.isEmpty()) {
            throw new MyShopException(ResultCode.ERROR, "购物车没有选中项");
        }

        List<CartItemTO> cartItemTOList = new LinkedList<>();
        for (Object obj : list) {
            CartItemTO c = mapper.convertValue(obj, CartItemTO.class);
            cartItemTOList.add(c);
        }


        // 遍历选中的购物项，获取sku，生成订单项
        for (CartItemTO cartItemTO : cartItemTOList) {
            Long skuId = cartItemTO.getSkuId();
            Integer quantity = cartItemTO.getQuantity();

            // feign拿到每个sku
            R skuR = productSkuFeignClient.selectById(skuId);
            Object skuObj = skuR.getData().get("record");
            ProductSkuTO skuTO = mapper.convertValue(skuObj, ProductSkuTO.class);


            // 扣减库存
            R r = productSkuFeignClient.updateStock(skuTO.getId(), cartItemTO.getQuantity());
            if (r.getSuccess() == false) {
                throw new MyShopException(ResultCode.ERROR, "库存不足或扣减库存失败");
            }

            // 扣减库存成功后，发消息到 TTL 队列
            RollbackStockMessage message = new RollbackStockMessage();
            message.setOrderId(orderId);
            message.setSkuId(skuTO.getId());
            message.setQuantity(cartItemTO.getQuantity());

            String json = null;
            try {
                json = mapper.writeValueAsString(message);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            rabbitTemplate.convertAndSend(
                    //发送到普通交换机，进入ttl队列，到期后进入死信交换机，然后进入order.stock.rollback.queue队列
                    "order-delay-exchange",
                    "order.delay",
                    json
            );


            // 检查和前端传来的价格一致
            for (String s : skuPrice) {
                String[] split = s.split(":");
                if (split[0].equals(skuTO.getId().toString())) {   // 比较要用equals

                    BigDecimal frontPrice = new BigDecimal(split[1]);  // 转成BigDecimal

                    if (frontPrice.compareTo(skuTO.getPrice()) != 0) {
                        throw new MyShopException(ResultCode.ERROR, "商品价格已经变动，请刷新页面后重新提交");
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

            // 计算订单项的价格
            BigDecimal b = orderItem.getSkuPrice().multiply(BigDecimal.valueOf(quantity));
            orderItem.setTotalPrice(b);

            // 添加订单的总价格
            orderTotal = orderTotal.add(b);

            // 生成订单项
            orderItemMapper.insert(orderItem);
        }

        // 设置订单的总价格
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

    // 轮询订单的付款状态
    @Override
    public boolean poll(String id) {
        R r = payFeignClient.poll(id);

        Object obj = r.getData().get("record");
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.convertValue(obj, String.class);

        // 已经付款
        if (s.equals("COMPLETED")) {
            Order order = orderMapper.selectByPrimaryKey(Long.valueOf(id));
            order.setStatus(1);
            orderMapper.updateByPrimaryKey(order);
            return true;
        }

        return false;
    }



    // 回滚库存
    @RabbitListener(queues = "order.stock.rollback.queue", ackMode = "MANUAL")
    public void rollbackStock(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        RollbackStockMessage message = mapper.readValue(msg, RollbackStockMessage.class);

        Long orderId = message.getOrderId();
        Long skuId = message.getSkuId();
        Integer quantity = message.getQuantity();

        // 查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);

        // 如果订单未支付
        if (order.getStatus() == 0) {
            R r = productSkuFeignClient.rollBackStock(skuId, quantity);

            if (r.getSuccess() == true) {
                channel.basicAck(tag, false); // 成功确认
            } else {
                System.out.println("回滚库存失败，重新入队");
                channel.basicNack(tag, false, true); // 失败，重新入队
            }

        }
    }

    @Override
    public String getOrderToken() {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("order:token:" + token, "1", 30, TimeUnit.MINUTES);
        return token;
    }

}