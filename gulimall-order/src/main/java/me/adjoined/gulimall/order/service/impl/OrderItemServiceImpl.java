package me.adjoined.gulimall.order.service.impl;

import com.rabbitmq.client.Channel;
import me.adjoined.gulimall.order.vo.InfoTo;
import me.adjoined.gulimall.order.vo.LuluTo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.Query;

import me.adjoined.gulimall.order.dao.OrderItemDao;
import me.adjoined.gulimall.order.entity.OrderItemEntity;
import me.adjoined.gulimall.order.service.OrderItemService;

@RabbitListener(queues={"hello-java-queue"})
@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    @RabbitHandler
    public void receiveInfoMessage(Message message, InfoTo content, Channel channel) throws InterruptedException {
        byte[] body = message.getBody();
        MessageProperties properties = message.getMessageProperties();
        System.out.println("got " + message + " type: " + message.getClass());
        Thread.sleep(3000);
        System.out.println("content: " + content);
        try {
            channel.basicAck(properties.getDeliveryTag(), false);
//            channel.basicNack(properties.getDeliveryTag(), false, true);
//            channel.basicReject(properties.getDeliveryTag(), true);
        } catch (IOException e) {
            // network issue
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void receiveLuluMessage(Message message, LuluTo content, Channel channel) throws InterruptedException {
        byte[] body = message.getBody();
        MessageProperties properties = message.getMessageProperties();
        System.out.println("got " + message + " type: " + message.getClass());
        System.out.println("getDeliveryTag " + properties.getDeliveryTag());

        Thread.sleep(3000);
        System.out.println("content: " + content);
        try {
            channel.basicAck(properties.getDeliveryTag(), false);
        } catch (IOException e) {
            // Network issue
            e.printStackTrace();
        }
    }
}