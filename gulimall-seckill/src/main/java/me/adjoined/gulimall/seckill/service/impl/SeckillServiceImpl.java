package me.adjoined.gulimall.seckill.service.impl;

import me.adjoined.gulimall.seckill.service.SeckillService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    private final static String SECKILL_PRODUCT_PREFIX = "seckill:sku:";
    private final static String SECKILL_SEMAPHORE_PREFIX = "seckill:semaphore:";

    @Override
    public void stocking() {
        String key = SECKILL_PRODUCT_PREFIX + "123";
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForList().leftPushAll(key, "1", "2", "3");
        }
        String sKey = SECKILL_SEMAPHORE_PREFIX + "123";
        if (!redisTemplate.hasKey(sKey)) {
            RSemaphore semaphore = redissonClient.getSemaphore(sKey);
            semaphore.trySetPermits(100);
        }
    }

    @Override
    public String kill() {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent("abc", "efg", 1000, TimeUnit.SECONDS);
        if (!aBoolean) {
            return null;
        }
        String sKey = SECKILL_SEMAPHORE_PREFIX + "123";
        RSemaphore semaphore = redissonClient.getSemaphore(sKey);
        if (!semaphore.tryAcquire(10)) {
            return null;
        }

        String orderSn = UUID.randomUUID().toString();
        // MQ
        rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderSn);
        return orderSn;
    }
}
