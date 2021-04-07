package me.adjoined.gulimall.order.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void initRabbitTemplate() {
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                System.out.println("confirmed...");
//                System.out.println("correlationData..." + correlationData);
//                System.out.println("ack..." + ack);
//                System.out.println("cause..." + cause);
//            }
//        });
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            System.out.println("confirmed...");
            System.out.println("correlationData..." + correlationData);
            System.out.println("ack..." + ack);
            System.out.println("cause..." + cause);
        }));
        /*
            private final Message message;
            private final int replyCode;
            private final String replyText;
            private final String exchange;
            private final String routingKey;
         */

//        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
//              @Override
//              public void returnedMessage(ReturnedMessage returned) {
//                  System.out.println("something wrong, returned");
//                  System.out.println("returned message: " + returned);
//              }
//          }
//        );
        rabbitTemplate.setReturnsCallback(returned -> {
            System.out.println("returned message: " + returned);
        });
    }
}
