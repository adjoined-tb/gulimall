package me.adjoined.gulimall.order.web;

import me.adjoined.gulimall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

@Controller
public class OrderWebController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/place")
    public String place(@RequestParam(value="token", defaultValue = "") String token, Model model) throws ExecutionException, InterruptedException {
        System.out.println(token);
        boolean placed = orderService.placeOrder(token);
        if (placed) {
            return "placed";
        } else {
            return "aiyowei";
        }
    }

    @GetMapping("/prepare")
    public String prepare(Model model) throws ExecutionException, InterruptedException {
        String token = orderService.prepareOrder();
        model.addAttribute("token", token);
        return "confirm";
    }
}
