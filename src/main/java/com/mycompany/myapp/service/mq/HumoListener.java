package com.mycompany.myapp.service.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
public class HumoListener {


    @Transactional
    @RabbitListener(queues = HumoToElma.QUEUE)
    public Mono<Void> lisen(){
        return Mono.empty();
    }

}
