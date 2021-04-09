package me.adjoined.gulimall.order.listener;

import com.rabbitmq.client.Channel;
import me.adjoined.gulimall.order.vo.LuluTo;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RabbitListener(queues = "order.seckill.order.queue")
@Component
public class OrderSeckillListener {
    @RabbitHandler
    public void listen(Message message, String content, Channel channel) throws InterruptedException {
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
