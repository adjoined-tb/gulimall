package me.adjoined.gulimall.order.to;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;
import me.adjoined.gulimall.order.entity.OrderEntity;
import me.adjoined.gulimall.order.entity.OrderItemEntity;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateTo {
    private OrderEntity orderEntity;
    private List<OrderItemEntity> orderItems;
    private BigDecimal payPrice;
    private BigDecimal fare;
}
