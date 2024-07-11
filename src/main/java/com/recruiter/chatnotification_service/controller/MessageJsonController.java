package com.recruiter.chatnotification_service.controller;

import com.recruiter.chatnotification_service.dto.NotificationMessage;
import com.recruiter.chatnotification_service.publisher.RabbitMqJsonProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jsonMessage")
public class MessageJsonController {

    private RabbitMqJsonProducer jsonProducer;

    public MessageJsonController(RabbitMqJsonProducer jsonProducer) {
        this.jsonProducer = jsonProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendJsonMessage(@RequestBody NotificationMessage seeker){
        jsonProducer.sendJsonMessage(seeker);
        return ResponseEntity.ok("Json message send to rabbitmq");

    }
}
