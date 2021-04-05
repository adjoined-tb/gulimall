package me.adjoined.gulimall.product.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.ResultType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import me.adjoined.gulimall.product.entity.CategoryEntity;
import me.adjoined.gulimall.product.service.CategoryService;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.R;


/**
 * ÉÌÆ·Èý¼¶·ÖÀà
 *
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:05:37
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    private static final Logger logger = LogManager.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    //http://adjoined.me/api/product/category/redis/son
    @RequestMapping("/redis/son")
    public R redisSon() throws InterruptedException {
        RLock lock = redissonClient.getLock("my-lock");
        lock.lock();
        try {
            logger.info("Got the lock");
            getCatalogFromDB();
        } finally {
            logger.info("releasing the lock");
            lock.unlock();
        }
        return R.ok().put("redisson", "pass");
    }

    @ResponseBody
    @RequestMapping("redis/updateCatalog")
    public String updateCatalog() throws InterruptedException {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("catalog-rw");
        RLock rLock = readWriteLock.writeLock();
        rLock.lock();
        logger.info("updating db");
        Thread.sleep(10000);
        stringRedisTemplate.delete("catalogJSON");
        logger.info("db updated");
        rLock.unlock();
        return "ok";
    }
    //http://adjoined.me/api/product/category/redis/getCatalog
    @ResponseBody
    @RequestMapping("/redis/getCatalog")
    public Map<String, List<String>> getCatalog() throws InterruptedException {
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            logger.info("cache hit");
            return new Gson().fromJson(catalogJSON, new TypeToken<Map<String, List<String>>>(){}.getType());
        }

        logger.info("cache miss");
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("catalog-rw");
        RLock rLock = readWriteLock.readLock();
        Map<String, List<String>> result = null;
        rLock.lock();
        try {
            result = getCatalogFromDBWithRedissonLock();
        } finally {
            rLock.unlock();
        }

        return result;
    }

    private Map<String, List<String>> getCatalogFromDBWithRedissonLock() throws InterruptedException {
        RLock lock = redissonClient.getLock("catalogJson-lock");
        lock.lock();
        Map<String, List<String>> dataFromDb;
        try {
            dataFromDb = getCatalogFromDB();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }

    private Map<String, List<String>> getCatalogFromDB() throws InterruptedException {
        // check redis first
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            Map<String, List<String>> result = new Gson().fromJson(catalogJSON, new TypeToken<Map<String, List<String>>>(){}.getType());
            return result;
        }
        logger.info("Sending query to db");
        Thread.sleep(10000);
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        result.put("furniture", ImmutableList.of(UUID.randomUUID().toString(), "table", "chair"));
        logger.info("Got query result from db");
        stringRedisTemplate.opsForValue().set("catalogJSON", new Gson().toJson(result),60, TimeUnit.SECONDS);
        return result;
    }


    @ResponseBody
    @GetMapping("/write")
    public String writeValue() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        RLock wlock = lock.writeLock();
        try {
            wlock.lock();
            Thread.sleep(30000);
            s = UUID.randomUUID().toString();
            stringRedisTemplate.opsForValue().set("writeValue", s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            wlock.unlock();
        }
        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        RLock rlock = lock.readLock();
        rlock.lock();
        try {
            Thread.sleep(5000);
            s = stringRedisTemplate.opsForValue().get("writeValue");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rlock.unlock();
        }
        return s;
    }



    @ResponseBody
    @RequestMapping("/lockGate")
    public String lockGate() throws InterruptedException {
        RCountDownLatch latch = redissonClient.getCountDownLatch("school-gate");
        latch.trySetCount(5);
        latch.await();
        return "vacation time";

    }

    @ResponseBody
    @RequestMapping("/soLong/{id}")
    public String soLong(@PathVariable("id") Long id) {
        RCountDownLatch latch = redissonClient.getCountDownLatch("school-gate");
        latch.countDown();
        return id + " is gone...";
    }


    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.acquire();
        return "parked";
    }

    @GetMapping("/leave")
    @ResponseBody
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release();
        return "leaved";
    }

    @RequestMapping("/test")
    public R test() {
        return R.ok().put("test", "pass");
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
