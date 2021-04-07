package me.adjoined.gulimall.order.controller;

import me.adjoined.gulimall.order.vo.InfoTo;
import me.adjoined.gulimall.order.vo.LuluTo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    // http://localhost:9000/sendMq?num=4
    @GetMapping("/sendMq")
    public String sendMq(@RequestParam(value="num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                InfoTo infoTo = new InfoTo();
                infoTo.setUserId("id" + i);
                infoTo.setUserKey("key" + i);
                rabbitTemplate.convertAndSend("hello.java.exchange.direct", "hello.java.wrong", infoTo);

            } else {
                LuluTo luluTo = new LuluTo();
                luluTo.setLu(i);
                rabbitTemplate.convertAndSend("hello.java.exchange.direct", "hello.java", luluTo);
            }
        }
        return "ok";
    }
}
