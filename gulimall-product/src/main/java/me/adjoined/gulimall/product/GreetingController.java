package me.adjoined.gulimall.product;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    @Autowired
    RedissonClient redisson;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        System.out.println("in " + Thread.currentThread().getName());
        RLock lock = redisson.getLock("my-lock");

        lock.lock(10, TimeUnit.SECONDS);
        try {
            System.out.println("locked " + Thread.currentThread().getName());
            Thread.sleep(30000);
        } catch (Exception e) {

        } finally {
            System.out.println("unlock " + Thread.currentThread().getName());
            lock.unlock();
        }
        return "hello";
    }

    @ResponseBody
    @GetMapping("/redis")
    public String redis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", "world_" + UUID.randomUUID().toString());

        String hello = ops.get("hello");

        return hello;
    }
}
