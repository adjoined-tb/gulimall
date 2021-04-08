package me.adjoined.gulimall.order.service.impl;

import com.google.common.collect.ImmutableList;
import me.adjoined.common.utils.R;
import me.adjoined.gulimall.order.feign.CartFeignService;
import me.adjoined.gulimall.order.feign.CouponFeignService;
import me.adjoined.gulimall.order.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.Query;

import me.adjoined.gulimall.order.dao.OrderDao;
import me.adjoined.gulimall.order.entity.OrderEntity;
import me.adjoined.gulimall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    ThreadPoolExecutor executor;

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
    public String confirmOrder() throws ExecutionException, InterruptedException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

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

        CompletableFuture.allOf(couponFuture, cartFuture).get();
        return "order confirmed";
    }
}