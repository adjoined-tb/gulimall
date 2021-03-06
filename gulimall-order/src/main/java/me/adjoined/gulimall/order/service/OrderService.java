package me.adjoined.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.gulimall.order.entity.OrderEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * ¶©µ¥
 *
 * @author tianbian
 * @email adjoined.tb@gmail.com
 * @date 2021-03-30 21:47:08
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
    Map<String, List<String>> getOrder();
    void removeOrder();
    String prepareOrder() throws ExecutionException, InterruptedException;
    void placeOrder(String orderToken);
}

