package me.adjoined.gulimall.cart.web;

import me.adjoined.gulimall.cart.interceptor.CartInterceptor;
import me.adjoined.gulimall.cart.service.CartService;
import me.adjoined.gulimall.cart.vo.CartItem;
import me.adjoined.gulimall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@Controller
public class CartWebController {
    @Autowired
    CartService cartService;

    @GetMapping("/cart.html")
    public String list(HttpSession session) {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);
        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart() throws ExecutionException, InterruptedException {
        CartItem cartItem = cartService.addToCart(123L, 10);
        return "success";
    }
}
