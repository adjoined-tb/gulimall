package me.adjoined.gulimall.order;

import lombok.Data;
import lombok.ToString;
import me.adjoined.gulimall.order.vo.InfoTo;
import me.adjoined.gulimall.order.vo.LuluTo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallOrderApplicationTests {
    private static final Logger logger = LogManager.getLogger(GulimallOrderApplicationTests.class);

    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void createExchange() {
        DirectExchange directExchange = new DirectExchange("hello.java.exchange.direct", true, false, null);
        amqpAdmin.declareExchange(directExchange);
        logger.info("Exchange [{}] created", "hello.java.exchange.direct");

    }

    @Test
    void createQueue() {
        Queue queue = new Queue("hello-java-queue", true, false, false, null);
        amqpAdmin.declareQueue(queue);
        logger.info("Queue [{}] created", "hello-java-queue");
    }

    @Test
    void createBinding() {
        Binding binding = new Binding("hello-java-queue",
                Binding.DestinationType.QUEUE,
                "hello.java.exchange.direct",
                "hello.java",
                null);
        amqpAdmin.declareBinding(binding);
        logger.info("Binding [{}] created", "hello-java-binding");
    }

    @Test
    void sendMessage() {
        rabbitTemplate.convertAndSend("hello.java.exchange.direct", "hello.java", "Hello rabbit");
        logger.info("Message sent");
    }

    @Test
    void sendObjectMessage() {
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                InfoTo infoTo = new InfoTo();
                infoTo.setUserId("id" + i);
                infoTo.setUserKey("key" + i);
                rabbitTemplate.convertAndSend("hello.java.exchange.direct", "hello.java", infoTo);

            } else {
                LuluTo luluTo = new LuluTo();
                luluTo.setLu(i);
                rabbitTemplate.convertAndSend("hello.java.exchange.direct", "hello.java", luluTo);
            }

            logger.info("Message sent");

        }

    }

}
