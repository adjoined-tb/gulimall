package me.adjoined.gulimall.order.feign;

import me.adjoined.gulimall.order.vo.CartItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("gulimall-cart")
public interface CartFeignService {

    @GetMapping("/currentUserCartItems")
    List<CartItem> getCurrentUserCartItems();
}
