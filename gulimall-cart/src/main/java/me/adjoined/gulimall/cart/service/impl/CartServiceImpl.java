package me.adjoined.gulimall.cart.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import me.adjoined.gulimall.cart.interceptor.CartInterceptor;
import me.adjoined.gulimall.cart.service.CartService;
import me.adjoined.gulimall.cart.vo.Cart;
import me.adjoined.gulimall.cart.vo.CartItem;
import me.adjoined.gulimall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ThreadPoolExecutor executor;

    private static final String CART_PREFIX = "gulimall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> ops = getCartOps();
        CartItem cartItem = new CartItem();

        CompletableFuture<Void> getSkuInfoFuture = CompletableFuture.runAsync(() -> {
            cartItem.setCheck(true);
            cartItem.setCount(num);
            cartItem.setImage("funny.jpg");
            cartItem.setTitle("bunny");
            cartItem.setSkuId(skuId);
            cartItem.setPrice(new BigDecimal("89.99"));
        }, executor);

        CompletableFuture<Void> getAttrFuture = CompletableFuture.runAsync(() -> {
            cartItem.setSkuAttr(ImmutableList.of("fluffy", "cute"));
        }, executor);

        CompletableFuture.allOf(getAttrFuture, getSkuInfoFuture).get();
        Gson gson = new Gson();
        ops.put(skuId.toString(),gson.toJson(cartItem));
        return cartItem;
    }

    @Override
    public Cart getCart() {
        Cart cart = new Cart();
        BoundHashOperations<String, Object, Object> ops = getCartOps();
        List<Object> values = ops.values();
        if (values != null) {
            List<CartItem> items = values.stream().map(obj -> {
                CartItem cartItem  = new Gson().fromJson((String) obj, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            cart.setItems(items);
        }
        return cart;
    }


    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        return redisTemplate.boundHashOps(cartKey);
    }
}
