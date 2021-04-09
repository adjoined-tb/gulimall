package me.adjoined.gulimall.seckill.scheduled;

import lombok.extern.slf4j.Slf4j;
import me.adjoined.gulimall.seckill.service.SeckillService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SeckillSkuScheduled {
    private final static String UPLOAD_LOCK = "seckill:upload:lock";
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    SeckillService seckillService;

    @Scheduled(cron = "0/3 * * * * ?")
    public void uploadSeckillSkuLatest3Days() {
        RLock lock = redissonClient.getLock(UPLOAD_LOCK);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.stocking();
        } finally {
            lock.unlock();
        }
    }
}
