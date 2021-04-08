package me.adjoined.gulimall.order.web;

import me.adjoined.gulimall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/place")
    public String place() throws ExecutionException, InterruptedException {
        String msg = orderService.confirmOrder();
        return "confirm";
    }
}
