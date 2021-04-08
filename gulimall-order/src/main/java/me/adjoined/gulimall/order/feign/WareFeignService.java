package me.adjoined.gulimall.order.feign;

import me.adjoined.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("gulimall-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/lock/order")
    R orderLockStock();
}
