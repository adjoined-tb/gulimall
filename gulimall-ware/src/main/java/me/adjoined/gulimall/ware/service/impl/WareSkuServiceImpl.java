package me.adjoined.gulimall.ware.service.impl;

import me.adjoined.common.exception.NoStockException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.adjoined.common.utils.PageUtils;
import me.adjoined.common.utils.Query;

import me.adjoined.gulimall.ware.dao.WareSkuDao;
import me.adjoined.gulimall.ware.entity.WareSkuEntity;
import me.adjoined.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;

@RabbitListener(queues = {"stock.release.stock.queue"})
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void orderLockStock() {
        WareSkuEntity entity1 = new WareSkuEntity();
        entity1.setSkuId(111L);
        entity1.setStockLocked(10);

        WareSkuEntity entity2 = new WareSkuEntity();
        entity2.setSkuId(222L);
        entity2.setStockLocked(20);

        if(!this.save(entity1)) {
            throw new NoStockException(111L);
        }
        if (!this.save(entity2)) {
            throw new NoStockException(222L);
        }

        rabbitTemplate.convertAndSend("stock-event-exchange",
                "stock.locked", "lock the stock for 2 minutes");
//        throw new NoStockException(333L);
    }

    @RabbitHandler
    public void handleStockLockedRelease(String content, Message message, Channel channel) {
        byte[] body = message.getBody();
        MessageProperties properties = message.getMessageProperties();
        System.out.println("got " + message + " type: " + message.getClass());
        System.out.println("getDeliveryTag " + properties.getDeliveryTag());

        System.out.println("content: " + content);
        try {
            channel.basicAck(properties.getDeliveryTag(), false);
        } catch (IOException e) {
            // Network issue
            e.printStackTrace();
        }
    }

}