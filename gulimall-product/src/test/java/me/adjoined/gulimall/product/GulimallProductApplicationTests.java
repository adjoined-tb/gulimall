package me.adjoined.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.adjoined.gulimall.product.controller.BrandController;
import me.adjoined.gulimall.product.entity.BrandEntity;
import me.adjoined.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class GulimallProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Autowired
    StringRedisTemplate redisTemplate;
    @Test
    void contextLoads() {
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("huawei");
//        brandService.save(brandEntity);

//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("phone");
//        System.out.println("saved successfully");
//        brandService.updateById(brandEntity);

        List<BrandEntity> brandIds = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        brandIds.forEach((item) -> {
            System.out.println(item);
        });
    }

    @Test
    void testStringRedisTemplate(){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("hello", "world"+ UUID.randomUUID().toString());

        String hello = ops.get("hello");
        System.out.println("got" + hello);
    }

}
