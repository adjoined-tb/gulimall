package me.adjoined.gulimall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderWebController {
    @GetMapping("/place")
    public String place() {
        return "confirm";
    }
}
