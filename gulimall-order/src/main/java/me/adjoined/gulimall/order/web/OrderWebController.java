package me.adjoined.gulimall.order.web;

import me.adjoined.gulimall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderWebController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/place")
    public String place() {
        String msg = orderService.confirmOrder();
        return "confirm";
    }
}
