package me.adjoined.gulimall.cart.service;

import me.adjoined.gulimall.cart.vo.Cart;
import me.adjoined.gulimall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    Cart getCart();
}
