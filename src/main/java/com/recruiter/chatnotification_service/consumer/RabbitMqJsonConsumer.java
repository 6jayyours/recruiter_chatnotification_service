package com.recruiter.chatnotification_service.consumer;

import com.recruiter.chatnotification_service.dto.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqJsonConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqJsonConsumer.class);



    @RabbitListener(queues = {"${rabbitmq.queue.json.name}"})
    public void consume(NotificationMessage notificationMessage){
        LOGGER.info(String.format("Received json  message -> %s", notificationMessage.toString()));
    }
}
