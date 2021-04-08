package me.adjoined.gulimall.order.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.ImmutableList;
import me.adjoined.common.exception.NoStockException;
import me.adjoined.common.utils.R;
import me.adjoined.gulimall.order.constant.OrderConstant;
import me.adjoined.gulimall.order.entity.OrderItemEntity;
import me.adjoined.gulimall.order.enums.OrderStatusEnum;
import me.adjoined.gulimall.order.exception.OrderTokenException;
import me.adjoined.gulimall.order.feign.CartFeignService;
import me.adjoined.gulimall.order.feign.CouponFeignService;
import me.adjoined.gulimall.order.feign.WareFeignService;
import me.adjoined.gulimall.order.interceptor.LoginUserInterceptor;
import me.adjoined.gulimall.order.service.OrderItemService;
import me.adjoined.gulimall.order.to.OrderCreateTo;
import me.adjoined.gulimall.order.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.Query;

import me.adjoined.gulimall.order.dao.OrderDao;
import me.adjoined.gulimall.order.entity.OrderEntity;
import me.adjoined.gulimall.order.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    OrderItemService orderItemService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

//    @Cacheable(value = "order", key = "'order-detail'")
    @Cacheable(value = "order", key = "#root.method.name", sync = true)
    @Override
    public  Map<String, List<String>> getOrder() {
        System.out.println("check ...");
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        result.put("furniture", ImmutableList.of(UUID.randomUUID().toString(), "table", "chair"));
        return result;
    }

    @CacheEvict(value = "order", key = "'getOrder'")
    @Override
    public void removeOrder() {
        System.out.println("remove ...");
    }


    @Override
    public String prepareOrder() throws ExecutionException, InterruptedException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String user = LoginUserInterceptor.loginUser.get();
        CompletableFuture<Void> couponFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            R memberCoupons = couponFeignService.membercoupons();
            if (memberCoupons != null) {
                System.out.println("coupon info: " + memberCoupons.toString());
            }
        }, executor);

        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<CartItem> items = cartFeignService.getCurrentUserCartItems();
            if (items != null) {
                System.out.println("items: " + items.toString());
            }
        }, executor);
        String token = UUID.randomUUID().toString();
        String key = OrderConstant.USER_ORDER_TOKEN_PREFIX+user;
        redisTemplate.opsForValue().set(key, token, 30, TimeUnit.MINUTES);

        CompletableFuture.allOf(couponFuture, cartFuture).get();
        return token;
    }

    @Transactional
    @Override
    public void placeOrder(String orderToken) {
        String user = LoginUserInterceptor.loginUser.get();
        // check token
        String key = OrderConstant.USER_ORDER_TOKEN_PREFIX+user;
        String lua = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//        String redisToken = redisTemplate.opsForValue().get(key);
//        if (orderToken != null && orderToken.equals(redisToken)) { delete
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(lua, Long.class), Arrays.asList(key), orderToken);
        if (result == 0L) {
            throw new OrderTokenException();
        }

        // order db
        OrderCreateTo orderCreateTo = createOrder();
        this.saveOrder(orderCreateTo);

        // warehouse db
        R r = wareFeignService.orderLockStock();
        if ((Integer) r.get("code") != 0) {
            throw new NoStockException(999L);
        }
//        int i = 10/0;
    }


    private void saveOrder(OrderCreateTo orderCreateTo) {
        OrderEntity orderEntity = orderCreateTo.getOrderEntity();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);
        orderItemService.saveBatch(orderCreateTo.getOrderItems());
    }

    private OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();

        String orderSn = IdWorker.getTimeId();
        OrderEntity entity = new OrderEntity();
        entity.setOrderSn(orderSn);
        entity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderCreateTo.setOrderEntity(entity);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(orderSn);
        orderItemEntity.setCategoryId(100L);
        orderItemEntity.setSkuId(123L);

        orderCreateTo.setOrderItems(ImmutableList.of(orderItemEntity));

        return orderCreateTo;
    }


}