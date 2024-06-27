package com.recruiter.chatnotification_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatNotification {

    private Integer id;
    private String  senderId;
    private Integer  receiverId;
    private String content;
}
