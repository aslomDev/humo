package com.mycompany.myapp.service.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElmaToAdmin {

    public static final String QUEUE = "humoToAdmin";
    public static final String EXCHANGE = "exHumoToAdmin";
    public static final String ROUTING_KEY = "rkHumoToAdmin";

    @Bean
    public Queue adminConsumer() {
        return new Queue(QUEUE);
    }


    @Bean
    public TopicExchange adminExchangeConsumer() {
        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public Binding adminBindingConsumer(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter adminMessageConverterConsumer() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(adminMessageConverterConsumer());
        return template;
    }

}
