package me.adjoined.gulimall.cart.controller;

import me.adjoined.gulimall.cart.interceptor.CartInterceptor;
import me.adjoined.gulimall.cart.service.CartService;
import me.adjoined.gulimall.cart.vo.CartItem;
import me.adjoined.gulimall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<CartItem> getCurrentUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId() == null) {
            return null;
        } else {
            return cartService.getCart().getItems();
        }
    }
}
